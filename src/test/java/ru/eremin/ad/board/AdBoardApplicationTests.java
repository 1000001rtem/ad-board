package ru.eremin.ad.board;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
class AdBoardApplicationTests {

    @Autowired
    private R2dbcEntityTemplate template;

    @AfterEach
    public void destroy() {
        template.getDatabaseClient().sql("DELETE FROM ads").fetch().rowsUpdated().block();
        template.getDatabaseClient().sql("DELETE FROM categories").fetch().rowsUpdated().block();
    }

    @Test
    void shouldInitDb() {
        final DatabaseClient client = template.getDatabaseClient();
        final String categoryId = UUID.randomUUID().toString();
        final String categoryQuery = "INSERT INTO categories (category_id, category_name, create_user, last_modified_user) " +
                "VALUES " +
                String.format("('%s','t','t','t')", categoryId);

        final Integer categoryUpdated = client.sql(categoryQuery)
                .fetch()
                .rowsUpdated()
                .block();
        final String categoryResult = client.sql("SELECT * FROM categories").map(r -> r.get("category_id").toString()).first().block();
        assertEquals(categoryResult, categoryId);
        assertEquals(1, categoryUpdated);

        final String adId = UUID.randomUUID().toString();
        final String adQuery = "INSERT INTO ads (ad_id, ad_theme, ad_text, ad_type, category_id, create_user, last_modified_user) " +
                "VALUES " +
                String.format("('%s','t','tm','t','%s','t','t')", adId, categoryId);

        final Integer adUpdated = client.sql(adQuery)
                .fetch()
                .rowsUpdated()
                .block();
        final String adResult = client.sql("SELECT * FROM ads").map(r -> r.get("ad_id").toString()).first().block();
        assertEquals(adResult, adId);
        assertEquals(1, adUpdated);
    }
}
