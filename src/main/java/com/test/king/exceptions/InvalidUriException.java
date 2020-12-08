package com.test.king.exceptions;

import com.test.king.constants.ErrorMessages;

public class InvalidUriException extends Exception{
    public InvalidUriException() {
        super(ErrorMessages.INVALID_URI.getMessage());
    }
}
