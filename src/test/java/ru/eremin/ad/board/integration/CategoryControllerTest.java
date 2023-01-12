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
import ru.eremin.ad.board.route.dto.CreateCategoryRequest;
import ru.eremin.ad.board.storage.model.Category;
import ru.eremin.ad.board.storage.repository.CategoryRepository;
import ru.eremin.ad.board.util.TestUtils;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryControllerTest {

    private static final Logger log = LoggerFactory.getLogger(CategoryControllerTest.class);

    @Autowired
    private WebTestClient client;

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private R2dbcEntityTemplate template;

    @AfterEach
    void clear() {
        template.getDatabaseClient().sql("DELETE FROM categories").fetch().rowsUpdated().block();
    }

    @Test
    void should_find_all_category() {
        List.of(
            new Category("c1"),
            new Category("c2"),
            new Category("c3")
        ).forEach(category -> repository.insert(category).block());

        client.get()
            .uri("/api/v1/category/all")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .consumeWith(TestUtils.logConsumer(mapper, log))
            .jsonPath("$.data.length()").isEqualTo("3");
    }

    @Test
    void should_create_category() throws JsonProcessingException {
        client.post()
            .uri("/api/v1/category/create")
            .contentType(APPLICATION_JSON)
            .body(BodyInserters.fromValue(mapper.writeValueAsString(new CreateCategoryRequest("new category"))))
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .consumeWith(TestUtils.logConsumer(mapper, log))
            .jsonPath("$.data").value((String value) -> {
                StepVerifier.create(repository.findAll())
                    .expectNextMatches(next -> {
                        assertEquals(UUID.fromString(value), next.getId());
                        return true;
                    })
                    .verifyComplete();
            }
        );
    }
}
