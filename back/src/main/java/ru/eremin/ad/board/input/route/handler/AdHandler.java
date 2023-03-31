package ru.eremin.ad.board.input.route.handler;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.eremin.ad.board.business.service.AdService;
import ru.eremin.ad.board.input.route.dto.AdBoardResponseItem;
import ru.eremin.ad.board.input.route.dto.CreateAdRequest;
import ru.eremin.ad.board.input.route.dto.UpdateAdRequest;
import ru.eremin.ad.board.input.route.dto.UpgradeAdRequest;
import ru.eremin.ad.board.util.error.Errors;
import ru.eremin.ad.board.util.transformer.ResponseTransformers;
import ru.eremin.ad.board.util.validation.ValidationService;

import static ru.eremin.ad.board.input.route.RouteConfiguration.EMAIL_HEADER;

@Component
@RequiredArgsConstructor
public class AdHandler {

    private final AdService service;
    private final ValidationService validationService;

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
                    .bodyValue(AdBoardResponseItem.success(categoryId))
            )
            .transform(ResponseTransformers.errorResponseTransformer());
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        return Mono.just(request.queryParam("id")
                .map(UUID::fromString)
                .orElseThrow(() -> Errors.BAD_REQUEST.format("id").asException())
            )
            .flatMap(service::findById)
            .flatMap(result ->
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(AdBoardResponseItem.success(result))
            )
            .switchIfEmpty(
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(AdBoardResponseItem.empty())
            )
            .transform(ResponseTransformers.errorResponseTransformer());
    }

    public Mono<ServerResponse> create(final ServerRequest request) {
        var email = request.headers().firstHeader(EMAIL_HEADER);
        return request.bodyToMono(CreateAdRequest.class)
            .flatMap(validationService::validate)
            .flatMap(it -> service.create(it, email))
            .flatMap(result ->
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(AdBoardResponseItem.success(result))
            )
            .transform(ResponseTransformers.errorResponseTransformer());
    }

    public Mono<ServerResponse> update(final ServerRequest request) {
        var email = request.headers().firstHeader(EMAIL_HEADER);
        return request.bodyToMono(UpdateAdRequest.class)
            .flatMap(validationService::validate)
            .flatMap(it -> service.updateAd(it, email))
            .flatMap(result ->
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(AdBoardResponseItem.success(result))
            )
            .transform(ResponseTransformers.errorResponseTransformer());
    }

    public Mono<ServerResponse> upgrade(final ServerRequest request) {
        var email = request.headers().firstHeader(EMAIL_HEADER);
        return request.bodyToMono(UpgradeAdRequest.class)
            .flatMap(validationService::validate)
            .flatMap(it -> service.upgradeAd(it, email))
            .flatMap(result ->
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(AdBoardResponseItem.success(result))
            )
            .transform(ResponseTransformers.errorResponseTransformer());
    }

    public Mono<ServerResponse> findAllActive(final ServerRequest request) {
        var limit = request.queryParam("limit")
            .map(Integer::valueOf)
            .orElse(null);
        return service.findAllActive(limit)
            .collectList()
            .flatMap(result ->
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(AdBoardResponseItem.success(result))
            )
            .transform(ResponseTransformers.errorResponseTransformer());

    }
}
