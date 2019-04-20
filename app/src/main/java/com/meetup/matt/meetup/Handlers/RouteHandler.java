package com.meetup.matt.meetup.Handlers;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class RouteHandler {

    public static String getDistanceMatrix(String response) {
        String distance = null;
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray rows = jsonObject.getJSONArray("rows");
            JSONObject rowObj = rows.getJSONObject(0);
            JSONArray elements = rowObj.getJSONArray("elements");
            JSONObject distMatrixObj = elements.getJSONObject(0);
            JSONObject distanceObj = distMatrixObj.getJSONObject("distance");
            distance = distanceObj.getString("text");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return distance;
    }

    public static String getPolyline(String response) {
        String polyline = null;
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray res = (JSONArray) jsonResponse.get("routes");
            JSONObject routeObj = (JSONObject)res.get(0);
            JSONObject polyObj = (JSONObject) routeObj.get("overview_polyline");
            polyline = polyObj.getString("points");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return polyline;
    }
}
