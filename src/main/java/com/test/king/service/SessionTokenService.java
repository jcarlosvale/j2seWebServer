package com.test.king.service;

import com.test.king.dto.SessionTokenDto;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class SessionTokenService {

    public static final int EXPIRY_TIME_IN_MINUTES = 10;
    private static SessionTokenService SINGLE_SESSION_TOKEN_SERVICE_INSTANCE;

    public static SessionTokenService getInstance() {
        if (Objects.isNull(SINGLE_SESSION_TOKEN_SERVICE_INSTANCE)) {
            synchronized (SessionTokenService.class) {
                SINGLE_SESSION_TOKEN_SERVICE_INSTANCE = new SessionTokenService();
            }
        }
        return SINGLE_SESSION_TOKEN_SERVICE_INSTANCE;
    }

    public SessionTokenDto generateSessionTokenDto(String login) {
        return new SessionTokenDto(login, generateSessionKey(), generateExpiryDateTime());
    }

    LocalDateTime generateExpiryDateTime() {
        return LocalDateTime.now().plusMinutes(EXPIRY_TIME_IN_MINUTES);
    }

    String generateSessionKey() {
        return UUID.randomUUID().toString().replace("-","");
    }

    public void login(int id) {
    }

    public void highScoreList(int id) {
    }

    public void saveUserScoreLevel(String sessionKey, int levelId, int score) {
    }
}
