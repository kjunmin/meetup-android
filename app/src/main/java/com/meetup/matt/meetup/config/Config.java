package com.meetup.matt.meetup.config;

public class Config {
    public static final String PREF_FILE_NAME = "Preferences";
    public static final String SETTINGS_FILE_NAME = "Settings";

    //Location Settings
    public static final int LOCATION_UPDATE_INTERVAL = 3000; //Time in millis
    public static final int LOCATION_UPDATE_DISTANCE = 0; //Distance in meters
    public static final double DISTANCE_TO_TRIGGER = 100.0;

    //Directions Settings
    public static final String ROUTE_API_URL = "https://maps.googleapis.com/maps/api/directions";

    //Database Settings
    public static final String DEV_URI = "http://10.0.2.2";
    public static final String DEV_PORT = "5000";

    public static final String HOSTNAME= "ec2-54-243-228-140.compute-1.amazonaws.com";
    public static final String PORT = "5432";
    public static final String DB_USER = "hcxkxfckhoodwq";
    public static final String DB_PASSWORD = "fef430591da05a5d8a5abe0166e67d017be236130bbf69b3b7ac40382b23fe5f";
    public static final String URI = "postgres://hcxkxfckhoodwq:fef430591da05a5d8a5abe0166e67d017be236130bbf69b3b7ac40382b23fe5f@ec2-54-243-228-140.compute-1.amazonaws.com:5432/d3gt0tj424qf4g";
}
