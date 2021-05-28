package ru.eremin.ad.board.controller.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateAdRequest {
    private UUID id;
    private String newTheme = null;
    private String newText = null;
    private UUID newCategoryId = null;
}
