package com.meetup.matt.meetup.dto;

import com.google.gson.JsonObject;

public class ResponseDTO {
    private int status;
    private String text;
    private JsonObject data;

    public ResponseDTO(){

    };

    public ResponseDTO(int status, String text, JsonObject data) {
        this.status = status;
        this.text = text;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public String getText() {
        return text;
    }

    public JsonObject getData() { return data; }

}
