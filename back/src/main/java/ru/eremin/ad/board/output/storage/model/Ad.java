package ru.eremin.ad.board.output.storage.model;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import ru.eremin.ad.board.output.storage.model.enumirate.AdType;

@Data
@Table("ads")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Ad extends AbstractTable {

    @Id
    @Column("ad_id")
    private UUID id = UUID.randomUUID();

    @Column("ad_theme")
    private String theme;

    @Column("ad_text")
    private String text;

    @Column("ad_type")
    private AdType type;

    @Column("category_id")
    private UUID categoryId;

    @Column("active")
    private boolean active = true;

    @Column("start_date")
    private LocalDateTime startDate = LocalDateTime.now();

    @Column("end_date")
    private LocalDateTime endDate;
}
