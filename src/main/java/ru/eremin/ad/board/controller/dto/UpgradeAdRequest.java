package ru.eremin.ad.board.controller.dto;

import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.UUID;

@Data
public class UpgradeAdRequest {
    @Nullable
    private UUID id;
    @Nullable
    private Long duration;
}
