package com.meetup.matt.meetup.WebApi;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.meetup.matt.meetup.Controllers.UserRequestController;
import com.meetup.matt.meetup.Handlers.ApiRequestHandler;
import com.meetup.matt.meetup.Listeners.UserRequestListener;
import com.meetup.matt.meetup.config.Config;

import java.util.Map;

public final class FriendListApi {

    private static void handleFriendRequest(String userId, Context context, final UserRequestListener callback) {
        String url = Config.FRIEND_REQ_URL + userId;

        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onUserReqResponse(UserRequestController.getUserDetails(response));
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
