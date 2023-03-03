package ru.eremin.ad.board.business.service;

import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.eremin.ad.board.business.service.dto.CategoryDto;
import ru.eremin.ad.board.input.route.dto.CreateCategoryRequest;
import ru.eremin.ad.board.output.storage.model.Category;

/**
 * Service for working with ad categories.
 */
public interface CategoryService {

    /**
     * Find all categories.
     *
     * @return {@link Flux} list of all categories (dto)
     */
    Flux<CategoryDto> findAll();

    /**
     * Find category by id.
     *
     * @param id UUID
     * @return {@link Mono} category (dto)
     */
    Mono<Category> findById(final UUID id);

    /**
     * Creating a new category.
     * If the request contains an empty category name, it will return {@link reactor.core.publisher.MonoError}.
     *
     * @param request Request to create a new category
     * @return {@link Mono} UUID
     */
    Mono<UUID> create(final CreateCategoryRequest request);
}
