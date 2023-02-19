package ru.eremin.ad.board.route.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ErrorResponse {
    private Instant timestamp = Instant.now();
    /**
     * Error code
     */
    private String code;
    /**
     * Technical message
     */
    private String message;
    /**
     * Message for user
     */
    private String displayMessage;
}
