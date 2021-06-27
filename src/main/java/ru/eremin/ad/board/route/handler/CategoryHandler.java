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
import ru.eremin.ad.board.route.dto.CreateCategoryRequest;

import java.util.UUID;

@Component
public class CategoryHandler {

    private final CategoryService service;

    @Autowired
    public CategoryHandler(final CategoryService service) {
        this.service = service;
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {
        Flux<CategoryDto> result = service.findAll();
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(result, CategoryDto.class);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(CreateCategoryRequest.class)
            .map(service::create)
            .flatMap(response ->
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response, UUID.class));
    }
}
