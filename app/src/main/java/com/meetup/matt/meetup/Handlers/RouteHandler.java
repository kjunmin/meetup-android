package com.meetup.matt.meetup.Handlers;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.meetup.matt.meetup.Listeners.DirectionsListener;
import com.meetup.matt.meetup.R;
import com.meetup.matt.meetup.config.Config;
import com.meetup.matt.meetup.dto.RouteDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.security.auth.callback.Callback;


public class RouteHandler {

    private String ROUTE_API_URL = Config.ROUTE_API_URL;
    private RouteDTO route;
    private Context context;

    public RouteHandler(RouteDTO route, Context context){
        this.context = context;
        this.route = route;
    }

    private String constructRequestURL() {
        StringBuilder builder = new StringBuilder();
        builder.append(ROUTE_API_URL);
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
        builder.append("AIzaSyB_Pf1DWcEUPsqZRbDWRzUf41HW8sbqXVQ");
        Log.d("Directions", builder.toString());
        return builder.toString();
    }

    public void getRouteInformation(final DirectionsListener directionsListener) {
        Log.d("ReqUrl", constructRequestURL());
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, constructRequestURL(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                directionsListener.onDirectionsResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("HTTP GET ERROR", error.getMessage());
            }
        });
        queue.add(stringRequest);
    }

    public static String getPolyline(String response) {
        String polyline = null;
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray res = (JSONArray) jsonResponse.get("routes");
            JSONObject routeObj = (JSONObject)res.get(0);
            JSONObject polyObj = (JSONObject) routeObj.get("overview_polyline");
            polyline = polyObj.getString("points");
            Log.d("JSON", polyline);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return polyline;
    }
}
