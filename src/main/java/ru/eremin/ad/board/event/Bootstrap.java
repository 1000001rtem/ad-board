package ru.eremin.ad.board.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.eremin.ad.board.storage.model.Ad;
import ru.eremin.ad.board.storage.model.Category;
import ru.eremin.ad.board.storage.repository.AdRepository;
import ru.eremin.ad.board.storage.repository.CategoryRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static ru.eremin.ad.board.storage.model.enumirate.AdType.FREE;
import static ru.eremin.ad.board.storage.model.enumirate.AdType.PAID;

@Log4j2
@Component
@Profile("local")
@RequiredArgsConstructor
public class Bootstrap {

    private final AdRepository adRepository;
    private final CategoryRepository categoryRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        var vehicleCategory = UUID.randomUUID();
        var foodCategory = UUID.randomUUID();
        List.of(
            new Category().setId(vehicleCategory).setCategoryName("Транспортные средства"),
            new Category().setId(foodCategory).setCategoryName("Продукты питания")
        ).forEach(it -> {
            categoryRepository
                .save(it)
                .doOnSuccess(category -> log.info(category.getCategoryName() + " - " + category.getId()))
                .subscribe();
        });

        List.of(
            new Ad()
                .setTheme("Продам мопед")
                .setText("Мопед не мой я просто разместил объяву")
                .setCategoryId(vehicleCategory)
                .setType(FREE),
            new Ad()
                .setTheme("Продам машины")
                .setText("У нас автосалон и много машин")
                .setCategoryId(vehicleCategory)
                .setType(PAID)
                .setEndDate(LocalDateTime.now().plus(42, ChronoUnit.DAYS)),
            new Ad()
                .setTheme("Продам картошку")
                .setText("Продаю картошку")
                .setCategoryId(foodCategory)
                .setType(FREE),
            new Ad()
                .setTheme("Продам zuko")
                .setText("Продаю zuko")
                .setCategoryId(foodCategory)
                .setType(FREE)
                .setActive(false)
        ).forEach(it -> {
            adRepository
                .save(it)
                .doOnSuccess(ad -> log.info(ad.getTheme() + " - " + ad.getId()))
                .block();
        });
    }
}
