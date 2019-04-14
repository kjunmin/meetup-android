package com.meetup.matt.meetup.Handlers;

import android.content.Context;
import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.meetup.matt.meetup.Helpers.GeocodeHelper;
import com.meetup.matt.meetup.Utils.PolylineOptionsUtil;

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
        String polyString = RouteHandler.getPolyline(apiRes);
        List<LatLng> points = PolyUtil.decode(polyString);
        PolylineOptions pOptions = PolylineOptionsUtil.buildPolylineOptions(colorVal);
        Polyline polyline = map.addPolyline(pOptions.addAll(points));
        return polyline;
    }

    public void removePolylines(ArrayList<Polyline> lastUpdatedPolylines) {
        for (Polyline polyline: lastUpdatedPolylines) {
            if (polyline != null) {
                polyline.remove();
            }
        }
    }

    public void removeMarkers(ArrayList<Marker> lastUpdatedMarkers) {
        for (Marker marker : lastUpdatedMarkers) {
            if (marker != null) {
                marker.remove();
            }
        }
    }

    public Marker updateDestinationMarker(Marker destinationMarker, LatLng destination) {
        if (destinationMarker != null) {
            destinationMarker.remove();
        }
        destinationMarker = map.addMarker(new MarkerOptions().title("dest").position(destination));
        return destinationMarker;
    }

}
