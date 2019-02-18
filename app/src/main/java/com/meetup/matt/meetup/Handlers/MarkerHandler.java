package com.meetup.matt.meetup.Handlers;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MarkerHandler {


    private void setMarker(MarkerOptions markerOptions, GoogleMap map) {
        map.addMarker(markerOptions);
    }
}
