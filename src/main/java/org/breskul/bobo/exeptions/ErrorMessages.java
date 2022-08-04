package org.breskul.bobo.exeptions;

public enum ErrorMessages {
    NOT_FOUND(" not found"),
    NOT_UNIQUE_NAME(" not unique");
    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
