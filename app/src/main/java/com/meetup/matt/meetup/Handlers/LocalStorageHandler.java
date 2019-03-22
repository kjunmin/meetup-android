package com.meetup.matt.meetup.Handlers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.meetup.matt.meetup.dto.UserDTO;

public final class LocalStorageHandler {

    public static void storeSessionUser(Context context, String storageName, UserDTO user) {
        SharedPreferences prefs = context.getSharedPreferences(storageName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString("user_details", json);
        editor.commit();
    }

    public static UserDTO getSessionUser(Context context, String storageName) {
        SharedPreferences prefs = context.getSharedPreferences(storageName, context.MODE_PRIVATE);
        String json = prefs.getString("user_details", null);
        Gson gson = new Gson();
        UserDTO user = gson.fromJson(json, UserDTO.class);
        return user;
    }
}
