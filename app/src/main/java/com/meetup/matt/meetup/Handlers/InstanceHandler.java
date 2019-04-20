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
import com.meetup.matt.meetup.Utils.SessionUtil;
import com.meetup.matt.meetup.dto.SessionUserDTO;
import com.meetup.matt.meetup.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class InstanceHandler {
    private Context context;
    private GoogleMap map;
    private GeocodeHelper geocodeHelper;

    public InstanceHandler(Context context, GoogleMap map) {
        this.context = context;
        this.map = map;
        this.geocodeHelper = new GeocodeHelper(context);
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
        if (destinationMarker != null) {
            destinationMarker.remove();
        }
        destinationMarker = map.addMarker(new MarkerOptions().title("dest").position(destination));
        return destinationMarker;
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
