package com.meetup.matt.meetup.dto;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class MarkerDTO implements Serializable {
    private String id;
    private String address;
    private String label;
    private LatLng location;
    private Date lastUpdate;

    public MarkerDTO(String address, String label, LatLng location) {
        this.id = UUID.randomUUID().toString();
        this.address = address;
        this.label = label;
        this.location = location;
        this.lastUpdate = new Date();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
