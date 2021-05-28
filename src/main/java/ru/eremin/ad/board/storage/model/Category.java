package ru.eremin.ad.board.storage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("categories")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Category extends AbstractTable {

    @Id
    @Column("category_id")
    private UUID id = UUID.randomUUID();

    @Column("category_name")
    private String categoryName;

    public Category(final String categoryName) {
        this.categoryName = categoryName;
    }
}
