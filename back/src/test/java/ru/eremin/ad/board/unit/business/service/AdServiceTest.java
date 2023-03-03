package ru.eremin.ad.board.unit.business.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.eremin.ad.board.business.service.AdService;
import ru.eremin.ad.board.business.service.CategoryService;
import ru.eremin.ad.board.business.service.dto.AdDto;
import ru.eremin.ad.board.business.service.impl.AdServiceImpl;
import ru.eremin.ad.board.business.service.impl.CategoryServiceImpl;
import ru.eremin.ad.board.input.route.dto.CreateAdRequest;
import ru.eremin.ad.board.input.route.dto.UpdateAdRequest;
import ru.eremin.ad.board.input.route.dto.UpgradeAdRequest;
import ru.eremin.ad.board.output.storage.model.Ad;
import ru.eremin.ad.board.output.storage.model.enumirate.AdType;
import ru.eremin.ad.board.output.storage.repository.AdRepository;
import ru.eremin.ad.board.util.error.AdBoardException;
import ru.eremin.ad.board.util.error.Errors;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.eremin.ad.board.util.TestUtils.defaultAd;

public class AdServiceTest {
    private AdRepository adRepository;
    private CategoryService categoryService;

    private AdService subj;

    @BeforeEach
    public void init() {
        adRepository = mock(AdRepository.class);
        categoryService = mock(CategoryServiceImpl.class);
        TransactionalOperator transactionalOperator = mock(TransactionalOperator.class);

        subj = new AdServiceImpl(adRepository, categoryService, transactionalOperator);

        when(transactionalOperator.transactional(any(Flux.class))).thenAnswer(it ->
            it.getArguments()[0]
        );
        when(transactionalOperator.transactional(any(Mono.class))).thenAnswer(it ->
            it.getArguments()[0]
        );
    }

    //find

    @Test
    public void should_return_all_ads() {
        when(adRepository.findAll()).thenReturn(Flux.fromIterable(List.of(defaultAd(), defaultAd())));
        subj.findAll()
            .as(StepVerifier::create)
            .expectNextCount(2)
            .verifyComplete();
    }

    @Test
    public void should_deactivate_overdue_ads() {
        List<Ad> ads = List.of(
            defaultAd(),
            defaultAd(),
            defaultAd().setType(AdType.PAID).setEndDate(LocalDateTime.now().plus(42, ChronoUnit.DAYS)),
            defaultAd().setType(AdType.PAID).setEndDate(LocalDateTime.now().plus(1, ChronoUnit.MINUTES)),
            defaultAd().setText("noop").setType(AdType.PAID).setEndDate(LocalDateTime.now().minus(1, ChronoUnit.DAYS)), // должен деактивировать
            defaultAd().setText("noop").setType(AdType.PAID).setEndDate(LocalDateTime.now().minus(1, ChronoUnit.MINUTES)) // должен деактивировать
        );

        when(adRepository.findAll()).thenReturn(Flux.fromIterable(ads));
        when(adRepository.deactivate(any())).thenReturn(Mono.just(1));

        List<AdDto> result = subj.findAll().collectList().block();

        assertEquals(6, result.size());
        assertEquals(2, result.stream().filter(it -> !it.isActive()).count());
        verify(adRepository, times(2)).deactivate(any());
    }

    @Test
    public void should_filter_and_deactivate_overdue_ads() {
        List<Ad> ads = List.of(
            defaultAd(),
            defaultAd(),
            defaultAd().setType(AdType.PAID).setEndDate(LocalDateTime.now().plus(42, ChronoUnit.DAYS)),
            defaultAd().setType(AdType.PAID).setEndDate(LocalDateTime.now().plus(1, ChronoUnit.MINUTES)),
            defaultAd().setText("noop").setType(AdType.PAID).setEndDate(LocalDateTime.now().minus(1, ChronoUnit.DAYS)), // должен отфильтроваться
            defaultAd().setText("noop").setType(AdType.PAID).setEndDate(LocalDateTime.now().minus(1, ChronoUnit.MINUTES)) // должен отфильтроваться
        );

        when(adRepository.findAllActive(-1)).thenReturn(Flux.fromIterable(ads));
        when(adRepository.findAllActiveByCategoryId(any())).thenReturn(Flux.fromIterable(ads));
        when(adRepository.deactivate(any())).thenReturn(Mono.just(1));
        when(adRepository.findById(any())).thenReturn(Mono.just(ads.get(4)));

        List<AdDto> result = subj.findAllActive(-1).collectList().block();

        assertEquals(4, result.size());
        assertEquals(0, result.stream().filter(it -> !it.isActive()).count());

        List<AdDto> result2 = subj.findAllActiveByCategory(ads.get(0).getCategoryId()).collectList().block();

        assertEquals(4, result2.size());
        assertEquals(0, result2.stream().filter(it -> !it.isActive()).count());

        AdDto result3 = subj.findById(ads.get(4).getId()).block();

        assertNull(result3);

        verify(adRepository, times(5)).deactivate(any());
    }

    //create

