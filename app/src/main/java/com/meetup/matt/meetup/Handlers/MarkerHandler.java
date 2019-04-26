package com.meetup.matt.meetup.Handlers;

public class MarkerHandler {


    public static String buildInfoWindow(String distance) {
        return String.format("Distance: %s", distance);
    }
}
