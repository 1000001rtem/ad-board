package ru.eremin.ad.board.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.test.StepVerifier;
import ru.eremin.ad.board.route.dto.CreateAdRequest;
import ru.eremin.ad.board.route.dto.UpdateAdRequest;
import ru.eremin.ad.board.route.dto.UpgradeAdRequest;
import ru.eremin.ad.board.storage.model.Ad;
import ru.eremin.ad.board.storage.model.Category;
import ru.eremin.ad.board.storage.model.enumirate.AdType;
import ru.eremin.ad.board.storage.repository.AdRepository;
import ru.eremin.ad.board.storage.repository.CategoryRepository;
import ru.eremin.ad.board.util.TestUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AdControllerTest {

    private static final Logger log = LoggerFactory.getLogger(CategoryControllerTest.class);

    @Autowired
    private WebTestClient client;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private R2dbcEntityTemplate template;

    @AfterEach
    void destroy() {
        template.getDatabaseClient().sql("DELETE FROM ads").fetch().rowsUpdated().block();
        template.getDatabaseClient().sql("DELETE FROM categories").fetch().rowsUpdated().block();
    }

    @Test
    void should_find_ad_by_category() {
        UUID categoryId = categoryRepository.save(new Category("test")).block().getId();

        List.of(
            TestUtils.defaultAd().setCategoryId(categoryId),
            TestUtils.defaultAd().setCategoryId(categoryId),
            TestUtils.defaultAd().setCategoryId(categoryId),
            TestUtils.defaultAd().setCategoryId(categoryId).setActive(false),
            TestUtils.defaultAd().setCategoryId(categoryId).setActive(false),
            TestUtils.defaultAd(),
            TestUtils.defaultAd(),
            TestUtils.defaultAd()
        ).forEach(ad -> adRepository.save(ad).block());

        client.get()
            .uri(uriBuilder ->
                uriBuilder
                    .path("/api/v1/ad/find-by-category")
                    .queryParam("category-id", categoryId)
                    .build()
            )
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .consumeWith(TestUtils.logConsumer(mapper, log))
            .jsonPath("$.data.length()").isEqualTo(3);
    }

    @Test
    void should_filter_and_deactivate_overdue_ads() {
        UUID categoryId = categoryRepository.save(new Category("test")).block().getId();

        List.of(
            TestUtils.defaultAd().setCategoryId(categoryId),
            TestUtils.defaultAd().setCategoryId(categoryId),
            TestUtils.defaultAd().setCategoryId(categoryId),
            TestUtils.defaultAd().setCategoryId(categoryId).setType(AdType.PAID).setEndDate(LocalDateTime.now().plus(42, ChronoUnit.DAYS)),
            TestUtils.defaultAd().setCategoryId(categoryId).setType(AdType.PAID).setEndDate(LocalDateTime.now().plus(1, ChronoUnit.MINUTES)),
            TestUtils.defaultAd().setCategoryId(categoryId).setText("noop").setType(AdType.PAID).setEndDate(LocalDateTime.now().minus(1, ChronoUnit.DAYS)), // должен отфильтроваться
            TestUtils.defaultAd().setCategoryId(categoryId).setText("noop").setType(AdType.PAID).setEndDate(LocalDateTime.now().minus(1, ChronoUnit.MINUTES)), // должен отфильтроваться
            TestUtils.defaultAd(),
            TestUtils.defaultAd(),
            TestUtils.defaultAd()
        ).forEach(ad -> adRepository.save(ad).block());

        client.get()
            .uri(uriBuilder ->
                uriBuilder
                    .path("/api/v1/ad/find-by-category")
                    .queryParam("category-id", categoryId)
                    .build()
            )
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .consumeWith(TestUtils.logConsumer(mapper, log))
            .jsonPath("$.data.length()").isEqualTo(5);

        List<Ad> result = adRepository.findAll().collectList().block()
            .stream()
            .filter(it -> !it.isActive())
            .collect(Collectors.toList());

        assertEquals(2, result.size());
        assertTrue(result.stream().map(Ad::getText).collect(Collectors.toList()).containsAll(List.of("noop", "noop")));
    }

    @Test
    void should_find_by_id() {
        var id = UUID.randomUUID();

        adRepository.save(TestUtils.defaultAd().setId(id)).block();

        client.get()
            .uri(uriBuilder ->
                uriBuilder
                    .path("/api/v1/ad/find-by-id")
                    .queryParam("id", id.toString())
                    .build()
            )
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .consumeWith(TestUtils.logConsumer(mapper, log))
            .jsonPath("$.data.id").isEqualTo(id.toString());
    }

    @Test
    void should_return_null_in_formal_response_if_ad_not_found() {
        client.get()
            .uri(uriBuilder ->
                uriBuilder
                    .path("/api/v1/ad/find-by-id")
                    .queryParam("id", UUID.randomUUID())
                    .build()
            )
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .consumeWith(TestUtils.logConsumer(mapper, log))
            .jsonPath("$.status").isEqualTo("SUCCESS")
            .jsonPath("$.data").isEmpty();
    }

    @Test
    void should_create_ad() throws JsonProcessingException {
        UUID categoryId = categoryRepository.save(new Category("test")).block().getId();

        client.post()
            .uri("/api/v1/ad/create")
            .contentType(APPLICATION_JSON)
            .body(BodyInserters.fromValue(
                mapper.writeValueAsString(new CreateAdRequest(
                    "test",
                    "test",
                    AdType.FREE,
                    categoryId,
                    null
                )))
            )
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .consumeWith(TestUtils.logConsumer(mapper, log))
            .jsonPath("$.data").value((String value) ->
                StepVerifier.create(adRepository.findAll())
                    .expectNextMatches(next -> {
                        assertEquals(UUID.fromString(value), next.getId());
                        return true;
                    })
                    .verifyComplete()
            );
    }

    @Test
    void should_update_ad() throws JsonProcessingException {
        Ad ad = adRepository.save(TestUtils.defaultAd()).block();

        client.put()
            .uri("/api/v1/ad/update")
            .contentType(APPLICATION_JSON)
            .body(BodyInserters.fromValue(
                mapper.writeValueAsString(new UpdateAdRequest(ad.getId(), null, "new text", null)))
            )
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .consumeWith(TestUtils.logConsumer(mapper, log))
            .jsonPath("$.data").value((String value) ->
                StepVerifier.create(adRepository.findById(UUID.fromString(value)))
                    .expectNextMatches(next -> {
                        assertEquals(UUID.fromString(value), next.getId());
                        assertEquals("new text", next.getText());
                        return true;
                    })
                    .verifyComplete()
            );
    }

    @Test
    void should_upgrade_ad() throws JsonProcessingException {
        Ad ad = adRepository.save(TestUtils.defaultAd()).block();

        client.put()
            .uri("/api/v1/ad/upgrade")
            .contentType(APPLICATION_JSON)
            .body(BodyInserters.fromValue(
                mapper.writeValueAsString(new UpgradeAdRequest(ad.getId(), 5L)))
            )
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .consumeWith(TestUtils.logConsumer(mapper, log))
            .jsonPath("$.data").value((String value) ->
                StepVerifier.create(adRepository.findById(UUID.fromString(value)))
                    .expectNextMatches(next -> {
                        assertEquals(UUID.fromString(value), next.getId());
                        assertEquals(AdType.PAID, next.getType());
                        assertEquals(
                            LocalDateTime.now().truncatedTo(ChronoUnit.DAYS),
                            next.getEndDate().minus(5, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS)
                        );
                        return true;
                    })
                    .verifyComplete()
            );
    }

    @Test
    public void should_return_error_if_category_does_not_exist() throws JsonProcessingException {
        UUID categoryId = UUID.randomUUID();
        client.post()
            .uri("/api/v1/ad/create")
            .contentType(APPLICATION_JSON)
            .body(BodyInserters.fromValue(
                mapper.writeValueAsString(new CreateAdRequest(
                    "test",
                    "test",
                    AdType.FREE,
                    categoryId,
                    null
                )))
            )
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .consumeWith(TestUtils.logConsumer(mapper, log))
            .jsonPath("$.error.code").isEqualTo("CATEGORY_DOES_NOT_EXIST")
            .jsonPath("$.error.message").isEqualTo("Category with id " + categoryId + " does not exist")
            .jsonPath("$.error.displayMessage").isEqualTo("Something went wrong");
    }
}
