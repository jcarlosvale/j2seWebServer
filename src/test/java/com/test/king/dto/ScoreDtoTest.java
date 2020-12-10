package com.test.king.dto;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class ScoreDtoTest {
    @Test
    public void testEqualScoreDtoOnlyByUserIdAndLevelId() {
        int userId = new Random().nextInt(10);
        int levelId = new Random().nextInt(10);
        ScoreDto expected = new ScoreDto(levelId, new Random().nextInt(100), userId);
        ScoreDto actual = new ScoreDto(levelId, new Random().nextInt(100), userId);
        assertEquals(expected, actual);
        assertNotSame(expected, actual);
    }

}
