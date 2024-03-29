package com.meetup.matt.meetup.Controllers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.meetup.matt.meetup.dto.ResponseDTO;
import com.meetup.matt.meetup.dto.UserDTO;


public final class LoginController {
    public static UserDTO getLoginDetails(String response) {
        Gson gson = new Gson();
        try {
            ResponseDTO res = gson.fromJson(response, ResponseDTO.class);
            UserDTO user = gson.fromJson(res.getData(), UserDTO.class);
            return user;

        } catch (IllegalStateException | JsonSyntaxException exception) {
            Log.d("RequestError", exception.toString());
            return null;
        }
    }

    public static boolean evaluateLogin(String response) {
        Gson gson = new Gson();
        try {
            ResponseDTO res = gson.fromJson(response, ResponseDTO.class);
            return (res.getStatus() == 1);
        } catch (IllegalStateException | JsonSyntaxException exception) {
            Log.d("RequestError", exception.toString());
            return false;
        }
    }

    public static boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public static boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}
