package com.meetup.matt.meetup.Handlers;

import android.util.Log;

import com.google.gson.Gson;
import com.meetup.matt.meetup.dto.ResponseDTO;

import org.json.JSONObject;

public class LoginHandler {
    public static boolean evaluateLogin(String response) {
        Gson gson = new Gson();
        gson.toJson(response);
        ResponseDTO res = gson.fromJson(response, ResponseDTO.class);
        Log.d("evaluatehandler", response);
        Log.d("evaluatehandler", res.getData());
        return true;
    }
}
 