package ru.eremin.ad.board.controller.dto;

import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.UUID;

@Data
public class UpdateAdRequest {
    @Nullable
    private UUID id;
    private String newTheme = null;
    private String newText = null;
    private UUID newCategoryId = null;
}
