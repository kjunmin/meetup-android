package com.meetup.matt.meetup.dto;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MeetupSessionDTO implements Serializable {
    @SerializedName("id") private String sessionId;
    @SerializedName("session_code") private String sessionCode;
    @SerializedName("host") private SessionUserDTO host;
    @SerializedName("users") private SessionUserDTO[] users;
    @SerializedName("destination_location") private LatLng destinationLocation;
    private String destinationAddress;
    private LocalDateTime createdTimestamp;

    public MeetupSessionDTO() {}

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionCode() {
        return sessionCode;
    }

    public void setSessionCode(String sessionCode) {
        this.sessionCode = sessionCode;
    }

    public SessionUserDTO getHost() {
        return host;
    }

    public void setHost(SessionUserDTO host) {
        this.host = host;
    }

    public SessionUserDTO[] getUsers() {
        return users;
    }

    public void setUsers(SessionUserDTO[] users) {
        this.users = users;
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


}
