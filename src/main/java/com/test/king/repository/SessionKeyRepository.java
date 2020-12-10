package com.test.king.repository;

import com.test.king.dto.ScoreDto;
import com.test.king.dto.SessionKeyDto;

import java.time.LocalDateTime;
import java.util.*;

public class SessionKeyRepository {

    private static final int MAX_RANK_SIZE = 15;
    private static SessionKeyRepository SINGLE_SESSION_KEY_REPOSITORY_INSTANCE;

    private final Map<String, SessionKeyDto> mapOfSessionKeyDtoBySessionKey;
    private final Map<Integer, List<ScoreDto>> mapOfScoreDtoByLevelId;

    private SessionKeyRepository() {
        mapOfSessionKeyDtoBySessionKey = new HashMap<>();
        mapOfScoreDtoByLevelId = new HashMap<>();
    }

    public static SessionKeyRepository getInstance() {
        if (Objects.isNull(SINGLE_SESSION_KEY_REPOSITORY_INSTANCE)) {
            synchronized (SessionKeyRepository.class) {
                SINGLE_SESSION_KEY_REPOSITORY_INSTANCE = new SessionKeyRepository();
            }
        }
        return SINGLE_SESSION_KEY_REPOSITORY_INSTANCE;
    }

    public synchronized void save(SessionKeyDto sessionKeyDto) {
        mapOfSessionKeyDtoBySessionKey.put(sessionKeyDto.getSessionKey(), sessionKeyDto);
    }

    public boolean isValid(String sessionKey) {
        SessionKeyDto sessionKeyDto = mapOfSessionKeyDtoBySessionKey.get(sessionKey);
        return (Objects.nonNull(sessionKeyDto) && (!sessionKeyDto.getExpiryDateTime().isBefore(LocalDateTime.now())));
    }

    public synchronized void saveScoreLevel(int levelId, int score, String sessionKey) {
        List<ScoreDto> listOfScore = mapOfScoreDtoByLevelId.getOrDefault(levelId, new ArrayList<>());
        int userId = mapOfSessionKeyDtoBySessionKey.get(sessionKey).getUserId();
        if (listOfScore.size() < MAX_RANK_SIZE) {
            listOfScore.add(new ScoreDto(levelId, score, userId));
        } else {
            if (score > listOfScore.get(listOfScore.size()-1).getScore()) {
                listOfScore.remove(listOfScore.size()-1);
                listOfScore.add(new ScoreDto(levelId, score, userId));
            } else {
                return;
            }
        }
        listOfScore.sort((o1, o2) -> o2.getScore() - o1.getScore()); //reverse sort
        mapOfScoreDtoByLevelId.put(levelId, listOfScore);
    }

    public boolean exists(String sessionKey) {
        return mapOfSessionKeyDtoBySessionKey.containsKey(sessionKey);
    }

    public List<ScoreDto> getHighScoreList(int levelId) {
        return mapOfScoreDtoByLevelId.getOrDefault(levelId, new ArrayList<>());
    }
}
