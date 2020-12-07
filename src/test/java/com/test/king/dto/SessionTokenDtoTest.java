package com.test.king.dto;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class SessionTokenDtoTest {

    @Test
    public void testEqualSessionTokenDtoOnlyBySessionKey() {
        SessionTokenDto expected = new SessionTokenDto("someLogin", "sameToken", LocalDateTime.now());
        SessionTokenDto actual = new SessionTokenDto("otherLogin", "sameToken", LocalDateTime.now().plusMinutes(1));
        assertEquals(expected, actual);
        assertNotSame(expected, actual);
    }
}
