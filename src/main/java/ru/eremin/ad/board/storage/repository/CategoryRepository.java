package ru.eremin.ad.board.storage.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.eremin.ad.board.storage.model.Category;

import java.util.UUID;

import static org.springframework.data.relational.core.query.Criteria.where;

/**
 * Репозиторий для категорий
 */
@Repository
public class CategoryRepository {

    private final R2dbcEntityTemplate template;

    @Autowired
    public CategoryRepository(final R2dbcEntityTemplate template) {
        this.template = template;
    }

    /**
     * Поиск всех категорий.
     *
     * @return Flux, список всех категорий
     */
    public Flux<Category> findAll() {
        return template.select(Category.class)
                .all();
    }

    /**
     * Поиск категории по id
     *
     * @param id индентификатор категории
     * @return Mono, найденный объект или Mono.empty, если не найден
     */
    public Mono<Category> findById(UUID id) {
        return template.selectOne(
                Query.query(where("category_id").is(id)),
                Category.class
        );
    }

    /**
     * Сохранение категории
     *
     * @param category объект категории
     * @return Mono, сохранённая категория
     */
    public Mono<Category> insert(Category category) {
        return template.insert(Category.class)
                .using(category);
    }

    /**
     * Обновление категории
     *
     * @param category объект категории
     * @return Mono, обновлённая категория
     */
    public Mono<Category> update(Category category) {
        return template.update(category);
    }
}
