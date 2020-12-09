package com.test.king.dto;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class SessionKeyDtoTest {

    @Test
    public void testEqualSessionTokenDtoOnlyBySessionKey() {
        SessionKeyDto expected = new SessionKeyDto(Math.abs(new Random().nextInt()), "sameToken", LocalDateTime.now());
        SessionKeyDto actual = new SessionKeyDto(Math.abs(new Random().nextInt()), "sameToken", LocalDateTime.now().plusMinutes(1));
        assertEquals(expected, actual);
        assertNotSame(expected, actual);
    }
}
