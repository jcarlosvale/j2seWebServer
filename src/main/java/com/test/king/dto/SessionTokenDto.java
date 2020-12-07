package com.test.king.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public final class SessionTokenDto {

    private final UserDto userDto;
    private final String sessionKey;
    private final LocalDateTime expiryDateTime;

    public SessionTokenDto(final String login, final String sessionKey, final LocalDateTime expiryDateTime) {
        this.userDto = new UserDto(login);
        this.sessionKey = sessionKey;
        this.expiryDateTime = expiryDateTime;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public LocalDateTime getExpiryDateTime() {
        return expiryDateTime;
    }

    @Override
    public String toString() {
        return "SessionTokenDto{" + "userDto=" + userDto + ", sessionKey='" + sessionKey + '\'' + ", expiryDateTime=" +
                expiryDateTime + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SessionTokenDto that = (SessionTokenDto) o;
        return sessionKey.equals(that.sessionKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionKey);
    }
}
