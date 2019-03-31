package com.meetup.matt.meetup.dto;

import com.google.android.gms.maps.model.LatLng;

import java.time.LocalDateTime;

public class MeetupSessionDTO {
    private String sessionId;
    private UserDTO host;
    private LatLng destinationLocation;
    private String destinationAddress;
    private LocalDateTime createdTimestamp;
    private RouteDTO[] routes;
    private UserDTO[] users;

    public MeetupSessionDTO() {};

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public UserDTO getHost() {
        return host;
    }

    public void setHost(UserDTO host) {
        this.host = host;
    }

    public LatLng getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(LatLng destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public LocalDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public RouteDTO[] getRoutes() {
        return routes;
    }

    public void setRoutes(RouteDTO[] routes) {
        this.routes = routes;
    }

    public UserDTO[] getUsers() {
        return users;
    }

    public void setUsers(UserDTO[] users) {
        this.users = users;
    }
}
