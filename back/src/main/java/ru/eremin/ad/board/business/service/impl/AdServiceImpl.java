package ru.eremin.ad.board.business.service.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.eremin.ad.board.business.service.AdService;
import ru.eremin.ad.board.business.service.CategoryService;
import ru.eremin.ad.board.business.service.dto.AdDto;
import ru.eremin.ad.board.input.route.dto.CreateAdRequest;
import ru.eremin.ad.board.input.route.dto.UpdateAdRequest;
import ru.eremin.ad.board.input.route.dto.UpgradeAdRequest;
import ru.eremin.ad.board.output.storage.model.Ad;
import ru.eremin.ad.board.output.storage.model.Category;
import ru.eremin.ad.board.output.storage.model.enumirate.AdType;
import ru.eremin.ad.board.output.storage.repository.AdRepository;

import static ru.eremin.ad.board.util.error.Errors.AD_DOES_NOT_EXIST;
import static ru.eremin.ad.board.util.error.Errors.BAD_REQUEST;
import static ru.eremin.ad.board.util.error.Errors.CATEGORY_DOES_NOT_EXIST;
import static ru.eremin.ad.board.util.error.Errors.EMPTY_DURATION;

@Service
public class AdServiceImpl implements AdService {

    private final AdRepository repository;
    private final CategoryService categoryService;
    private final TransactionalOperator transactionalOperator;

    @Autowired
    public AdServiceImpl(final AdRepository repository, final CategoryService categoryService, final TransactionalOperator transactionalOperator) {
        this.repository = repository;
        this.categoryService = categoryService;
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public Flux<AdDto> findAll() {
        return repository.findAll()
            .flatMap(this::deactivateIfOverdue)
            .map(AdDto::new)
            .as(transactionalOperator::transactional)
            .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<AdDto> findAllActive(Integer limit) {
        return repository.findAllActive(limit)
            .filterWhen(ad -> deactivateIfOverdue(ad).map(Ad::isActive))
            .map(AdDto::new)
            .as(transactionalOperator::transactional)
            .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<AdDto> findAllActiveByCategory(final UUID categoryId) {
        return repository.findAllActiveByCategoryId(categoryId)
            .filterWhen(ad -> deactivateIfOverdue(ad).map(Ad::isActive))
            .map(AdDto::new)
            .as(transactionalOperator::transactional)
            .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<AdDto> findById(final UUID id) {
        return repository.findById(id)
            .flatMap(ad -> deactivateIfOverdue(ad).flatMap(
                it -> it.isActive() ? Mono.just(it) : Mono.empty()
            ))
            .map(AdDto::new)
            .as(transactionalOperator::transactional)
            .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UUID> create(final CreateAdRequest request) {
        return validateCreateRequest(request)
            .then(checkCategoryExist(request.getCategoryId()))
            .flatMap(categoryId -> {
                final LocalDateTime endDate = AdType.PAID.equals(request.getType())
                    ? LocalDateTime.now().plus(request.getDuration(), ChronoUnit.DAYS)
                    : null;
                return repository.save(
                    new Ad()
                        .setTheme(request.getTheme())
                        .setText(request.getText())
                        .setType(request.getType())
                        .setCategoryId(categoryId)
                        .setEndDate(endDate)
                );
            })
            .map(Ad::getId)
            .as(transactionalOperator::transactional)
            .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UUID> updateAd(final UpdateAdRequest request) {
        return Mono.just(request)
            .flatMap(req -> {
                if (req.getId() == null) return Mono.error(BAD_REQUEST.format("id").asException());
                return repository.findById(req.getId())
                    .switchIfEmpty(Mono.error(AD_DOES_NOT_EXIST.format(req.getId().toString()).asException()))
                    .flatMap(ad -> {
                        if (req.getNewCategoryId() != null) {
                            return checkCategoryExist(req.getNewCategoryId())
                                .map(ad::setCategoryId);
                        }
                        return Mono.just(ad);
                    })
                    .flatMap(ad -> {
                        ad
                            .setText(req.getNewText() != null ? req.getNewText() : ad.getText())
                            .setTheme(req.getNewTheme() != null ? req.getNewTheme() : ad.getTheme());
                        return repository.update(ad);
                    });
            })
            .map(Ad::getId)
            .as(transactionalOperator::transactional)
            .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UUID> upgradeAd(final UpgradeAdRequest request) {
        return Mono.just(request)
            .flatMap(req -> {
                if (req.getId() == null) return Mono.error(BAD_REQUEST.format("id").asException());
                if (req.getDuration() == null) return Mono.error(BAD_REQUEST.format("duration").asException());
                return repository.findById(req.getId())
                    .switchIfEmpty(Mono.error(AD_DOES_NOT_EXIST.format(req.getId().toString()).asException()))
                    .flatMap(ad -> {
                        ad
                            .setType(AdType.PAID)
                            .setEndDate(LocalDateTime.now().plus(req.getDuration(), ChronoUnit.DAYS));
                        return repository.update(ad);
                    });
            })
            .map(Ad::getId)
            .as(transactionalOperator::transactional)
            .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<Ad> deactivateIfOverdue(final Ad ad) {
        return Mono.just(ad)
            .flatMap(it -> {
                if (AdType.PAID.equals(ad.getType()) && ad.getEndDate().isBefore(LocalDateTime.now())) {
                    return repository.deactivate(it.getId())
                        .map(number -> it.setActive(false));
                }
                return Mono.just(it);
            });
    }

    private Mono<Void> validateCreateRequest(final CreateAdRequest request) {
        return Mono.just(request)
            .flatMap(req -> {
                if (req.getCategoryId() == null
                    || req.getText() == null
                    || req.getTheme() == null
                    || req.getType() == null
                ) {
                    return Mono.error(BAD_REQUEST.format("request").asException());
                }
                // If the ad is paid, it must be with duration.
                if (AdType.PAID.equals(req.getType()) && req.getDuration() == null) {
                    return Mono.error(EMPTY_DURATION.asException());
                }
                return Mono.empty();
            });
    }

    private Mono<UUID> checkCategoryExist(final UUID categoryId) {
        return categoryService.findById(categoryId)
            .switchIfEmpty(Mono.error(CATEGORY_DOES_NOT_EXIST.format(categoryId.toString()).asException()))
            .map(Category::getId);
    }
}
