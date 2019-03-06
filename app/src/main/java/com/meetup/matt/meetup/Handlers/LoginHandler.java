package com.meetup.matt.meetup.Handlers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.meetup.matt.meetup.dto.ResponseDTO;

import org.json.JSONObject;

public class LoginHandler {
    public static boolean evaluateLogin(String response) {
        Gson gson = new Gson();
        try {
            ResponseDTO res = gson.fromJson(response, ResponseDTO.class);
            Log.d("GSON", "Test:" + res.getData().toString());
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
