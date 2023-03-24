package ru.eremin.ad.board.output.storage.repository.impl;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.eremin.ad.board.output.storage.repository.CategoryRepository;
import ru.eremin.ad.board.output.storage.model.Category;

import static org.springframework.data.relational.core.query.Criteria.where;

@Repository
public class ReactiveSqlCategoryRepository implements CategoryRepository {

    private final R2dbcEntityTemplate template;

    @Autowired
    public ReactiveSqlCategoryRepository(final R2dbcEntityTemplate template) {
        this.template = template;
    }

    @Override
    public Flux<Category> findAll() {
        return template.select(Category.class)
            .all();
    }

    @Override
    public Mono<Category> findById(UUID id) {
        return template.selectOne(
            Query.query(where("category_id").is(id)),
            Category.class
        );
    }

    @Override
    public Mono<Category> save(Category category) {
        return template.insert(Category.class)
            .using(category);
    }

    @Override
    public Mono<Category> update(Category category) {
        return template.update(category);
    }
}
