package com.meetup.matt.meetup.dto;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

public class MapUserDTO {
    @SerializedName("firstname") private String firstname;
    @SerializedName("lastname") private String lastname;
    @SerializedName("user_id") private String userId;
    @SerializedName("user_location") private LatLng userLocation;

    MapUserDTO() {};

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LatLng getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(LatLng userLocation) {
        this.userLocation = userLocation;
    }
}
