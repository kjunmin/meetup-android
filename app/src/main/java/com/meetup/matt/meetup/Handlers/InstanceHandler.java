package com.meetup.matt.meetup.Handlers;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.meetup.matt.meetup.Helpers.GeocodeHelper;
import com.meetup.matt.meetup.Utils.PolylineOptionsUtil;
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
