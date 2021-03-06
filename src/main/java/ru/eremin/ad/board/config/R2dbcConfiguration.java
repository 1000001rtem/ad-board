package ru.eremin.ad.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import ru.eremin.ad.board.config.converter.InstantReadingConverter;
import ru.eremin.ad.board.config.converter.InstantWritingConverter;

import java.util.List;

@Configuration
@EnableR2dbcAuditing
public class R2dbcConfiguration {

    /**
     * Регистрация конвертеров для Instant, для сохранения в базу
     */
    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions() {
        return new R2dbcCustomConversions(
            List.of(
                new InstantReadingConverter(),
                new InstantWritingConverter()
            )
        );
    }
}
