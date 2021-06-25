package ru.eremin.ad.board.controller.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.eremin.ad.board.business.service.AdService;
import ru.eremin.ad.board.business.service.dto.AdDto;
import ru.eremin.ad.board.controller.dto.CreateAdRequest;
import ru.eremin.ad.board.controller.dto.UpdateAdRequest;
import ru.eremin.ad.board.controller.dto.UpgradeAdRequest;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ad")
public class AdController {

    private final AdService service;

    public AdController(final AdService service) {
        this.service = service;
    }

    @GetMapping("find-by-category")
    public Flux<AdDto> findByCategory(@RequestParam("category-id") UUID categoryId) {
        return service.findAllActiveByCategory(categoryId);
    }

    @PostMapping("create")
    public Mono<UUID> create(@RequestBody CreateAdRequest request) {
        return service.create(request);
    }

    @PostMapping("update")
    public Mono<UUID> update(@RequestBody UpdateAdRequest request) {
        return service.updateAd(request);
    }

    @PostMapping("upgrade")
    public Mono<UUID> upgrade(@RequestBody UpgradeAdRequest request) {
        return service.upgradeAd(request);
    }
}
