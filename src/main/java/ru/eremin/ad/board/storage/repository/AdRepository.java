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
import static org.springframework.data.relational.core.query.Update.update;

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
     * @return {@link Flux} список всех обхявлений
     */
    public Flux<Ad> findAll() {
        return template.select(Ad.class)
            .all();
    }

    /**
     * Поиск всех активных объявлений
     *
     * @return {@link Flux} список всех объявлений
     */
    public Flux<Ad> findAllActive() {
        return template.select(Ad.class)
            .matching(query(where("active").isTrue()))
            .all();
    }

    /**
     * Поиск всех объявлений в категории
     *
     * @param categoryId идентификатор каотегории
     * @return {@link Flux} список всех объявлений в категории
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
     * @return {@link Mono} объявление
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
     * @return {@link Mono} сохранённое объявление
     */
    public Mono<Ad> insert(Ad ad) {
        return template.insert(Ad.class)
            .using(ad);
    }

    /**
     * Обновление объявления
     *
     * @param ad объект объявления
     * @return {@link Mono} обновлённое объявление
     */
    public Mono<Ad> updateAd(Ad ad) {
        return template.update(ad);
    }

    /**
     * Деактивировать объявление
     *
     * @param id индентификатор объявления
     * @return {@link Mono} количество изменнёных записей или ошибку если кол-во отлично от 1.
     */
    public Mono<Integer> deactivate(UUID id) {
        return template.update(Ad.class)
            .matching(query(where("ad_id").is(id)))
            .apply(update("active", false))
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
    public Mono<Integer> activate(UUID id) {
        return template.update(Ad.class)
            .matching(query(where("ad_id").is(id)))
            .apply(update("active", true))
            .handle((it, sink) -> {
                if (it != 1) {
                    sink.error(new RuntimeException());
                }
                sink.next(it);
            });
    }

}
