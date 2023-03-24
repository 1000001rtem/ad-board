package ru.eremin.ad.board.input.route.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryRequest {
    @Nullable
    private String categoryName;
}
