package com.meetup.matt.meetup.Handlers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.meetup.matt.meetup.dto.MeetupSessionDTO;
import com.meetup.matt.meetup.dto.UserDTO;

public final class LocalStorageHandler {

    public static void storeSessionDetails(Context context, String storageName, MeetupSessionDTO sessionDetails) {
        SharedPreferences prefs = context.getSharedPreferences(storageName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(sessionDetails);
        editor.putString("session_details", json);
        editor.apply();
    }

    public static MeetupSessionDTO getSessionDetails(Context context, String storageName) {
        SharedPreferences prefs = context.getSharedPreferences(storageName, context.MODE_PRIVATE);
        String json = prefs.getString("session_details", null);
        Gson gson = new Gson();
        MeetupSessionDTO sessionDetails = gson.fromJson(json, MeetupSessionDTO.class);
        return sessionDetails;
    }

    public static void storeSessionUser(Context context, String storageName, UserDTO user) {
        SharedPreferences prefs = context.getSharedPreferences(storageName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString("user_details", json);
        editor.apply();
    }

    public static UserDTO getSessionUser(Context context, String storageName) {
        SharedPreferences prefs = context.getSharedPreferences(storageName, context.MODE_PRIVATE);
        String json = prefs.getString("user_details", null);
        Gson gson = new Gson();
        UserDTO user = gson.fromJson(json, UserDTO.class);
        return user;
    }
}
