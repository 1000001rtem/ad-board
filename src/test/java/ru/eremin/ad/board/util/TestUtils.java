package ru.eremin.ad.board.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import ru.eremin.ad.board.storage.model.Ad;
import ru.eremin.ad.board.storage.model.enumirate.AdType;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Consumer;

public class TestUtils {

    /**
     * Консюмер для логирования запроса и ответа для {@link org.springframework.test.web.reactive.server.WebTestClient}
     */
    public static Consumer<EntityExchangeResult<byte[]>> logConsumer(ObjectMapper mapper, Logger log) {
        return exchangeResult -> {
            ObjectWriter prettyWriter = mapper.writerWithDefaultPrettyPrinter();
            try {
                byte[] request = exchangeResult.getRequestBodyContent();
                if (request != null) {
                    log.info("REQUEST:\n" + prettyWriter.writeValueAsString(mapper.readTree(request)));
                }
                log.info("RESPONSE:\n" + prettyWriter.writeValueAsString(mapper.readTree(exchangeResult.getResponseBody())));
            } catch (IOException e) {
                log.error(null, e);
            }
        };
    }

    public static Ad defaultAd() {
        return new Ad()
            .setTheme("test theme")
            .setText("test text")
            .setType(AdType.FREE)
            .setCategoryId(UUID.randomUUID())
            .setStartDate(LocalDateTime.now());
    }
}
