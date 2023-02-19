package ru.eremin.ad.board.storage.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.eremin.ad.board.storage.model.Ad;
import ru.eremin.ad.board.storage.repository.AdRepository;

import java.util.UUID;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

/**
 * Репозиторий для объявлений
 */
@Repository
public class ReactiveSqlAdRepository implements AdRepository {

    private final R2dbcEntityTemplate template;

    @Autowired
    public ReactiveSqlAdRepository(final R2dbcEntityTemplate template) {
        this.template = template;
    }

    /**
     * Поиск всех объявлений
     *
     * @return {@link Flux} список всех обхявлений
     */
    public Flux<Ad> findAll() {
        return template.select(Ad.class)
            .all();
    }

    @Override
    public Flux<Ad> findAllActive() {
        return template.select(Ad.class)
            .matching(query(where("active").isTrue()))
            .all();
    }

    @Override
    public Flux<Ad> findAllActiveByCategoryId(UUID categoryId) {
        return template.select(Ad.class)
            .matching(
                query(
                    where("categoryId").is(categoryId)
                        .and(where("active").isTrue())
                )
            )
            .all();
    }

    /**
     * Поиск объявления по id
     *
     * @param id индентификатор объявления
     * @return {@link Mono} объявление
     */
    @Override
    public Mono<Ad> findById(UUID id) {
        return template.selectOne(
            query(where("ad_id").is(id)),
            Ad.class
        );
    }

    @Override
    public Mono<Ad> save(Ad ad) {
        return template.insert(Ad.class)
            .using(ad);
    }

    /**
     * Обновление объявления
     *
     * @param ad объект объявления
     * @return {@link Mono} обновлённое объявление
     */
    @Override
    public Mono<Ad> update(Ad ad) {
        return template.update(ad);
    }

    @Override
    public Mono<Integer> deactivate(UUID id) {
        return template.update(Ad.class)
            .matching(query(where("ad_id").is(id)))
            .apply(Update.update("active", false))
            .handle((it, sink) -> {
                if (it != 1) {
                    sink.error(new RuntimeException());
                }
                sink.next(it);
            });
    }

    /**
     * Активация объявление
     *
     * @param id индентификатор объявления
     * @return {@link Mono} количество изменнёных записей или ошибку если кол-во отлично от 1.
     */
    @Override
    public Mono<Integer> activate(UUID id) {
        return template.update(Ad.class)
            .matching(query(where("ad_id").is(id)))
            .apply(Update.update("active", true))
            .handle((it, sink) -> {
                if (it != 1) {
                    sink.error(new RuntimeException());
                }
                sink.next(it);
            });
    }

}
