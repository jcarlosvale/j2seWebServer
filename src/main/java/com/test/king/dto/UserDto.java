package com.test.king.dto;

import com.test.king.exceptions.LoginNullRuntimeException;

import java.util.Objects;

public final class UserDto {
    private final String login;

    public UserDto(String login) {
        if (Objects.isNull(login)) {
            throw new LoginNullRuntimeException();
        }
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public String toString() {
        return "UserDto{" + "login='" + login + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDto userDto = (UserDto) o;
        return login.equals(userDto.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}
