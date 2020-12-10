package com.test.king.exceptions;

/**
 * Used when it is not possible to read the session key in the URL.
 */
public class RequestSessionKeyReadException extends Exception {
    public RequestSessionKeyReadException(String message) {
        super(message);
    }
}
