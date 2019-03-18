package com.meetup.matt.meetup.WebApi;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.meetup.matt.meetup.Handlers.ApiRequestHandler;
import com.meetup.matt.meetup.Handlers.LoginHandler;
import com.meetup.matt.meetup.Listeners.LoginListener;
import com.meetup.matt.meetup.config.Config;

import java.util.HashMap;
import java.util.Map;

public final class LoginApi {

    private static Map<String, String> buildLoginRequestObject(String email, String password) {
        final Map<String, String> userLogin = new HashMap<>();
        userLogin.put("email", email);
        userLogin.put("password", password);
        return userLogin;
    }

    public static void handleLogin(final String email, final String password, Context context, final LoginListener callback) {
        String url = Config.LOGIN_URL;
        final Map userLogin = buildLoginRequestObject(email, password);

        StringRequest req = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                callback.onLoginResponse(LoginHandler.evaluateLogin(response.toString()), LoginHandler.getLoginDetails(response.toString()));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return userLogin;
            }
        };
        ApiRequestHandler.getInstance(context).addToRequestQueue(req);
    };


}
