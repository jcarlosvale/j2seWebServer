package com.test.king.dto;

import com.test.king.exceptions.LoginNullRuntimeException;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserDtoTest {

    @Test
    public void testUserDtoEquals() {
        UserDto expected = new UserDto("someLogin");
        UserDto actual = new UserDto("someLogin");
        assertEquals(expected, actual);
        assertNotSame(expected, actual);
    }

    @Test
    public void testLoginCaseSensitive() {
        UserDto expected = new UserDto("someLogin");
        UserDto actual = new UserDto("SomeLogin");
        assertNotEquals(expected, actual);
    }

    @Test(expected = LoginNullRuntimeException.class)
    public void testLoginNullException() {
        new UserDto(null);
    }

}
