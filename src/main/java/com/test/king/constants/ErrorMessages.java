package com.test.king.constants;

public enum ErrorMessages {
    INVALID_URI("Invalid URI"),
    INVALID_ID("Invalid ID"),
    INVALID_SESSION_KEY("Invalid Session Key");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
