package ru.eremin.ad.board.route.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.eremin.ad.board.business.service.AdService;
import ru.eremin.ad.board.route.dto.CreateAdRequest;
import ru.eremin.ad.board.route.dto.UpdateAdRequest;
import ru.eremin.ad.board.route.dto.UpgradeAdRequest;
import ru.eremin.ad.board.util.error.Errors;
import ru.eremin.ad.board.util.transformer.ResponseTransformers;

import java.util.UUID;

@Component
public class AdHandler {

    private final AdService service;

    public AdHandler(final AdService service) {
        this.service = service;
    }

    public Mono<ServerResponse> findByCategory(ServerRequest request) {
        return Mono.just(request.queryParam("category-id")
            .map(UUID::fromString)
            .orElseThrow(() -> Errors.BAD_REQUEST.format("category-id").asException())
        )
            .flatMapMany(service::findAllActiveByCategory)
            .collectList()
            .flatMap(categoryId ->
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(categoryId)
            )
            .transform(ResponseTransformers.errorResponseTransformer());
    }

    public Mono<ServerResponse> create(final ServerRequest request) {
        return request.bodyToMono(CreateAdRequest.class)
            .flatMap(service::create)
            .flatMap(response ->
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(response)
            )
            .transform(ResponseTransformers.errorResponseTransformer());
    }

    public Mono<ServerResponse> update(final ServerRequest request) {
        return request.bodyToMono(UpdateAdRequest.class)
            .flatMap(service::updateAd)
            .flatMap(response ->
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(response)
            )
            .transform(ResponseTransformers.errorResponseTransformer());
    }

    public Mono<ServerResponse> upgrade(final ServerRequest request) {
        return request.bodyToMono(UpgradeAdRequest.class)
            .flatMap(service::upgradeAd)
            .flatMap(response ->
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(response)
            )
            .transform(ResponseTransformers.errorResponseTransformer());
    }
}
