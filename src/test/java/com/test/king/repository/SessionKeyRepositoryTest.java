package com.test.king.repository;

import com.test.king.dto.ScoreDto;
import com.test.king.dto.SessionKeyDto;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class SessionKeyRepositoryTest {

    private SessionKeyRepository sessionKeyRepository = SessionKeyRepository.getInstance();

    @Test
    public void testSessionKeyIsValid() {
        String sessionKey = "someSessionKey";
        int userId = new Random().nextInt(100);
        LocalDateTime expiryDateTime = LocalDateTime.now().plusDays(1);

        SessionKeyDto sessionKeyDto = new SessionKeyDto(userId, sessionKey,expiryDateTime);
        sessionKeyRepository.save(sessionKeyDto);
        assertTrue(sessionKeyRepository.isValid(sessionKey));
    }

    @Test
    public void testSessionKeyNonexistentIsInvalid() {
        String sessionKey = "someSessionKey";
        assertFalse(sessionKeyRepository.isValid(sessionKey));
    }

    @Test
    public void testSessionKeyExpiredIsInvalid() {
        String sessionKey = "someSessionKey";
        int userId = new Random().nextInt(100);
        LocalDateTime expiryDateTime = LocalDateTime.now().minusSeconds(1);

        SessionKeyDto sessionKeyDto = new SessionKeyDto(userId, sessionKey,expiryDateTime);
        sessionKeyRepository.save(sessionKeyDto);
        assertFalse(sessionKeyRepository.isValid(sessionKey));
    }

    @Test
    public void testSaveScoreListOneElement() {
        int levelId = 1;
        int userId = 100;
        int score = 1000;
        String sessionKey = String.valueOf(userId);
        SessionKeyDto sessionKeyDto = new SessionKeyDto(userId, sessionKey, LocalDateTime.now());
        List<ScoreDto> expectedList = new ArrayList<>();
        expectedList.add(new ScoreDto(levelId, score, userId));

        sessionKeyRepository.save(sessionKeyDto);
        //case 1 value stored
        sessionKeyRepository.saveScoreLevel(levelId, score, sessionKey);
        List<ScoreDto> actualList = sessionKeyRepository.getHighScoreList(levelId);
        assertThat(expectedList, is(actualList));
    }

    @Test
    public void testSaveScoreListMultipleElements() {
        int levelId = 2;
        int userId = 100;
        int score = 1000;
        String sessionKey = String.valueOf(userId);
        SessionKeyDto sessionKeyDto = new SessionKeyDto(userId, sessionKey, LocalDateTime.now());

        List<ScoreDto> expectedList = new ArrayList<>();
        for (int i = SessionKeyRepository.MAX_RANK_SIZE; i >= 1; i--) {
            expectedList.add(new ScoreDto(levelId, score * i, userId));
        }

        sessionKeyRepository.save(sessionKeyDto);
        //case max records value stored
        for (int i = 1; i <= SessionKeyRepository.MAX_RANK_SIZE; i++) {
            sessionKeyRepository.saveScoreLevel(levelId, score*i, sessionKey);
        }
        List<ScoreDto> actualList = sessionKeyRepository.getHighScoreList(levelId);
        assertThat(expectedList, is(actualList));
    }

    @Test
    public void testSaveScoreListMultipleElementsExceedingHigher() {
        int levelId = 3;
        int userId = 100;
        int score = 1000;
        String sessionKey = String.valueOf(userId);
        SessionKeyDto sessionKeyDto = new SessionKeyDto(userId, sessionKey, LocalDateTime.now());

        List<ScoreDto> expectedList = new ArrayList<>();
        for (int i = SessionKeyRepository.MAX_RANK_SIZE+1; i >= 1; i--) {
            expectedList.add(new ScoreDto(levelId, score * i, userId));
        }
        expectedList.remove(expectedList.size()-1); //remove last one

        sessionKeyRepository.save(sessionKeyDto);
        //case max records value stored
        for (int i = 1; i <= SessionKeyRepository.MAX_RANK_SIZE+1; i++) {
            sessionKeyRepository.saveScoreLevel(levelId, score*i, sessionKey);
        }
        List<ScoreDto> actualList = sessionKeyRepository.getHighScoreList(levelId);
        assertThat(expectedList, is(actualList));
    }

    @Test
    public void testSaveScoreListMultipleElementsExceedingLower() {
        int levelId = 4;
        int userId = 100;
        int score = 1000;
        String sessionKey = String.valueOf(userId);
        SessionKeyDto sessionKeyDto = new SessionKeyDto(userId, sessionKey, LocalDateTime.now());

        List<ScoreDto> expectedList = new ArrayList<>();
        for (int i = SessionKeyRepository.MAX_RANK_SIZE; i >= 1; i--) {
            expectedList.add(new ScoreDto(levelId, score * i, userId));
        }

        sessionKeyRepository.save(sessionKeyDto);
        //case max records value stored
        for (int i = 1; i <= SessionKeyRepository.MAX_RANK_SIZE; i++) {
            sessionKeyRepository.saveScoreLevel(levelId, score*i, sessionKey);
        }
        //lower element
        sessionKeyRepository.saveScoreLevel(levelId, 0, sessionKey);

        List<ScoreDto> actualList = sessionKeyRepository.getHighScoreList(levelId);
        assertThat(expectedList, is(actualList));
    }
}
