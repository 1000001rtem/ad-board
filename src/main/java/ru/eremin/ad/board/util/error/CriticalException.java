package ru.eremin.ad.board.util.error;

import lombok.Getter;

@Getter
public class CriticalException extends RuntimeException {

    private final String code;
    private final String message;

    public CriticalException(Throwable throwable) {
        this.code = "UNEXPECTED_ERROR";
        this.message = throwable.getMessage();
    }
}
