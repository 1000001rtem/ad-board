package ru.eremin.ad.board.storage.repository;

import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.eremin.ad.board.storage.model.Ad;

public interface AdRepository extends BaseRepository<Ad, UUID> {

    /**
     * Find all ad with active=true
     *
     * @param limit if null all ads will be return
     * @return {@link Flux} ads list never null
     */
    Flux<Ad> findAllActive(Integer limit);

    /**
     * Find all ad with active=true by category id
     *
     * @param categoryId UUID
     * @return {@link Flux} ads list never null
     */
    Flux<Ad> findAllActiveByCategoryId(UUID id);

    /**
     * Deactivate ad
     *
     * @param id UUID
     * @return {@link Mono} the number of records changed or an error if the number is different from 1.
     */
    Mono<Integer> deactivate(UUID id);

    /**
     * Активация объявление
     *
     * @param id UUID
     * @return {@link Mono} the number of records changed or an error if the number is different from 1.
     */
    Mono<Integer> activate(UUID id);
}
