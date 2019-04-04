package com.meetup.matt.meetup.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class MeetupSessionDTO implements Parcelable {
    @SerializedName("id") private String sessionId;
    @SerializedName("session_code") private String sessionCode;
    @SerializedName("host") private UserDTO host;
    @SerializedName("users") private UserDTO[] users;
    @SerializedName("destination_location") private LatLng destinationLocation;
    private String destinationAddress;
    private LocalDateTime createdTimestamp;
    private RouteDTO[] routes;


    public MeetupSessionDTO(Parcel in) {
        //Order same as parcel dest
        this.sessionId = in.readString();
        this.sessionCode = in.readString();
        this.destinationAddress = in.readString();
        this.host = (UserDTO)in.readTypedObject(UserDTO.CREATOR);
        this.users = (UserDTO[])in.createTypedArray(UserDTO.CREATOR);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MeetupSessionDTO createFromParcel(Parcel in) {
            return new MeetupSessionDTO(in);
        }
        public MeetupSessionDTO[] newArray(int size) {
            return new MeetupSessionDTO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //Order same as parcel in
        dest.writeString(this.sessionId);
        dest.writeString(this.sessionCode);
        dest.writeString(this.destinationAddress);
        dest.writeTypedObject(host, 0);
        dest.writeTypedArray(users, 0);
    }

    public MeetupSessionDTO() {};

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
