package com.meetup.matt.meetup.WebApi;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.meetup.matt.meetup.Handlers.ApiRequestHandler;
import com.meetup.matt.meetup.config.Config;

import java.util.HashMap;
import java.util.Map;

public class LoginApi {

    public static void isEmailValid(final String email, Context context) {
        String url = Config.DEV_URI+":"+Config.DEV_PORT+"/login";

        final Map<String, String> emailReq = new HashMap<>();
        emailReq.put("email", email);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Volley", response.toString());
                return true;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return emailReq;
            }
        };
        ApiRequestHandler.getInstance(context).addToRequestQueue(stringRequest);
    };


}