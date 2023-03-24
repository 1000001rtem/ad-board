package ru.eremin.ad.board.util.error;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdBoardException extends RuntimeException {

    private String code;
    private String message;
    private String displayMessage;

    public AdBoardException(final Error error) {
        super(error.getMessage());
        this.code = error.getCode();
        this.message = error.getMessage();
        this.displayMessage = error.getDisplayMessage();
    }
}
