package ru.eremin.ad.board.business.service;

import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.eremin.ad.board.business.service.dto.AdDto;
import ru.eremin.ad.board.input.route.dto.CreateAdRequest;
import ru.eremin.ad.board.input.route.dto.UpdateAdRequest;
import ru.eremin.ad.board.input.route.dto.UpgradeAdRequest;

/**
 * Service for working with ads.
 */
public interface AdService {

    /**
     * Find all ads.
     * For paid ads, the expiration date is checked and then ad deactivated if expired.
     * Executed transactionally.
     *
     * @return {@link Flux} ads list (dto)
     */
    Flux<AdDto> findAll();

    /**
     * Find all active ads.
     * For paid ads, the expiration date is checked and then ad deactivated if expired.
     * Executed transactionally.
     *
     * @param limit if null all ads will be return
     * @return {@link Flux} ads list (dto)
     */
    Flux<AdDto> findAllActive(Integer limit);

    /**
     * Find all active ads in a given category.
     * For paid ads, the expiration date is checked and then ad deactivated if expired.
     * Executed transactionally.
     *
     * @param categoryId UUID
     * @return {@link Flux} ads list (dto)
     */
    Flux<AdDto> findAllActiveByCategory(final UUID categoryId);

    /**
     * Find one ad
     * If the ad is paid and expired, it will be deactivated
     * Executed transactionally.
     *
     * @param id UUID
     * @return {@link Mono} ad if exist and active (dto) or {@link reactor.core.publisher.MonoEmpty} if not found or not active
     */
    Mono<AdDto> findById(final UUID id);

    /**
     * Create a new ad.
     * If a non-existent group is specified in the request, it will return {@link reactor.core.publisher.MonoError}.
     * Executed transactionally.
     *
     * @param request request to create
     * @param userId  id of creator
     * @return {@link Mono} ads id
     */
    Mono<UUID> create(final CreateAdRequest request, final String userId);

    /**
     * Update ad.
     * If a non-existent group is specified in the request or the ad does not exist, it will be returned {@link reactor.core.publisher.MonoError}.
     * Executed transactionally.
     *
     * @param request request to update
     * @param userId  id of creator
     * @return {@link Mono} updated ads id
     */
    Mono<UUID> updateAd(final UpdateAdRequest request, final String userId);

    /**
     * Upgrading an ad to a paid one.
     * If the ad does not exist will be returned {@link reactor.core.publisher.MonoError}.
     * Executed transactionally.
     *
     * @param request request to upgrade
     * @param userId  id of creator
     * @return {@link Mono} upgraded ads id
     */
    Mono<UUID> upgradeAd(final UpgradeAdRequest request, final String userId);
}
