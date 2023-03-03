package ru.eremin.ad.board.output.storage.repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repository with base operations
 *
 * @param <T>  Entity class
 * @param <ID> Class of entity id
 */
public interface BaseRepository<T, ID> {

    /**
     * Find all records
     *
     * @return {@link Flux} records list never null
     */
    Flux<T> findAll();

    /**
     * Find record by id
     *
     * @param id
     * @return {@link Mono} record or {@link reactor.core.publisher.MonoEmpty} if not found
     */
    Mono<T> findById(ID id);

    /**
     * Insert entity
     *
     * @param entity must not be null
     * @return {@link Mono} saved entity
     */
    Mono<T> save(T entity);

    /**
     * Update entity
     *
     * @param entity must not be null
     * @return {@link Mono} updated entity
     */
    Mono<T> update(T entity);
}
