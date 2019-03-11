package com.meetup.matt.meetup.dto;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ResponseDTO {
    private int status;
    private String text;
    private JsonElement data;

    public ResponseDTO(){

    };

    public ResponseDTO(int status, String text, JsonElement data) {
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

    public JsonElement getData() { return data; }

}
