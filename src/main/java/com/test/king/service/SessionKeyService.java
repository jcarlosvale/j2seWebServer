package com.test.king.service;

import com.test.king.dto.SessionKeyDto;
import com.test.king.repository.SessionKeyRepository;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class SessionKeyService {

    public static final int EXPIRY_TIME_IN_MINUTES = 10;
    private static SessionKeyService SINGLE_SESSION_TOKEN_SERVICE_INSTANCE;
    private final SessionKeyRepository sessionKeyRepository;

    private SessionKeyService() {
        this.sessionKeyRepository = SessionKeyRepository.getInstance();
    }

    public static SessionKeyService getInstance() {
        if (Objects.isNull(SINGLE_SESSION_TOKEN_SERVICE_INSTANCE)) {
            synchronized (SessionKeyService.class) {
                SINGLE_SESSION_TOKEN_SERVICE_INSTANCE = new SessionKeyService();
            }
        }
        return SINGLE_SESSION_TOKEN_SERVICE_INSTANCE;
    }

    public SessionKeyDto generateSessionTokenDto(int userId) {
        return new SessionKeyDto(userId, generateSessionKey(), generateExpiryDateTime());
    }

    LocalDateTime generateExpiryDateTime() {
        return LocalDateTime.now().plusMinutes(EXPIRY_TIME_IN_MINUTES);
    }

    String generateSessionKey() {
        return UUID.randomUUID().toString().replace("-","");
    }

    public String login(int id) {
        SessionKeyDto sessionKeyDto = generateSessionTokenDto(id);
        sessionKeyRepository.saveOrReplace(sessionKeyDto);
        return sessionKeyDto.getSessionKey();
    }

    public String highScoreList(int id) {
        return "";
    }

    public void saveUserScoreLevel(String sessionKey, int levelId, int score) {
    }
}
