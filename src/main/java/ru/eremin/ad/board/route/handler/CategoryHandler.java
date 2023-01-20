package ru.eremin.ad.board.route.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.eremin.ad.board.business.service.CategoryService;
import ru.eremin.ad.board.business.service.dto.CategoryDto;
import ru.eremin.ad.board.route.dto.AdBoardResponseItem;
import ru.eremin.ad.board.route.dto.CreateCategoryRequest;
import ru.eremin.ad.board.util.transformer.ResponseTransformers;
import ru.eremin.ad.board.util.validation.ValidationService;

@Component
public class CategoryHandler {

    private final CategoryService service;
    private final ValidationService validationService;

    @Autowired
    public CategoryHandler(final CategoryService service, final ValidationService validationService) {
        this.service = service;
        this.validationService = validationService;
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {
        Flux<CategoryDto> resultFlux = service.findAll();
        return resultFlux
            .collectList()
            .flatMap(result ->
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(AdBoardResponseItem.success(result))
            )
            .transform(ResponseTransformers.errorResponseTransformer());
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(CreateCategoryRequest.class)
            .flatMap(validationService::validate)
            .flatMap(service::create)
            .flatMap(result ->
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(AdBoardResponseItem.success(result))
            )
            .transform(ResponseTransformers.errorResponseTransformer());
    }
}
