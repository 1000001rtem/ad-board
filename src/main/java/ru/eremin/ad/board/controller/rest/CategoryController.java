package ru.eremin.ad.board.controller.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.eremin.ad.board.business.service.CategoryService;
import ru.eremin.ad.board.business.service.dto.CategoryDto;
import ru.eremin.ad.board.controller.dto.CreateCategoryRequest;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(final CategoryService service) {
        this.service = service;
    }

    @GetMapping("all")
    public Flux<CategoryDto> findAll() {
        return service.findAll();
    }

    @PostMapping(value = "create", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<UUID> create(@RequestBody CreateCategoryRequest request) {
        return service.create(request.getCategoryName());
    }
}
