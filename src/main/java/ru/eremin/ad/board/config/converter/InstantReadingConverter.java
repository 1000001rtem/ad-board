package ru.eremin.ad.board.config.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.time.Instant;

@ReadingConverter
public class InstantReadingConverter implements Converter<Long, Instant>{

    @Override
    public Instant convert(final Long source) {
        return Instant.ofEpochMilli(source);
    }
}
