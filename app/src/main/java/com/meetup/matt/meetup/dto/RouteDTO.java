package com.meetup.matt.meetup.dto;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.meetup.matt.meetup.Helpers.GeocodeHelper;

public class RouteDTO {

    public static final class TransportMode {
        //Constants for route Gmap Directions API
        public static final String DRIVING = "driving";
        public static final String TRANSIT = "transit";
        public static final String WALKING = "walking";
        public static final String BICYCLING = "bicycling";
    }

    public static final class Units {
        public static final String METRIC = "metric";
        public static final String IMPERIAL = "imperial";
    }

    private String userId;
    private LatLng origin;
    private LatLng destination;
    private String originAddress;
    private String destinationAddress;
    private String units;
    private String transportMode;

    public RouteDTO() {};

    public static class Builder {

        public Builder(Context context) {
            this.context = context;
        }

        private Context context;
        private String userId;
        private LatLng origin;
        private LatLng destination;
        private String originAddress;
        private String destinationAddress;
        private String units = RouteDTO.Units.METRIC;
        private String transportMode = RouteDTO.TransportMode.WALKING;
        GeocodeHelper helper = new GeocodeHelper(context);

        public Builder() {

        }

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder setOrigin(LatLng origin) {
            this.origin = origin;
            return this;
        }

        public Builder setDestination(LatLng destination) {
            this.destination = destination;
            return this;
        }

        public Builder setOriginAddress() {
            this.originAddress = helper.getAddressFromLatLng(origin);
            return this;
        }

        public Builder setDestinationAddress() {
            this.destinationAddress = helper.getAddressFromLatLng(destination);
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
            route.userId = this.userId;
            route.origin = this.origin;
            route.destination = this.destination;
            route.originAddress = this.originAddress;
            route.destinationAddress = this.destinationAddress;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOriginAddress() {
        return originAddress;
    }

    public void setOriginAddress(String originAddress) {
        this.originAddress = originAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }
}
