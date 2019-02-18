package com.meetup.matt.meetup.Handlers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ApiRequestHandler {

    private static ApiRequestHandler instance;
    private RequestQueue requestQueue;
    private static Context context;

    private ApiRequestHandler(Context context) {
        this.context = context;
        this.requestQueue = getRequestQueue();
    }

    public static synchronized ApiRequestHandler getInstance(Context context) {
        if (instance == null) {
            instance = new ApiRequestHandler(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
