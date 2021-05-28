package ru.eremin.ad.board.controller.dto;

import lombok.Data;
import ru.eremin.ad.board.storage.model.enumirate.AdType;

import java.util.UUID;

@Data
public class CreateAdRequest {
    private String theme;
    private String text;
    private AdType type;
    private UUID categoryId;
    private Long duration = null;
}
