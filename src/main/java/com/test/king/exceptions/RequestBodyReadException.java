package com.test.king.exceptions;

/**
 * Used when an expected Request Body does not have information OR incorrect value (ex: signed int).
 */
public class RequestBodyReadException extends Exception {
    public RequestBodyReadException(String message) {
        super(message);
    }
}
