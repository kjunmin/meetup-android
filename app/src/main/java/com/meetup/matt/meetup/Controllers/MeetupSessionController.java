package com.meetup.matt.meetup.Controllers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import com.meetup.matt.meetup.dto.MeetupSessionDTO;
import com.meetup.matt.meetup.dto.ResponseDTO;

public class MeetupSessionController {

    public static MeetupSessionDTO createMeetupSession(String response) {
        Gson gson = new Gson();
        try {
            ResponseDTO res = gson.fromJson(response, ResponseDTO.class);

            if (res.getStatus() == 1) {
                MeetupSessionDTO meetupSessionDetails = gson.fromJson(res.getData(), MeetupSessionDTO.class);
                return meetupSessionDetails;
            }
        } catch (IllegalStateException | JsonSyntaxException exception) {
            Log.d("RequestError", exception.toString());
            return null;
        }
        return null;
    }

    public static MeetupSessionDTO getMeetupSessionDetails(String response) {
        Gson gson = new Gson();
        try {
            ResponseDTO res = gson.fromJson(response, ResponseDTO.class);
            if (res.getStatus() == 1) {
                MeetupSessionDTO meetupSessionDetails = gson.fromJson(res.getData(), MeetupSessionDTO.class);
                return meetupSessionDetails;
            }
        } catch (IllegalStateException | JsonSyntaxException exception) {
            Log.d("RequestError", exception.toString());
            return null;
        }
        return null;
    }
}
