package com.test.king.repository;

import com.test.king.dto.ScoreDto;
import com.test.king.dto.SessionKeyDto;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents the "storage" of our Session Keys.
 * It's a singleton class
 * They are used two maps:
 * - Map of SessionKeyDTO (userid, sessionKey, expiryDate) mapped by SessionKey, stores the Session Keys
 * - Map of List of ScoreDTO (level, score, userId) mapped by LevelId, stores the top 15 scores.
 */
public class SessionKeyRepository {

    public static final int MAX_RANK_SIZE = 15;
    private static SessionKeyRepository sessionKeyRepository;

    private final Map<String, SessionKeyDto> mapOfSessionKeyDtoBySessionKey;
    private final Map<Integer, List<ScoreDto>> mapOfScoreDtoByLevelId;

    private SessionKeyRepository() {
        mapOfSessionKeyDtoBySessionKey = new HashMap<>();
        mapOfScoreDtoByLevelId = new HashMap<>();
    }

    public static SessionKeyRepository getInstance() {
        if (Objects.isNull(sessionKeyRepository)) {
            synchronized (SessionKeyRepository.class) {
                sessionKeyRepository = new SessionKeyRepository();
            }
        }
        return sessionKeyRepository;
    }

    /**
     * Saves a Session Key Dto into the Map.
     * It's a synchronized map to avoid concurrent threads to manipulate the map.
     * @param sessionKeyDto DTO to be saved in the Map.
     */
    public synchronized void save(final SessionKeyDto sessionKeyDto) {
        mapOfSessionKeyDtoBySessionKey.put(sessionKeyDto.getSessionKey(), sessionKeyDto);
    }

    /**
     * Verifies if a session key is present in the Map and not expired.
     * @param sessionKey    session key to be verified
     * @return  True if is valid, False otherwise.
     */
    public boolean isValid(final String sessionKey) {
        SessionKeyDto sessionKeyDto = mapOfSessionKeyDtoBySessionKey.get(sessionKey);
        return (Objects.nonNull(sessionKeyDto) && (!sessionKeyDto.getExpiryDateTime().isBefore(LocalDateTime.now())));
    }

    /**
     * Saves the score of a given user, associated by the session key, in a level.
     * Rules:
     * - the map of Score is sorted in descending way.
     * - the map of Score keeps at maximum 15 top score records by level id and distinct user id.
     * It's a synchronized map to avoid concurrent threads to manipulate the map.
     * @param levelId   level id to be stored
     * @param score     score to be stored in the map of scoreDto
     * @param sessionKey    session key containing the user id.
     */
    public synchronized void saveScoreLevel(final int levelId, final int score, final String sessionKey) {
        List<ScoreDto> listOfScore = mapOfScoreDtoByLevelId.getOrDefault(levelId, new ArrayList<>());
        int userId = mapOfSessionKeyDtoBySessionKey.get(sessionKey).getUserId();
        ScoreDto scoreDtoToBeInserted = new ScoreDto(levelId, score, userId);
        //verify if the user exists and update score if the new one is higher
        int indexOfSameScoreDto = listOfScore.indexOf(scoreDtoToBeInserted);
        if (indexOfSameScoreDto >= 0) {
            if (listOfScore.get(indexOfSameScoreDto).getScore() < scoreDtoToBeInserted.getScore()) {
                listOfScore.set(indexOfSameScoreDto, scoreDtoToBeInserted);
            }
        } else {
            if (listOfScore.size() < MAX_RANK_SIZE) {
                listOfScore.add(scoreDtoToBeInserted);
            } else {
                if (score > listOfScore.get(listOfScore.size()-1).getScore()) {
                    listOfScore.remove(listOfScore.size()-1);
                    listOfScore.add(scoreDtoToBeInserted);
                } else {
                    return;
                }
            }
        }
        listOfScore.sort((o1, o2) -> o2.getScore() - o1.getScore()); //reverse sort
        mapOfScoreDtoByLevelId.put(levelId, listOfScore);
    }

    public boolean exists(final String sessionKey) {
        return mapOfSessionKeyDtoBySessionKey.containsKey(sessionKey);
    }

    public List<ScoreDto> getHighScoreList(final int levelId) {
        return mapOfScoreDtoByLevelId.getOrDefault(levelId, new ArrayList<>());
    }
}
