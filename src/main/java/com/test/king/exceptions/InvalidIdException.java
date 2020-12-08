package com.test.king.exceptions;

import com.test.king.constants.ErrorMessages;

public class InvalidIdException extends Exception {
    public InvalidIdException() {
        super(ErrorMessages.INVALID_ID.getMessage());
    }
}
