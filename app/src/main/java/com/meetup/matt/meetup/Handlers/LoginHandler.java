package com.meetup.matt.meetup.Handlers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.meetup.matt.meetup.dto.ResponseDTO;
import com.meetup.matt.meetup.dto.UserDTO;


public class LoginHandler {
    public static UserDTO getLoginDetails(String response) {
        Gson gson = new Gson();
        try {
            ResponseDTO res = gson.fromJson(response, ResponseDTO.class);
            UserDTO user = gson.fromJson(res.getData(), UserDTO.class);
            return user;

        } catch (IllegalStateException | JsonSyntaxException exception) {
            Log.d("LoginError", exception.toString());
            return null;
        }
    }

    public static boolean evaluateLogin(String response) {
        Gson gson = new Gson();
        try {
            ResponseDTO res = gson.fromJson(response, ResponseDTO.class);

            //Login Successful
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
}
