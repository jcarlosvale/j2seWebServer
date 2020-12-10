package com.test.king.exceptions;

import com.test.king.constants.ErrorMessages;

/**
 * Used in case of invalid URI, like: wrong pattern, missing resources mapping, wrong query...
 */
public class InvalidUriException extends Exception{
    public InvalidUriException() {
        super(ErrorMessages.INVALID_URI.getMessage());
    }
}
