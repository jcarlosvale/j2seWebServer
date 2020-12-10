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
    public void testSaveScoreListUpdatingOneElement() {
        int levelId = 2;

        int userId1 = 100;
        int userId2 = 200;
        int userId3 = 300;

        int score1 = 1000;
        int score2 = 2000;
        int score3 = 3000;

        String sessionKey1 = String.valueOf(userId1);
        String sessionKey2 = String.valueOf(userId2);
        String sessionKey3 = String.valueOf(userId3);

        SessionKeyDto sessionKeyDto1 = new SessionKeyDto(userId1, sessionKey1, LocalDateTime.now());
        SessionKeyDto sessionKeyDto2 = new SessionKeyDto(userId2, sessionKey2, LocalDateTime.now());
        SessionKeyDto sessionKeyDto3 = new SessionKeyDto(userId3, sessionKey3, LocalDateTime.now());

        List<ScoreDto> expectedList = new ArrayList<>();
        expectedList.add(new ScoreDto(levelId, score3, userId3));
        expectedList.add(new ScoreDto(levelId, score2, userId2));
        expectedList.add(new ScoreDto(levelId, score1, userId1));

        sessionKeyRepository.save(sessionKeyDto1);
        sessionKeyRepository.save(sessionKeyDto2);
        sessionKeyRepository.save(sessionKeyDto3);

        sessionKeyRepository.saveScoreLevel(levelId, score1, sessionKey1);
        sessionKeyRepository.saveScoreLevel(levelId, score2, sessionKey2);
        sessionKeyRepository.saveScoreLevel(levelId, score3, sessionKey3);

        List<ScoreDto> actualList = sessionKeyRepository.getHighScoreList(levelId);
        assertThat(expectedList, is(actualList));

        //UPDATE 1st score
        int newScore1 = 4000;
        sessionKeyRepository.saveScoreLevel(levelId, newScore1, sessionKey1);

        //UPDATE 1st score
        int newScore3 = 1000;
        sessionKeyRepository.saveScoreLevel(levelId, newScore3, sessionKey3);


        //UPDATE expected list
        expectedList.clear();
        expectedList.add(new ScoreDto(levelId, newScore1, userId1));
        expectedList.add(new ScoreDto(levelId, score3, userId3));
        expectedList.add(new ScoreDto(levelId, score2, userId2));

        //retrieve
        actualList = sessionKeyRepository.getHighScoreList(levelId);

        //assert
        assertThat(expectedList, is(actualList));
    }

    @Test
    public void testSaveScoreListMultipleSaveOneElement() {
        int levelId = 3;
        int userId = 100;
        int score = 1000;
        String sessionKey = String.valueOf(userId);
        SessionKeyDto sessionKeyDto = new SessionKeyDto(userId, sessionKey, LocalDateTime.now());

        List<ScoreDto> expectedList = new ArrayList<>();
        expectedList.add(new ScoreDto(levelId, score * SessionKeyRepository.MAX_RANK_SIZE, userId));

        sessionKeyRepository.save(sessionKeyDto);
        //case max records value stored
        for (int i = 1; i <= SessionKeyRepository.MAX_RANK_SIZE; i++) {
            sessionKeyRepository.saveScoreLevel(levelId, score*i, sessionKey);
        }
        List<ScoreDto> actualList = sessionKeyRepository.getHighScoreList(levelId);
        assertThat(expectedList, is(actualList));
    }

    @Test
    public void testSaveScoreListMultipleElements() {
        int levelId = 4;
        int userId = 100;
        int score = 1000;

        List<ScoreDto> expectedList = new ArrayList<>();
        for (int i = SessionKeyRepository.MAX_RANK_SIZE; i >= 1; i--) {
            sessionKeyRepository.save(new SessionKeyDto(userId+i, String.valueOf(userId+i), LocalDateTime.now()));
            expectedList.add(new ScoreDto(levelId, score * i, userId+i));
        }

        //case max records value stored
        for (int i = 1; i <= SessionKeyRepository.MAX_RANK_SIZE; i++) {
            sessionKeyRepository.saveScoreLevel(levelId, score*i, String.valueOf(userId+i));
        }
        List<ScoreDto> actualList = sessionKeyRepository.getHighScoreList(levelId);
        assertThat(expectedList, is(actualList));
    }

    @Test
    public void testSaveScoreListMultipleElementsExceedingHigher() {
        int levelId = 5;
        int userId = 100;
        int score = 1000;

        List<ScoreDto> expectedList = new ArrayList<>();
        for (int i = SessionKeyRepository.MAX_RANK_SIZE+1; i >= 1; i--) {
            sessionKeyRepository.save(new SessionKeyDto(userId+i, String.valueOf(userId+i), LocalDateTime.now()));
            expectedList.add(new ScoreDto(levelId, score * i, userId+i));
        }
        expectedList.remove(expectedList.size()-1); //remove last one

        //case max records value stored
        for (int i = 1; i <= SessionKeyRepository.MAX_RANK_SIZE+1; i++) {
            sessionKeyRepository.saveScoreLevel(levelId, score*i, String.valueOf(userId+i));
        }
        List<ScoreDto> actualList = sessionKeyRepository.getHighScoreList(levelId);
        assertThat(expectedList, is(actualList));
    }

    @Test
    public void testSaveScoreListMultipleElementsExceedingLower() {
        int levelId = 6;
        int userId = 100;
        int score = 1000;

        List<ScoreDto> expectedList = new ArrayList<>();
        for (int i = SessionKeyRepository.MAX_RANK_SIZE; i >= 1; i--) {
            sessionKeyRepository.save(new SessionKeyDto(userId+i, String.valueOf(userId+i), LocalDateTime.now()));
            expectedList.add(new ScoreDto(levelId, score * i, userId+i));
        }

        //case max records value stored
        for (int i = 1; i <= SessionKeyRepository.MAX_RANK_SIZE; i++) {
            sessionKeyRepository.saveScoreLevel(levelId, score*i, String.valueOf(userId+i));
        }
        //lower element
        sessionKeyRepository.saveScoreLevel(levelId, 0, String.valueOf(userId+1));

        List<ScoreDto> actualList = sessionKeyRepository.getHighScoreList(levelId);
        assertThat(expectedList, is(actualList));
    }
}
