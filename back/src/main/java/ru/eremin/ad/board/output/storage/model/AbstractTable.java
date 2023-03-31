package ru.eremin.ad.board.output.storage.model;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Base database fields
 */
@Data
@Accessors(chain = true)
abstract public class AbstractTable {

    private String createUser;

    private LocalDateTime createTime;

    private String lastModifiedUser;

    private LocalDateTime lastModifiedTime;
}

