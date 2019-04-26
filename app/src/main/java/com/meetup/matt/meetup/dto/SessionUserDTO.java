package com.meetup.matt.meetup.dto;

import android.os.Parcel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SessionUserDTO implements Serializable {
    @SerializedName("user") private UserDTO user;
    private Polyline polyline;
    private Marker marker;
    @SerializedName("distance") private String distance;
    @SerializedName("transport_mode") private String transportMode;
    @SerializedName("user_location") private LatLng userLocation;


    public SessionUserDTO() {


    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    public LatLng getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(LatLng userLocation) {
        this.userLocation = userLocation;
    }
}
