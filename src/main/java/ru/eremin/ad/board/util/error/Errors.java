package ru.eremin.ad.board.util.error;

public enum Errors implements Error {
    BAD_REQUEST("Unacceptable value for %s"),
    EMPTY_DURATION("Required duration for ad with type PAID"),
    CATEGORY_DOES_NOT_EXIST("Category with id %s does not exist"),
    AD_DOES_NOT_EXIST("Ad with id %s does not exist"),
    ;

    private final String code;
    private final String message;

    Errors(final String message) {
        this.code = name();
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
