package ru.eremin.ad.board.controller.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UpgradeAdRequest {
    private UUID id;
    private Long duration;
}