    @Test
    public void should_return_error_when_create_ad_with_non_existent_category() {
        when(categoryService.findById(any())).thenReturn(Mono.empty());

        UUID categoryId = UUID.randomUUID();
        CreateAdRequest request = new CreateAdRequest("", "", AdType.FREE, categoryId, null);

        subj.create(request)
            .as(StepVerifier::create)
            .expectErrorMatches(error -> {
                assertTrue(error instanceof AdBoardException);
                assertEquals(Errors.CATEGORY_DOES_NOT_EXIST.name(), ((AdBoardException) error).getCode());
                assertTrue(error.getMessage().contains(categoryId.toString()));
                return true;
            })
            .verify();

        verify(adRepository, never()).save(any());
    }

    @Test
    public void should_return_error_when_paid_ad_without_duration() {
        when(categoryService.findById(any())).thenReturn(Mono.empty());
        CreateAdRequest request = new CreateAdRequest("", "", AdType.PAID, UUID.randomUUID(), null);

        subj.create(request)
            .as(StepVerifier::create)
            .expectErrorMatches(error -> {
                assertTrue(error instanceof AdBoardException);
                assertEquals(Errors.EMPTY_DURATION.name(), ((AdBoardException) error).getCode());
                return true;
            })
            .verify();

        verify(adRepository, never()).save(any());
    }

    //update

    @Test
    public void should_return_error_when_updated_ad_not_exist() {
        when(adRepository.findById(any())).thenReturn(Mono.empty());

        UUID id = UUID.randomUUID();
        UpdateAdRequest request = new UpdateAdRequest(id, "", "", null);

        subj.updateAd(request)
            .as(StepVerifier::create)
            .expectErrorMatches(error -> {
                assertTrue(error instanceof AdBoardException);
                assertEquals(Errors.AD_DOES_NOT_EXIST.name(), ((AdBoardException) error).getCode());
                assertTrue(error.getMessage().contains(id.toString()));
                return true;
            })
            .verify();

        verify(adRepository, never()).update(any());
    }

    @Test
    public void should_return_error_when_update_ad_with_non_existent_category() {
        UUID categoryId = UUID.randomUUID();
        when(adRepository.findById(any())).thenReturn(Mono.just(defaultAd()));
        when(categoryService.findById(eq(categoryId))).thenReturn(Mono.empty());

        UpdateAdRequest request = new UpdateAdRequest(UUID.randomUUID(), "", "", categoryId);

        subj.updateAd(request)
            .as(StepVerifier::create)
            .expectErrorMatches(error -> {
                assertTrue(error instanceof AdBoardException);
                assertEquals(Errors.CATEGORY_DOES_NOT_EXIST.name(), ((AdBoardException) error).getCode());
                assertTrue(error.getMessage().contains(categoryId.toString()));
                return true;
            })
            .verify();

        verify(adRepository, never()).update(any());
    }

    @Test
    public void should_update_ad_if_category_id_in_request_is_null() {
        Ad ad = defaultAd();
        ArgumentCaptor<Ad> captor = ArgumentCaptor.forClass(Ad.class);
        when(adRepository.findById(any())).thenReturn(Mono.just(ad));
        when(adRepository.update(captor.capture())).thenReturn(Mono.just(ad));

        UpdateAdRequest request = new UpdateAdRequest(UUID.randomUUID(), "new", "new", null);

        subj.updateAd(request)
            .as(StepVerifier::create)
            .expectNext(ad.getId())
            .verifyComplete();

        assertEquals(ad.getId(), captor.getValue().getId());
        assertEquals("new", captor.getValue().getText());
        assertEquals("new", captor.getValue().getTheme());
        assertEquals(ad.getCategoryId(), captor.getValue().getCategoryId());
    }

    //upgrade

    @Test
    public void should_return_error_when_upgraded_ad_not_exist() {
        when(adRepository.findById(any())).thenReturn(Mono.empty());

        UUID id = UUID.randomUUID();
        UpgradeAdRequest request = new UpgradeAdRequest(id, 5L);

        subj.upgradeAd(request)
            .as(StepVerifier::create)
            .expectErrorMatches(error -> {
                assertTrue(error instanceof AdBoardException);
                assertEquals(Errors.AD_DOES_NOT_EXIST.name(), ((AdBoardException) error).getCode());
                assertTrue(error.getMessage().contains(id.toString()));
                return true;
            })
            .verify();

        verify(adRepository, never()).update(any());
    }

    @Test
    public void should_upgrade_ad() {
        Ad ad = defaultAd();
        ArgumentCaptor<Ad> captor = ArgumentCaptor.forClass(Ad.class);
        when(adRepository.findById(eq(ad.getId()))).thenReturn(Mono.just(ad));
        when(adRepository.update(captor.capture())).thenReturn(Mono.just(ad));

        UpgradeAdRequest request = new UpgradeAdRequest(ad.getId(), 5L);
        subj.upgradeAd(request)
            .as(StepVerifier::create)
            .expectNext(ad.getId())
            .verifyComplete();

        assertEquals(ad.getId(), captor.getValue().getId());
        assertEquals(ad.getType(), AdType.PAID);
        assertEquals(
            LocalDateTime.now().truncatedTo(ChronoUnit.DAYS),
            captor.getValue().getEndDate().minus(5, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS)
        );
    }
}
