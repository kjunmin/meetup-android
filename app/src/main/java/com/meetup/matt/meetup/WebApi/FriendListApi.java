package com.meetup.matt.meetup.WebApi;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.meetup.matt.meetup.Controllers.UserRequestController;
import com.meetup.matt.meetup.Handlers.ApiRequestHandler;
import com.meetup.matt.meetup.Listeners.AddUserListener;
import com.meetup.matt.meetup.Listeners.GetFriendListListener;
import com.meetup.matt.meetup.config.Config;

import java.util.HashMap;
import java.util.Map;

public final class FriendListApi {

    private static Map<String, String> buildAddUserRequestObject(String userId, String friendEmail) {
        final Map<String, String> request = new HashMap<>();
        request.put("user_id", userId);
        request.put("friend_email", friendEmail);
        return request;
    }

    public static void handleAddFriend(String userId, String friendEmail, Context context, final AddUserListener callback) {
        String url = Config.ADD_USER_URL;

        final Map addUser = buildAddUserRequestObject(userId, friendEmail);

        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onUserAddedResponse(UserRequestController.addUser(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return addUser;
            }
        };
        ApiRequestHandler.getInstance(context).addToRequestQueue(req);
    }


    public static void handleGetFriendList(String userId, Context context, final GetFriendListListener callback) {
        String url = Config.GET_FRIENDS_URL + userId;

        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onDataReceived(UserRequestController.getFriendListDetails(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley", error.toString());
            }
        });
        ApiRequestHandler.getInstance(context).addToRequestQueue(req);
    }
}
