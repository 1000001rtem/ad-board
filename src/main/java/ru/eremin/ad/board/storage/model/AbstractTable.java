package ru.eremin.ad.board.storage.model;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * Базовый набор полей для таблиц.
 */
@Data
abstract public class AbstractTable {

    private String createUser;

    private LocalDateTime createTime;

    private String lastModifiedUser;

    private LocalDateTime lastModifiedTime;
}

