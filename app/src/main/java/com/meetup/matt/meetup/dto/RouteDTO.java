package com.meetup.matt.meetup.dto;

import com.google.android.gms.maps.model.LatLng;

public class RouteDTO {

    private LatLng origin;
    private LatLng destination;
    private String units;
    private String transportMode;

    //Constants for route Gmap Directions API
    public static final String DRIVING = "driving";
    public static final String TRANSIT = "transit";
    public static final String WALKING = "walking";
    public static final String BICYCLING = "bicycling";
    public static final String METRIC = "metric";
    public static final String IMPERIAL = "imperial";

    private RouteDTO() {};

    public static class Builder{
        private LatLng origin;
        private LatLng destination;
        private String units;
        private String transportMode;

        public Builder() {
            this.transportMode = RouteDTO.WALKING;
            this.units = RouteDTO.METRIC;
        }

        public Builder setOrigin(LatLng origin) {
            this.origin = origin;
            return this;
        }

        public Builder setDestination(LatLng destination) {
            this.destination = destination;
            return this;
        }

        public Builder setUnits(String units) {
            this.units = units;
            return this;
        }

        public Builder setTransportMode(String transportMode) {
            this.transportMode = transportMode;
            return this;
        }

        public RouteDTO build() {
            RouteDTO route = new RouteDTO();
            route.origin = this.origin;
            route.destination = this.destination;
            route.transportMode = this.transportMode;
            route.units = this.units;
            return route;
        }
    }



    public LatLng getOrigin() {
        return origin;
    }

    public void setOrigin(LatLng origin) {
        this.origin = origin;
    }

    public LatLng getDestination() {
        return destination;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }
}
