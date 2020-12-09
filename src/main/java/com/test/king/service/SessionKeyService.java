package com.test.king.service;

import com.test.king.dto.ScoreDto;
import com.test.king.dto.SessionKeyDto;
import com.test.king.exceptions.InvalidSessionKeyException;
import com.test.king.repository.SessionKeyRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SessionKeyService {

    public static final int EXPIRY_TIME_IN_MINUTES = 10;
    private static SessionKeyService SINGLE_SESSION_TOKEN_SERVICE_INSTANCE;
    private final SessionKeyRepository sessionKeyRepository;

    private SessionKeyService(SessionKeyRepository sessionKeyRepository) {
        this.sessionKeyRepository = sessionKeyRepository;
    }

    public static SessionKeyService getInstance(SessionKeyRepository sessionKeyRepository) {
        if (Objects.isNull(SINGLE_SESSION_TOKEN_SERVICE_INSTANCE)) {
            synchronized (SessionKeyService.class) {
                SINGLE_SESSION_TOKEN_SERVICE_INSTANCE = new SessionKeyService(sessionKeyRepository);
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
        String sessionKey;

        do {
            sessionKey = UUID.randomUUID().toString().replace("-","");
        } while(sessionKeyRepository.exists(sessionKey));

        return sessionKey;
    }

    public String login(int userId) {
        SessionKeyDto sessionKeyDto = generateSessionTokenDto(userId);
        sessionKeyRepository.save(sessionKeyDto);
        return sessionKeyDto.getSessionKey();
    }

    public String highScoreList(int levelId) {
        List<ScoreDto> highScoreList = sessionKeyRepository.getHighScoreList(levelId);
        return parse(highScoreList);
    }

    private String parse(List<ScoreDto> highScoreList) {
        StringBuilder stringBuilder = new StringBuilder();
        for(ScoreDto scoreDto : highScoreList) {
            stringBuilder.append(String.format(",%d=%d", scoreDto.getUserId(), scoreDto.getScore()));
        }
        return (stringBuilder.length() > 0) ? stringBuilder.substring(1) : " ";
    }

    public void saveUserScoreLevel(String sessionKey, int levelId, int score) throws InvalidSessionKeyException {
        if (sessionKeyRepository.isValid(sessionKey)) {
            sessionKeyRepository.saveScoreLevel(levelId, score, sessionKey);
        } else {
            throw new InvalidSessionKeyException();
        }
    }
}
