package com.meetup.matt.meetup.Handlers;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.meetup.matt.meetup.Listeners.ApiResponseListener;
import com.meetup.matt.meetup.Utils.PolylineOptionsUtil;
import com.meetup.matt.meetup.Utils.SessionUtil;
import com.meetup.matt.meetup.WebApi.RouteApi;
import com.meetup.matt.meetup.dto.RouteDTO;
import com.meetup.matt.meetup.dto.SessionUserDTO;
import com.meetup.matt.meetup.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class InstanceHandler {
    private GoogleMap map;
    private Context context;

    public InstanceHandler(Context context, GoogleMap map) {
        this.map = map;
        this.context = context;
    }

    public void setPolyline(RouteDTO route, final SessionUserDTO sessionUser, final int userIndex) {
        if (sessionUser.getPolyline() == null) {
            RouteApi.getRouteInformation(context, route, new ApiResponseListener() {
                @Override
                public void onApiResponse(String response) {
                    Polyline polyline;
                    polyline = plotPolyline(response, SessionUtil.getColourByIndex(userIndex));
                    sessionUser.setPolyline(polyline);
                }
            });
        } else {
            RouteApi.getRouteInformation(context, route, new ApiResponseListener() {
                @Override
                public void onApiResponse(String response) {
                    String polyString = RouteHandler.getPolyline(response);
                    List<LatLng> points = PolyUtil.decode(polyString);
                    sessionUser.getPolyline().setPoints(points);
                }
            });
        }
    }

    public void setMarker(RouteDTO route, final SessionUserDTO sessionUser, final int userIndex) {
        if (sessionUser.getMarker() == null) {
            RouteApi.getDistanceMatrix(context, route, new ApiResponseListener() {
                @Override
                public void onApiResponse(String response) {
                    String dist = RouteHandler.getDistanceMatrix(response);
                    Marker marker = map.addMarker(new MarkerOptions().title(sessionUser.getUser().getFirstName())
                            .position(sessionUser.getUser().getUserLocation())
                            .icon(SessionUtil.getBitmapDescriptor(userIndex))
                            .snippet("Distance: " + dist));
                    marker.showInfoWindow();
                    sessionUser.setMarker(marker);
                }
            });
        } else {
            RouteApi.getDistanceMatrix(context, route, new ApiResponseListener() {
                @Override
                public void onApiResponse(String response) {
                    String dist = RouteHandler.getDistanceMatrix(response);
                    Marker marker = sessionUser.getMarker();
                    marker.setSnippet("Distance: " + dist);
                    marker.setPosition(sessionUser.getUser().getUserLocation());
                    marker.showInfoWindow();
                    sessionUser.setMarker(marker);
                }
            });

        }


    }
    public Polyline plotPolyline(String apiRes, int colorVal) {
        Polyline polyline = null;
        try {
            String polyString = RouteHandler.getPolyline(apiRes);
            List<LatLng> points = PolyUtil.decode(polyString);
            PolylineOptions pOptions = PolylineOptionsUtil.buildPolylineOptions(colorVal);
            polyline = map.addPolyline(pOptions.addAll(points));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return polyline;
    }


    public Marker updateDestinationMarker(Marker destinationMarker, LatLng destination) {
        Marker marker = destinationMarker;
        if (destinationMarker != null) {
            destinationMarker.setPosition(destination);
        } else {
            marker = map.addMarker(new MarkerOptions().title("dest").position(destination));
        }
        return  marker;
    }

    public ArrayList<SessionUserDTO> updateSessionUsers(UserDTO[] users, ArrayList<SessionUserDTO> sessionUsers) {
        for (UserDTO user : users) {
            for (SessionUserDTO sessionUser : sessionUsers)
                if (user.getUserId().equals(sessionUser.getUser().getUserId())) {
                    sessionUser.getUser().setUserLocation(user.getUserLocation());
                }
        }
        return sessionUsers;
    }


}
