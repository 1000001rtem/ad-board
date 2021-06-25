package ru.eremin.ad.board.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.eremin.ad.board.business.service.dto.CategoryDto;
import ru.eremin.ad.board.storage.model.Category;
import ru.eremin.ad.board.storage.repository.CategoryRepository;

import java.util.UUID;

import static ru.eremin.ad.board.util.error.Errors.BAD_REQUEST;

/**
 * Сервис для работы с категориями объявлений.
 */
@Service
public class CategoryService {

    private final CategoryRepository repository;

    @Autowired
    public CategoryService(final CategoryRepository repository) {
        this.repository = repository;
    }

    /**
     * Найти все категории.
     *
     * @return {@link Flux} cписок всех категорий
     */
    public Flux<CategoryDto> findAll() {
        return repository.findAll()
            .map(CategoryDto::new)
            .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Найти категорию по идентификатору.
     *
     * @param id идентификатор категории
     * @return {@link Mono} категория
     */
    public Mono<Category> findById(final UUID id) {
        if (id == null) return Mono.error(BAD_REQUEST.format("id").asException());
        return repository.findById(id)
            .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Создание новой категории.
     * Если в запросе указано пустое имя категории, то будет возвращён {@link reactor.core.publisher.MonoError}.
     *
     * @param name Имя для новой категории
     * @return {@link Mono} Идентификатор новой категории
     */
    public Mono<UUID> create(final String name) {
        if (name == null
            || name.isBlank()
        ) {
            return Mono.error(new RuntimeException());
        }
        return repository.insert(new Category(name))
            .map(Category::getId)
            .subscribeOn(Schedulers.boundedElastic());
    }
}
