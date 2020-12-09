package com.test.king.repository;

import com.test.king.dto.SessionKeyDto;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class SessionKeyRepository {

    private static SessionKeyRepository SINGLE_SESSION_KEY_REPOSITORY_INSTANCE;

    private final Map<Integer, SessionKeyDto> mapOfSessionKeyDtoByUserId;

    private SessionKeyRepository() {
        mapOfSessionKeyDtoByUserId = new HashMap<>();
    }

    public static SessionKeyRepository getInstance() {
        if (Objects.isNull(SINGLE_SESSION_KEY_REPOSITORY_INSTANCE)) {
            synchronized (SessionKeyRepository.class) {
                SINGLE_SESSION_KEY_REPOSITORY_INSTANCE = new SessionKeyRepository();
            }
        }
        return SINGLE_SESSION_KEY_REPOSITORY_INSTANCE;
    }

    public synchronized void saveOrReplace(SessionKeyDto sessionKeyDto) {
        mapOfSessionKeyDtoByUserId.remove(sessionKeyDto.getUserId());
        mapOfSessionKeyDtoByUserId.put(sessionKeyDto.getUserId(), sessionKeyDto);
    }
}
