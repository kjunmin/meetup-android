package com.meetup.matt.meetup.WebApi;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.meetup.matt.meetup.Handlers.ApiRequestHandler;
import com.meetup.matt.meetup.Handlers.RegisterHandler;
import com.meetup.matt.meetup.Listeners.LoginListener;
import com.meetup.matt.meetup.Listeners.RegisterListener;
import com.meetup.matt.meetup.config.Config;
import com.meetup.matt.meetup.dto.RegistrationDTO;

import java.util.HashMap;
import java.util.Map;

public final class RegisterApi {

    private static Map buildRegistrationRequestObject(RegistrationDTO registrationDTO) {
        final Map<String, String> userRegistration = new HashMap<>();
        userRegistration.put("email", registrationDTO.getEmail());
        userRegistration.put("firstname", registrationDTO.getFirstname());
        userRegistration.put("lastname", registrationDTO.getLastname());
        userRegistration.put("password", registrationDTO.getPassword());
        return userRegistration;
    }

    public static void handleRegistration(RegistrationDTO registrationDTO, Context context, final RegisterListener callback) {
        String url = Config.REGISTER_URL;
        final Map userRegistration = buildRegistrationRequestObject(registrationDTO);

        StringRequest req = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onRegisterResponse(RegisterHandler.evaluateRegistration(response), RegisterHandler.getMessage(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return userRegistration;
            }
        };
        ApiRequestHandler.getInstance(context).addToRequestQueue(req);
    }
}
