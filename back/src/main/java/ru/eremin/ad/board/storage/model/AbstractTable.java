package ru.eremin.ad.board.storage.model;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * Base database fields
 */
@Data
abstract public class AbstractTable {

    private String createUser;

    private LocalDateTime createTime;

    private String lastModifiedUser;

    private LocalDateTime lastModifiedTime;
}

