package com.test.king.service;

import com.test.king.constants.LogMessages;
import com.test.king.dto.ScoreDto;
import com.test.king.dto.SessionKeyDto;
import com.test.king.exceptions.InvalidSessionKeyException;
import com.test.king.repository.SessionKeyRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SessionKeyService {

    public static final int EXPIRY_TIME_IN_MINUTES = 10;
    private static SessionKeyService SINGLE_SESSION_TOKEN_SERVICE_INSTANCE;

    private final SessionKeyRepository sessionKeyRepository;
    private final Logger logger = Logger.getLogger(SessionKeyService.class.getName());

    private SessionKeyService(final SessionKeyRepository sessionKeyRepository) {
        this.sessionKeyRepository = sessionKeyRepository;
    }

    public static SessionKeyService getInstance(final SessionKeyRepository sessionKeyRepository) {
        if (Objects.isNull(SINGLE_SESSION_TOKEN_SERVICE_INSTANCE)) {
            synchronized (SessionKeyService.class) {
                SINGLE_SESSION_TOKEN_SERVICE_INSTANCE = new SessionKeyService(sessionKeyRepository);
            }
        }
        return SINGLE_SESSION_TOKEN_SERVICE_INSTANCE;
    }

    public String login(final int userId) {
        logger.log(Level.INFO, LogMessages.LOGIN_SERVICE_START.getMessage(), userId);
        final SessionKeyDto sessionKeyDto = generateSessionTokenDto(userId);
        sessionKeyRepository.save(sessionKeyDto);
        logger.log(Level.INFO, LogMessages.LOGIN_SERVICE_END.getMessage(), sessionKeyDto);
        return sessionKeyDto.getSessionKey();
    }

    private SessionKeyDto generateSessionTokenDto(int userId) {
        return new SessionKeyDto(userId, generateSessionKey(), generateExpiryDateTime());
    }

    private LocalDateTime generateExpiryDateTime() {
        return LocalDateTime.now().plusMinutes(EXPIRY_TIME_IN_MINUTES);
    }

    private String generateSessionKey() {
        String sessionKey;

        do {
            sessionKey = UUID.randomUUID().toString().replace("-","");
        } while(sessionKeyRepository.exists(sessionKey));

        return sessionKey;
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
