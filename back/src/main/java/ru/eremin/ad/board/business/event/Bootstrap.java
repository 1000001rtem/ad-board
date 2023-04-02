package ru.eremin.ad.board.business.event;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.eremin.ad.board.output.storage.model.Ad;
import ru.eremin.ad.board.output.storage.model.Category;
import ru.eremin.ad.board.output.storage.repository.AdRepository;
import ru.eremin.ad.board.output.storage.repository.CategoryRepository;

import static ru.eremin.ad.board.output.storage.model.enumirate.AdType.FREE;
import static ru.eremin.ad.board.output.storage.model.enumirate.AdType.PAID;

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
        var electronicsCategory = UUID.randomUUID();
        var clothingCategory = UUID.randomUUID();
        var servicesCategory = UUID.randomUUID();
        List.of(
            new Category().setId(vehicleCategory).setCategoryName("Транспортные средства"),
            new Category().setId(foodCategory).setCategoryName("Продукты питания"),
            new Category().setId(electronicsCategory).setCategoryName("Электроника"),
            new Category().setId(clothingCategory).setCategoryName("Одежда и обувь"),
            new Category().setId(servicesCategory).setCategoryName("Услуги")
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
                .setEndDate(LocalDateTime.now().plus(42, ChronoUnit.DAYS))
                .setCreateUser("test@test.com"),
            new Ad()
                .setTheme("Продам картошку")
                .setText("Продаю картошку")
                .setCategoryId(foodCategory)
                .setType(FREE)
                .setCreateUser("test@test.com"),
            new Ad()
                .setTheme("Продам zuko")
                .setText("Продаю zuko")
                .setCategoryId(foodCategory)
                .setType(FREE)
                .setActive(false),
            new Ad()
                .setTheme("Продам велосипед")
                .setText("Велосипед в хорошем состоянии")
                .setCategoryId(vehicleCategory)
                .setType(FREE),
            new Ad()
                .setTheme("Продам яблоки")
                .setText("Свежие яблоки с собственного сада")
                .setCategoryId(foodCategory)
                .setType(FREE),
            new Ad()
                .setTheme("Требуются официанты")
                .setText("Требуются официанты в ресторан")
                .setCategoryId(servicesCategory)
                .setType(PAID)
                .setEndDate(LocalDateTime.now().plus(7, ChronoUnit.DAYS)),
            new Ad()
                .setTheme("Продам телевизор")
                .setText("Телевизор Samsung, 42 дюйма")
                .setCategoryId(electronicsCategory)
                .setType(FREE),
            new Ad()
                .setTheme("Скидки на одежду")
                .setText("Большие скидки на одежду в магазине")
                .setCategoryId(clothingCategory)
                .setType(PAID)
                .setEndDate(LocalDateTime.now().plus(14, ChronoUnit.DAYS))
        ).forEach(it -> {
            adRepository
                .save((Ad) it)
                .doOnSuccess(ad -> log.info(ad.getTheme() + " - " + ad.getId()))
                .block();
        });
    }
}
