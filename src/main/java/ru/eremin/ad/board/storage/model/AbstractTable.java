package ru.eremin.ad.board.storage.model;

import lombok.Data;

import java.time.Instant;

/**
 * Базовый набор полей для таблиц.
 */
@Data
abstract public class AbstractTable {

    private String createUser;

    private Instant createTime;

    private String lastModifiedUser;

    private Instant lastModifiedTime;
}
