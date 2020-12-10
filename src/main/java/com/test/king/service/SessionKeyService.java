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

/**
 * It is a Singleton class.
 * Main class providing the services:
 * - Login
 * - Save Score
 * - Ranking List of Highscores
 */
public class SessionKeyService {

    public static final int EXPIRY_TIME_IN_MINUTES = 10;
    private static SessionKeyService sessionKeyService;

    private final SessionKeyRepository sessionKeyRepository;
    private final Logger logger = Logger.getLogger(SessionKeyService.class.getName());

    private SessionKeyService(final SessionKeyRepository sessionKeyRepository) {
        this.sessionKeyRepository = sessionKeyRepository;
    }

    public static SessionKeyService getInstance(final SessionKeyRepository sessionKeyRepository) {
        if (Objects.isNull(sessionKeyService)) {
            synchronized (SessionKeyService.class) {
                sessionKeyService = new SessionKeyService(sessionKeyRepository);
            }
        }
        return sessionKeyService;
    }

    /**
     * Method used to create, save in the repository a SessionKey and retrieve it by userId.
     * @param userId    userId to be generated a session key
     * @return  the generated session key
     */
    public String login(final int userId) {
        logger.log(Level.INFO, LogMessages.LOGIN_SERVICE_START.getMessage(), userId);
        final SessionKeyDto sessionKeyDto = generateSessionTokenDto(userId);
        sessionKeyRepository.save(sessionKeyDto);
        logger.log(Level.INFO, LogMessages.LOGIN_SERVICE_END.getMessage(), sessionKeyDto);
        return sessionKeyDto.getSessionKey();
    }

    /**
     * Method to save a user's score in a level id using informations based on session key
     * @param sessionKey    contains the user id
     * @param levelId       level to be save the score
     * @param score         score to be saved
     * @throws InvalidSessionKeyException   exception in case of an invalid session key (expired, for example)
     */
    public void saveUserScoreLevel(String sessionKey, int levelId, int score) throws InvalidSessionKeyException {
        if (sessionKeyRepository.isValid(sessionKey)) {
            sessionKeyRepository.saveScoreLevel(levelId, score, sessionKey);
        } else {
            throw new InvalidSessionKeyException();
        }
    }

    /**
     * Retrieves the high score list from a level.
     * @param levelId   level id
     * @return  the top 15 scores from a level id in a String specific format and descending order.
     */
    public String highScoreList(int levelId) {
        List<ScoreDto> highScoreList = sessionKeyRepository.getHighScoreList(levelId);
        return parse(highScoreList);
    }

    /**
     * Generate a Session Key Dto associated an user id.
     * @param userId    user id to be associated in the Session Key Dto
     * @return  generated Session Key Dto.
     */
    private SessionKeyDto generateSessionTokenDto(int userId) {
        return new SessionKeyDto(userId, generateSessionKey(), generateExpiryDateTime());
    }

    /**
     * Generates the Expiry Date Time using the validate time in minutes
     * @return  the time to expiry
     */
    private LocalDateTime generateExpiryDateTime() {
        return LocalDateTime.now().plusMinutes(EXPIRY_TIME_IN_MINUTES);
    }

    /**
     * Generates unique session key using UUID random values and looking for in the repository
     * @return  unique session key generated.
     */
    private String generateSessionKey() {
        String sessionKey;

        do {
            sessionKey = UUID.randomUUID().toString().replace("-","");
        } while(sessionKeyRepository.exists(sessionKey));

        return sessionKey;
    }

    /**
     * Formats a List of Score Dto in a specific String
     * Format: <userid1>=<score1>,<userid2>=<score2>...
     * @param highScoreList a list of Score Dto
     * @return  the formatted String
     */
    private String parse(List<ScoreDto> highScoreList) {
        StringBuilder stringBuilder = new StringBuilder();
        for(ScoreDto scoreDto : highScoreList) {
            stringBuilder.append(String.format(",%d=%d", scoreDto.getUserId(), scoreDto.getScore()));
        }
        return (stringBuilder.length() > 0) ? stringBuilder.substring(1) : " ";
    }
}
