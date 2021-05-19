package ru.eremin.ad.board;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class AdBoardApplicationTests {

    @Autowired
    private R2dbcEntityTemplate template;

    @Test
    void shouldInitDb() {
        final DatabaseClient client = template.getDatabaseClient();
        final String categoryId = UUID.randomUUID().toString();
        final String categoryQuery = "INSERT INTO category_table (category_id, category_name, create_user, last_modify_user) " +
                "VALUES " +
                String.format("('%s','t','t','t')", categoryId);

        final Integer categoryUpdated = client.sql(categoryQuery)
                .fetch()
                .rowsUpdated()
                .block();
        final String categoryResult = client.sql("SELECT * FROM category_table").map(r -> r.get("category_id").toString()).first().block();
        assertEquals(categoryResult, categoryId);
        assertEquals(1, categoryUpdated);

        final String adId = UUID.randomUUID().toString();
        final String adQuery = "INSERT INTO ad_table (ad_id, ad_theme, ad_text, ad_type, category_id, create_user, last_modify_user) " +
                "VALUES " +
                String.format("('%s','t','tm','t','%s','t','t')", adId, categoryId);

        final Integer adUpdated = client.sql(adQuery)
                .fetch()
                .rowsUpdated()
                .block();
        final String adResult = client.sql("SELECT * FROM ad_table").map(r -> r.get("ad_id").toString()).first().block();
        assertEquals(adResult, adId);
        assertEquals(1, adUpdated);
    }
}
