package com.test.king.exceptions;

import com.test.king.constants.ErrorMessages;

public class LoginNullRuntimeException extends RuntimeException {
    public LoginNullRuntimeException() {
        super(ErrorMessages.NULL_LOGIN.getMessage());
    }
}
