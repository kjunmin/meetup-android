package com.meetup.matt.meetup.WebApi;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.meetup.matt.meetup.Controllers.MeetupSessionController;
import com.meetup.matt.meetup.Handlers.ApiRequestHandler;
import com.meetup.matt.meetup.Listeners.CreateMeetupSessionListener;
import com.meetup.matt.meetup.Listeners.GetMeetupSessionListener;
import com.meetup.matt.meetup.config.Config;
import com.meetup.matt.meetup.dto.MeetupSessionDTO;

import java.util.HashMap;
import java.util.Map;

public class SessionApi {
    private static Map buildSessionRequestObject(MeetupSessionDTO meetupSessionDTO) {
        final Map<String, String> meetupSessionObject = new HashMap<>();
        meetupSessionObject.put("host_id", meetupSessionDTO.getHost().getUserId());
        return meetupSessionObject;
    }

    public static void handleCreateMeetupSession(MeetupSessionDTO meetupSessionDTO, Context context, final CreateMeetupSessionListener callback) {
        String url = Config.CREATE_MEETUP_SESSION_URL;

        final Map createSessionObj = buildSessionRequestObject(meetupSessionDTO);

        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onMeetupSessionCreateResponse(MeetupSessionController.createMeetupSession(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return createSessionObj;
            }
        };
        ApiRequestHandler.getInstance(context).addToRequestQueue(req);
    }

    public static void handleGetMeetupSessionBySessionCode(String sessionCode, Context context, final GetMeetupSessionListener callback) {
        String url = Config.GET_MEETUP_SESSION_BY_SESSCODE_URL + sessionCode;
        Log.d("Meetup", url);
        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onMeetupSessionRequestResponse(MeetupSessionController.getMeetupSessionDetails(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley", error.toString());
            }
        });
        ApiRequestHandler.getInstance(context).addToRequestQueue(req);
    }

    public static void handleGetMeetupSessionBySessionId(String sessionId, Context context, final GetMeetupSessionListener callback) {
        String url = Config.GET_MEETUP_SESSION_BY_SESSID_URL + sessionId;

        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onMeetupSessionRequestResponse(MeetupSessionController.getMeetupSessionDetails(response));
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
