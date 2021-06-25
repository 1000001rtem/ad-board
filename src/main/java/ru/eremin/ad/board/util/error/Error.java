package ru.eremin.ad.board.util.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Абстракция ошибки
 */
public interface Error {
    String getCode();

    String getMessage();

    /**
     * Превращает ошибку в exception
     *
     * @return {@link AdBoardException}
     */
    default AdBoardException asException() {
        return new AdBoardException(this);
    }

    /**
     * Форматирует сообщение об ошибке
     */
    default Error format(String... values) {
        return new FormattedError(getCode(), String.format(getMessage(), values));
    }
}

@Getter
@AllArgsConstructor
class FormattedError implements Error {
    private final String code;
    private final String message;
}
