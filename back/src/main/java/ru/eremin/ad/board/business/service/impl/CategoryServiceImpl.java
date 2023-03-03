package ru.eremin.ad.board.business.service.impl;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.eremin.ad.board.business.service.CategoryService;
import ru.eremin.ad.board.business.service.dto.CategoryDto;
import ru.eremin.ad.board.input.route.dto.CreateCategoryRequest;
import ru.eremin.ad.board.output.storage.model.Category;
import ru.eremin.ad.board.output.storage.repository.CategoryRepository;

import static ru.eremin.ad.board.util.error.Errors.BAD_REQUEST;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    @Autowired
    public CategoryServiceImpl(final CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<CategoryDto> findAll() {
        return repository.findAll()
            .map(CategoryDto::new)
            .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Category> findById(final UUID id) {
        if (id == null) return Mono.error(BAD_REQUEST.format("id").asException());
        return repository.findById(id)
            .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UUID> create(final CreateCategoryRequest request) {
        final String name = request.getCategoryName();
        if (name == null || name.isBlank()) {
            return Mono.error(BAD_REQUEST.format("name").asException());
        }
        return repository.save(new Category(name))
            .map(Category::getId)
            .subscribeOn(Schedulers.boundedElastic());
    }
}
