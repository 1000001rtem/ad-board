package ru.eremin.ad.board.storage.model;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

/**
 * Базовый набор полей для таблиц, автоматически заполняет необходимые служебные поля.
 */
class BaseRows {

    @CreatedBy
    private String createUser;

    @CreatedDate
    private Instant createTime;

    @LastModifiedBy
    private String lastModifiedUser;

    @LastModifiedDate
    private Instant lastModifiedTime;
}
