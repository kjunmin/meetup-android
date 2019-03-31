package com.meetup.matt.meetup.WebApi;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.meetup.matt.meetup.Controllers.MeetupSessionController;
import com.meetup.matt.meetup.Handlers.ApiRequestHandler;
import com.meetup.matt.meetup.Listeners.GetMeetupSessionListener;
import com.meetup.matt.meetup.config.Config;
import com.meetup.matt.meetup.dto.MeetupSessionDTO;

import java.util.HashMap;
import java.util.Map;

public class SessionApi {
    private static Map buildSessionRequestObject(MeetupSessionDTO meetupSessionDTO) {
        final Map<String, String> meetupSessionObject = new HashMap<>();
        meetupSessionObject.put("session_id", meetupSessionDTO.getSessionId());
        return meetupSessionObject;
    }

    public static void handleGetMeetupSession(MeetupSessionDTO meetupSessionDTO, Context context, final GetMeetupSessionListener callback) {
        String url = Config.ADD_USER_URL;

        final Map meetupSessionObject = buildSessionRequestObject(meetupSessionDTO);

        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onMeetupSessionRequestResponse(MeetupSessionController.getMeetupSessionDetails(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return meetupSessionObject;
            }
        };
        ApiRequestHandler.getInstance(context).addToRequestQueue(req);
    }
}
