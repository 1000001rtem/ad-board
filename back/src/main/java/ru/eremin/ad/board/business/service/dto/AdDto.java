package ru.eremin.ad.board.business.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.lang.NonNull;
import ru.eremin.ad.board.storage.model.Ad;
import ru.eremin.ad.board.storage.model.enumirate.AdType;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AdDto {
    private UUID id;
    private String theme;
    private String text;
    private AdType type;
    private UUID categoryId;
    private boolean active;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public AdDto(@NonNull Ad ad) {
        this.id = ad.getId();
        this.theme = ad.getTheme();
        this.text = ad.getText();
        this.type = ad.getType();
        this.categoryId = ad.getCategoryId();
        this.active = ad.isActive();
        this.startDate = ad.getStartDate();
        this.endDate = ad.getEndDate();
    }
}
