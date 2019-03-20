package com.meetup.matt.meetup.Controllers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.meetup.matt.meetup.dto.ResponseDTO;

public final class RegisterContoller {
    public static boolean evaluateRegistration(String response) {
        Gson gson = new Gson();
        try {
            ResponseDTO res = gson.fromJson(response, ResponseDTO.class);

            if (res.getStatus() == 1) {
                return true;
            } else {
                return false;
            }

        } catch (IllegalStateException | JsonSyntaxException exception) {
            Log.d("LoginError", exception.toString());
            return false;
        }
    }

    public static String getMessage(String response) {
        Gson gson = new Gson();
        try {
            ResponseDTO res = gson.fromJson(response, ResponseDTO.class);
            return res.getText();

        } catch (IllegalStateException | JsonSyntaxException exception) {
            Log.d("LoginError", exception.toString());
            return null;
        }
    }
}
