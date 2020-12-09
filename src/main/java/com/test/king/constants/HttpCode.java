package com.test.king.constants;

public enum HttpCode {
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    BAD_REQUEST(400, "Bad Request"),
    OK(200, "OK"),
    CREATED(201, "Created"),
    UNAUTHORIZED(401, "Unauthorized");

    private final int code;
    private final String message;

    HttpCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
