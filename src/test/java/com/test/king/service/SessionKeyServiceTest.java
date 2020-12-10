package com.test.king.service;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class SessionKeyServiceTest {
/*
    @Mock
    private SessionKeyRepository sessionKeyRepository;

    @Spy
    @InjectMocks
    private SessionKeyService sessionKeyService;

    @Test
    public void testGenerateSessionTokenDto() {
        int expectedUserId = Math.abs(new Random().nextInt());
        String expectedSessionKey = "someSessionKey";
        LocalDateTime expectedExpiryDateTime = LocalDateTime.now();

        doReturn(expectedSessionKey).when(sessionKeyService).generateSessionKey();
        when(sessionKeyService.generateExpiryDateTime()).thenReturn(expectedExpiryDateTime);
        when(sessionKeyService.generateSessionTokenDto(expectedUserId)).thenCallRealMethod();

        SessionKeyDto actualSessionKeyDto = sessionKeyService.generateSessionTokenDto(expectedUserId);

        assertNotNull(actualSessionKeyDto);
        assertEquals(expectedUserId, actualSessionKeyDto.getUserId());
        assertEquals(expectedSessionKey, actualSessionKeyDto.getSessionKey());
        assertEquals(expectedExpiryDateTime, actualSessionKeyDto.getExpiryDateTime());
    }

    @Test
    public void testGenerateSessionKey() {
        when(sessionKeyService.generateSessionKey()).thenCallRealMethod();
        String regex = "^[a-zA-Z0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        for (int i = 0; i < 1000; i++) {
            String actualSessionKey = sessionKeyService.generateSessionKey();
            Matcher matcher = pattern.matcher(actualSessionKey);
            assertTrue(matcher.matches());
        }
    }

    @Test
    public void testGenerateExpiryDateTime() throws InterruptedException {
        when(sessionKeyService.generateExpiryDateTime()).thenCallRealMethod();
        LocalDateTime beforeLocalDateTime = LocalDateTime.now().plusMinutes(SessionKeyService.EXPIRY_TIME_IN_MINUTES);
        TimeUnit.MILLISECONDS.sleep(10);
        LocalDateTime actualExpiryDateTime = sessionKeyService.generateExpiryDateTime();
        TimeUnit.MILLISECONDS.sleep(10);
        LocalDateTime afterLocalDateTime = LocalDateTime.now().plusMinutes(SessionKeyService.EXPIRY_TIME_IN_MINUTES);
        assertTrue(actualExpiryDateTime.isAfter(beforeLocalDateTime));
        assertTrue(actualExpiryDateTime.isBefore(afterLocalDateTime));
    }
    */
}
