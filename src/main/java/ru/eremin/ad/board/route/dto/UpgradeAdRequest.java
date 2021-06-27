package ru.eremin.ad.board.route.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpgradeAdRequest {
    @Nullable
    private UUID id;
    @Nullable
    private Long duration;
}
