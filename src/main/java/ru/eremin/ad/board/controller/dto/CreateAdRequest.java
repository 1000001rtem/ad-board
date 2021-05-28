package ru.eremin.ad.board.controller.dto;

import lombok.Data;
import org.springframework.lang.Nullable;
import ru.eremin.ad.board.storage.model.enumirate.AdType;

import java.util.UUID;

@Data
public class CreateAdRequest {
    @Nullable
    private String theme;
    @Nullable
    private String text;
    @Nullable
    private AdType type;
    @Nullable
    private UUID categoryId;
    private Long duration = null;
}
