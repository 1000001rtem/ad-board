package ru.eremin.ad.board.util.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Error abstraction
 */
public interface Error {
    String getCode();

    String getMessage();

    String getDisplayMessage();

    /**
     * Convert Error to exception
     *
     * @return {@link AdBoardException}
     */
    default AdBoardException asException() {
        return new AdBoardException(this);
    }

    /**
     * Format error message
     */
    default Error format(String... values) {
        return new FormattedError(getCode(), String.format(getMessage(), values), getDisplayMessage());
    }
}

@Getter
@AllArgsConstructor
class FormattedError implements Error {
    private final String code;
    private final String message;
    private final String displayMessage;
}
