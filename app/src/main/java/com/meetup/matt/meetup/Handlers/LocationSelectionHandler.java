package com.meetup.matt.meetup.Handlers;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.meetup.matt.meetup.Listeners.DirectionsListener;
import com.meetup.matt.meetup.dto.RouteDTO;

import java.util.List;

public class LocationSelectionHandler {

    private static LocationSelectionHandler instance;
    private Marker lastDestinationMarker;
    private Polyline lastPolyline;

    private LocationSelectionHandler() {
        this.lastPolyline = null;
        this.lastDestinationMarker = null;
    }

    public static synchronized LocationSelectionHandler getInstance() {
        if (instance == null) {
            instance = new LocationSelectionHandler();
        }
        return instance;
    }

    public void displayPolyline(LatLng origin, LatLng destination, Context context, final GoogleMap map) {
        RouteDTO route = new RouteDTO.Builder()
                                    .setOrigin(origin)
                                    .setDestination(destination)
                                    .setUnits(RouteDTO.METRIC)
                                    .setTransportMode(RouteDTO.WALKING)
                                    .build();

        RouteHandler routeHandler = new RouteHandler(route, context);
        routeHandler.getRouteInformation(new DirectionsListener() {
            @Override
            public void onDirectionsResponse(String value) {
                String polyline = RouteHandler.getPolyline(value);
                if (polyline != null) {
                    List<LatLng> pointList = PolyUtil.decode(polyline);
                    if (lastPolyline != null) {
                        lastPolyline.remove();
                    }
                    map.addPolyline(new PolylineOptions().addAll(pointList));
                }
            }
        });
    }



}
