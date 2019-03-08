package com.meetup.matt.meetup.dto;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class UserDTO {

    @SerializedName("firstname") private String firstName;
    @SerializedName("lastname") private String lastName;
    @SerializedName("user_id") private String userId;
    @SerializedName("created_timestamp") private String createdDate;

    public UserDTO() {};



    public UserDTO(String firstName, String lastName, String userId, String createdDate) {
        this.firstName = firstName;
        this.lastName = lastName;
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
}
