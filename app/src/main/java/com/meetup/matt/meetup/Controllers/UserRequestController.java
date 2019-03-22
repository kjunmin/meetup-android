package com.meetup.matt.meetup.Controllers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.meetup.matt.meetup.dto.ResponseDTO;
import com.meetup.matt.meetup.dto.UserDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class UserRequestController {
    public static UserDTO getUserDetails(String response) {
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

    public static ArrayList<UserDTO> getFriendListDetails(String response) {
        Gson gson = new Gson();
        try {
            ResponseDTO res = gson.fromJson(response, ResponseDTO.class);
            UserDTO[] friends = gson.fromJson(res.getData(), UserDTO[].class);
            return new ArrayList<>(Arrays.asList(friends));
        } catch (IllegalStateException | JsonSyntaxException exception) {
            Log.d("RequestError", exception.toString());
            return null;
        }
    }

    public static ResponseDTO addUser(String response) {
        Gson gson = new Gson();
        try {
            ResponseDTO res = gson.fromJson(response, ResponseDTO.class);
            return res;
        } catch (IllegalStateException | JsonSyntaxException exception) {
            Log.d("RequestError", exception.toString());
            return null;
        }
    }
}
