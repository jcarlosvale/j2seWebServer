package com.test.king.exceptions;

import com.test.king.constants.ErrorMessages;

public class InvalidSessionKeyException extends Exception {
    public InvalidSessionKeyException() {
        super(ErrorMessages.INVALID_SESSION_KEY.getMessage());
    }
}
