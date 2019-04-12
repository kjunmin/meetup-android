package com.meetup.matt.meetup.WebApi;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.meetup.matt.meetup.Handlers.ApiRequestHandler;
import com.meetup.matt.meetup.Listeners.ApiResponseListener;
import com.meetup.matt.meetup.config.Config;
import com.meetup.matt.meetup.dto.RouteDTO;

public class RouteApi {

    private static String buildDistanceMatrixApiUrl(RouteDTO route) {
        StringBuilder builder = new StringBuilder();
        builder.append(Config.GOOGLE_DISTANCE_MATRIX_API_URL);
        builder.append("/json?");
        builder.append("origins=");
        builder.append(route.getOrigin().latitude + "," + route.getOrigin().longitude);
        builder.append("&destinations=");
        builder.append(route.getDestination().latitude + "," + route.getDestination().longitude);
        builder.append("&mode=");
        builder.append(route.getTransportMode());
        builder.append("&units=");
        builder.append(route.getUnits());
        builder.append("&key=");
        builder.append(Config.GOOGLE_API_KEY);
        Log.d("DistanceMatrixAPI", builder.toString());
        return builder.toString();
    }

    private static String buildDirectionsApiUrl(RouteDTO route) {
        StringBuilder builder = new StringBuilder();
        builder.append(Config.GOOGLE_DIRECTIONS_API_URL);
        builder.append("/json?");
        builder.append("origin=");
        builder.append(route.getOrigin().latitude + "," + route.getOrigin().longitude);
        builder.append("&destination=");
        builder.append(route.getDestination().latitude + "," + route.getDestination().longitude);
        builder.append("&mode=");
        builder.append(route.getTransportMode());
        builder.append("&units=");
        builder.append(route.getUnits());
        builder.append("&key=");
        builder.append(Config.GOOGLE_API_KEY);
        Log.d("DirectionsAPI", builder.toString());
        return builder.toString();
    }

    public static void getDistanceMatrix(Context context, RouteDTO route, final ApiResponseListener apiResponseListener) {
        StringRequest req = new StringRequest(Request.Method.GET, buildDistanceMatrixApiUrl(route), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                apiResponseListener.onApiResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("HTTP GET ERROR", error.getMessage());
            }
        });
        ApiRequestHandler.getInstance(context).addToRequestQueue(req);
    }

    public static void getRouteInformation(Context context, RouteDTO route, final ApiResponseListener apiResponseListener) {
        StringRequest req = new StringRequest(Request.Method.GET, buildDirectionsApiUrl(route), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                apiResponseListener.onApiResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("HTTP GET ERROR", error.getMessage());
            }
        });
        ApiRequestHandler.getInstance(context).addToRequestQueue(req);
    }


}
