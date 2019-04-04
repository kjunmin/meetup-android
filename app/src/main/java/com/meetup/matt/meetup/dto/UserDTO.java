package com.meetup.matt.meetup.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class UserDTO implements Parcelable {

    public UserDTO(Parcel in) {
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.email = in.readString();
        this.userId = in.readString();
        this.createdDate = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public UserDTO createFromParcel(Parcel in) {
            return new UserDTO(in);
        }

        public UserDTO[] newArray(int size) {
            return new UserDTO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.email);
        dest.writeString(this.userId);
        dest.writeString(this.createdDate);
    }

    @SerializedName("firstname") private String firstName;
    @SerializedName("lastname") private String lastName;
    @SerializedName("email") private String email;
    @SerializedName("user_id") private String userId;
    @SerializedName("created_timestamp") private String createdDate;

    public UserDTO() {};

    public UserDTO(String firstName, String lastName, String email, String userId, String createdDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userId = userId;
        this.createdDate = createdDate;

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
