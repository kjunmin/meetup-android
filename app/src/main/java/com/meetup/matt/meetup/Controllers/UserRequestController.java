package com.meetup.matt.meetup.Controllers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.meetup.matt.meetup.dto.ResponseDTO;
import com.meetup.matt.meetup.dto.UserDTO;

public class UserRequestController {
    public static UserDTO getUserDetails(String response) {
        Gson gson = new Gson();
        try {
            ResponseDTO res = gson.fromJson(response, ResponseDTO.class);
            UserDTO user = gson.fromJson(res.getData(), UserDTO.class);
            return user;

        } catch (IllegalStateException | JsonSyntaxException exception) {
            Log.d("UserReqError", exception.toString());
            return null;
        }
    }
}
