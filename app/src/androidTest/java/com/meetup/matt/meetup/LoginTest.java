package com.meetup.matt.meetup;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;
import android.test.mock.MockContext;

import com.meetup.matt.meetup.Handlers.LoginHandler;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginTest extends TestCase {

    @Test
    public void isLoginEmailValidTest() {
        assertTrue(LoginHandler.isEmailValid("test@email.com"));
        assertFalse(LoginHandler.isEmailValid("test"));
    }
}
