package ru.eremin.ad.board.storage.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.eremin.ad.board.storage.model.Ad;

import java.util.UUID;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

/**
 * Репозиторий для объявлений
 */
@Repository
public class AdRepository {

    private final R2dbcEntityTemplate template;

    @Autowired
    public AdRepository(final R2dbcEntityTemplate template) {
        this.template = template;
    }

    /**
     * Поиск всех объявлений
     *
     * @return Flux, список всех обхявлений
     */
    public Flux<Ad> findAll() {
        return template.select(Ad.class)
                .all();
    }

    /**
     * Поиск всех объявлений в категории
     *
     * @param categoryId идентификатор каотегории
     * @return Flux, список всех объявлений в категории
     */
    public Flux<Ad> findByCategoryId(UUID categoryId) {
        return template.select(Ad.class)
                .matching(query(where("categoryId").is(categoryId)))
                .all();
    }

    /**
     * Поиск объявления по id
     *
     * @param id индентификатор объявления
     */
    public Mono<Ad> findById(UUID id) {
        return template.selectOne(
                query(where("ad_id").is(id)),
                Ad.class
        );
    }

    /**
     * Сохранение объявления
     *
     * @param ad объект объявления
     * @return Mono, сохранённое объявление
     */
    public Mono<Ad> insert(Ad ad) {
        return template.insert(Ad.class)
                .using(ad);
    }

    /**
     * Обновление объявления
     *
     * @param ad объект объявления
     * @return Mono, обновлённое объявление
     */
    public Mono<Ad> update(Ad ad) {
        return template.update(ad);
    }

}
