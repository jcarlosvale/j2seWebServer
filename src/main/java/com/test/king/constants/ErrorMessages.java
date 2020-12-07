package com.test.king.constants;

public enum ErrorMessages {
    NULL_LOGIN("Login is NULL");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
