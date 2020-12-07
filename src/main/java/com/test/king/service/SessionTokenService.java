package com.test.king.service;

import com.test.king.dto.SessionTokenDto;

import java.time.LocalDateTime;
import java.util.UUID;

public class SessionTokenService {

    public static final int EXPIRY_TIME_IN_MINUTES = 10;

    public SessionTokenDto generateSessionTokenDto(String login) {
        return new SessionTokenDto(login, generateSessionKey(), generateExpiryDateTime());
    }

    LocalDateTime generateExpiryDateTime() {
        return LocalDateTime.now().plusMinutes(EXPIRY_TIME_IN_MINUTES);
    }

    String generateSessionKey() {
        return UUID.randomUUID().toString().replace("-","");
    }
}
