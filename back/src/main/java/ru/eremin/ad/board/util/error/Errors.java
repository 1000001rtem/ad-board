package ru.eremin.ad.board.util.error;

public enum Errors implements Error {
    BAD_REQUEST("Unacceptable value for [%s]", "Something went wrong"),
    EMPTY_DURATION("Required duration for ad with type PAID", "Something went wrong"),
    CATEGORY_DOES_NOT_EXIST("Category with id %s does not exist", "Something went wrong"),
    AD_DOES_NOT_EXIST("Ad with id %s does not exist", "Something went wrong"),
    WRONG_USER("Create user does`n equal modify user", "Something went wrong");

    /**
     * Error code
     */
    private final String code;
    /**
     * Technical message
     */
    private final String message;
    /**
     * Message for user
     */
    private final String displayMessage;

    Errors(final String message, final String displayMessage) {
        this.code = name();
        this.message = message;
        this.displayMessage = displayMessage;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public String getDisplayMessage() {
        return this.displayMessage;
    }
}
