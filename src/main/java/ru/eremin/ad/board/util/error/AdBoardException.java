package ru.eremin.ad.board.util.error;

import lombok.Getter;

@Getter
public class AdBoardException extends RuntimeException {

    private final String code;
    private final String message;
    private final String displayMessage;

    public AdBoardException(final Error error) {
        super(error.getMessage());
        this.code = error.getCode();
        this.message = error.getMessage();
        this.displayMessage = error.getDisplayMessage();
    }
}
