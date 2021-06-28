package ru.eremin.ad.board.route.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAdRequest {
    @Nullable
    private UUID id;
    private String newTheme = null;
    private String newText = null;
    private UUID newCategoryId = null;
}
