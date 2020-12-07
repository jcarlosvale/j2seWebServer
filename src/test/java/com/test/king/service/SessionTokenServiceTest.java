package com.test.king.service;

import com.test.king.dto.SessionTokenDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SessionTokenServiceTest {

    @Mock
    private SessionTokenService sessionTokenService;

    @Test
    public void testGenerateSessionTokenDto() {
        String expectedLogin = "someLogin";
        String expectedSessionKey = "someSessionKey";
        LocalDateTime expectedExpiryDateTime = LocalDateTime.now();

        when(sessionTokenService.generateSessionKey()).thenReturn(expectedSessionKey);
        when(sessionTokenService.generateExpiryDateTime()).thenReturn(expectedExpiryDateTime);
        when(sessionTokenService.generateSessionTokenDto(expectedLogin)).thenCallRealMethod();

        SessionTokenDto actualSessionTokenDto = sessionTokenService.generateSessionTokenDto("someLogin");

        assertNotNull(actualSessionTokenDto);
        assertEquals(expectedLogin, actualSessionTokenDto.getUserDto().getLogin());
        assertEquals(expectedSessionKey, actualSessionTokenDto.getSessionKey());
        assertEquals(expectedExpiryDateTime, actualSessionTokenDto.getExpiryDateTime());
    }

    @Test
    public void testGenerateSessionKey() {
        when(sessionTokenService.generateSessionKey()).thenCallRealMethod();
        String regex = "^[a-zA-Z0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        for (int i = 0; i < 1000; i++) {
            String actualSessionKey = sessionTokenService.generateSessionKey();
            Matcher matcher = pattern.matcher(actualSessionKey);
            assertTrue(matcher.matches());
        }
    }

    @Test
    public void testGenerateExpiryDateTime() throws InterruptedException {
        when(sessionTokenService.generateExpiryDateTime()).thenCallRealMethod();
        LocalDateTime beforeLocalDateTime = LocalDateTime.now().plusMinutes(SessionTokenService.EXPIRY_TIME_IN_MINUTES);
        TimeUnit.MILLISECONDS.sleep(10);
        LocalDateTime actualExpiryDateTime = sessionTokenService.generateExpiryDateTime();
        TimeUnit.MILLISECONDS.sleep(10);
        LocalDateTime afterLocalDateTime = LocalDateTime.now().plusMinutes(SessionTokenService.EXPIRY_TIME_IN_MINUTES);
        assertTrue(actualExpiryDateTime.isAfter(beforeLocalDateTime));
        assertTrue(actualExpiryDateTime.isBefore(afterLocalDateTime));
    }
}
