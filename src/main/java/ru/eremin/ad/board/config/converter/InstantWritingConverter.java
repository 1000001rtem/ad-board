package ru.eremin.ad.board.config.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.time.Instant;

@WritingConverter
public class InstantWritingConverter implements Converter<Instant, Long> {

    @Override
    public Long convert(final Instant source) {
        return source.toEpochMilli();
    }
}
