package ru.eremin.ad.board.controller.dto;

import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class CreateCategoryRequest {
    @Nullable
    private String categoryName;
}
