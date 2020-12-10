package com.test.king.exceptions;

import com.test.king.constants.ErrorMessages;

/**
 * Used when the SessionKey is expired OR does not exist.
 */
public class InvalidSessionKeyException extends Exception {
    public InvalidSessionKeyException() {
        super(ErrorMessages.INVALID_SESSION_KEY.getMessage());
    }
}
