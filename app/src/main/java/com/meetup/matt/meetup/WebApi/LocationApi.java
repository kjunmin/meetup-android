package com.meetup.matt.meetup.WebApi;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.meetup.matt.meetup.Handlers.ApiRequestHandler;
import com.meetup.matt.meetup.Helpers.GeocodeHelper;
import com.meetup.matt.meetup.config.Config;
import com.meetup.matt.meetup.dto.RouteDTO;

import java.util.HashMap;
import java.util.Map;

public final class LocationApi {

    public static Map buildRouteRequestObject(RouteDTO routeDTO) {
        final Map<String, String> locationData = new HashMap<>();
        locationData.put("user_id", routeDTO.getUserId());
        locationData.put("current_location", routeDTO.getOriginAddress());
        locationData.put("destination_location", routeDTO.getDestinationAddress());
        locationData.put("current_address", routeDTO.getOrigin().toString());
        locationData.put("destination_address", routeDTO.getDestination().toString());
        return locationData;
    }

    public static void insertRouteData(final RouteDTO route, Context context) {
        String url = Config.DEV_URI+":"+Config.DEV_PORT;
        url += "/api";


        final Map locationData = buildRouteRequestObject(route);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Volley", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return locationData;
            }
        };
        ApiRequestHandler.getInstance(context).addToRequestQueue(stringRequest);
    }
}
