package com.meetup.matt.meetup.dto;

public class ResponseDTO {
    private int status;
    private String data;

    ResponseDTO(){};

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
