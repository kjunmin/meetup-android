package com.meetup.matt.meetup;

import android.support.test.runner.AndroidJUnit4;

import com.meetup.matt.meetup.Controllers.LoginController;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginTest extends TestCase {

    @Test
    public void isLoginEmailValidTest() {
        assertTrue(LoginController.isEmailValid("test@email.com"));
        assertFalse(LoginController.isEmailValid("test"));
    }
}
