package com.meetup.matt.meetup.Controllers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.meetup.matt.meetup.dto.MeetupSessionDTO;
import com.meetup.matt.meetup.dto.ResponseDTO;
import com.meetup.matt.meetup.dto.SessionUserDTO;

public final class MeetupSessionController {

    public static SessionUserDTO getMeetupSessionUserDetails(String response) {
        Gson gson = new Gson();
        try {
            ResponseDTO res = gson.fromJson(response, ResponseDTO.class);
            if (res.getStatus() == 1) {
                SessionUserDTO sessionUserDetails = gson.fromJson(res.getData(), SessionUserDTO.class);
                return sessionUserDetails;
            }
            return null;
        } catch (IllegalStateException | JsonSyntaxException exception) {
            Log.d("RequestError", exception.toString());
            return null;
        }
    }

    public static SessionUserDTO[] getMeetupSessionUsersDetails(String response) {
        Gson gson = new Gson();
        try {
            ResponseDTO res = gson.fromJson(response, ResponseDTO.class);
            if (res.getStatus() == 1) {
                SessionUserDTO[] sessionUserDetails = gson.fromJson(res.getData(), SessionUserDTO[].class);
                return sessionUserDetails;
            }
            return null;
        } catch (IllegalStateException | JsonSyntaxException exception) {
            Log.d("RequestError", exception.toString());
            return null;
        }
    }


    public static MeetupSessionDTO getMeetupSessionDetails(String response) {
        Gson gson = new Gson();
        try {
            ResponseDTO res = gson.fromJson(response, ResponseDTO.class);
            if (res.getStatus() == 1) {
                MeetupSessionDTO meetupSessionDetails = gson.fromJson(res.getData(), MeetupSessionDTO.class);
                Log.d("API", "users: " + meetupSessionDetails.getUsers());
                return meetupSessionDetails;
            }
            return null;
        } catch (IllegalStateException | JsonSyntaxException exception) {
            Log.d("RequestError", exception.toString());
            return null;
        }
    }
}
