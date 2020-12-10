package com.test.king.dto;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents the Session Key used by an User with an Expire Date Time.
 */
public final class SessionKeyDto {

    private final int userId;
    private final String sessionKey;
    private final LocalDateTime expiryDateTime;

    public SessionKeyDto(final int userId, final String sessionKey, final LocalDateTime expiryDateTime) {
        this.userId = userId;
        this.sessionKey = sessionKey;
        this.expiryDateTime = expiryDateTime;
    }

    public int getUserId() {
        return userId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public LocalDateTime getExpiryDateTime() {
        return expiryDateTime;
    }

    @Override
    public String toString() {
        return "SessionKeyDto{" + "userId=" + userId + ", sessionKey='" + sessionKey + '\'' + ", expiryDateTime=" +
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
        SessionKeyDto that = (SessionKeyDto) o;
        return sessionKey.equals(that.sessionKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionKey);
    }
}
