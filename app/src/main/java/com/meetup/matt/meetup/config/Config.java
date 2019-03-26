package com.meetup.matt.meetup.config;

public class Config {

    //Environment settings
    public static final String ENV_PROD = "PRODUCTION";
    public static final String ENV_DEV = "DEVELOPMENT";
    public static final String ENV = ENV_DEV;


    //Local Storage Settings
    public static final String PREF_FILE_NAME = "Preferences";
    public static final String SETTINGS_FILE_NAME = "Settings";
    public static final String SESSION_FILE_NAME = "Session_Data";

    //Location Settings
    public static final int LOCATION_UPDATE_INTERVAL = 3000; //Time in millis
    public static final int LOCATION_UPDATE_DISTANCE = 0; //Distance in meters
    public static final double DISTANCE_TO_TRIGGER = 100.0;

    //Directions Settings
    public static final String GOOGLE_API_KEY = "AIzaSyB_Pf1DWcEUPsqZRbDWRzUf41HW8sbqXVQ";
    public static final String GOOGLE_DIRECTIONS_API_URL = "https://maps.googleapis.com/maps/api/directions";
    public static final String GOOGLE_DISTANCE_MATRIX_API_URL = "https://maps.googleapis.com/maps/api/distancematrix";

    //Endpoint Settings
    public static final String PROD_URI = "http://meetupendpoint-env.g6hzsmdx3t.ap-southeast-1.elasticbeanstalk.com";
    public static final String DEV_URI = "http://10.0.2.2";
    public static final String DEV_PORT = "5000";
    public static final String ENDPOINT_URI = ENV == ENV_PROD ? PROD_URI : DEV_URI + ":" + DEV_PORT;

//    public static final String HOSTNAME= "ec2-54-243-228-140.compute-1.amazonaws.com";
//    public static final String PORT = "5432";
//    public static final String DB_USER = "hcxkxfckhoodwq";
//    public static final String DB_PASSWORD = "fef430591da05a5d8a5abe0166e67d017be236130bbf69b3b7ac40382b23fe5f";
//    public static final String URI = "postgres://hcxkxfckhoodwq:fef430591da05a5d8a5abe0166e67d017be236130bbf69b3b7ac40382b23fe5f@ec2-54-243-228-140.compute-1.amazonaws.com:5432/d3gt0tj424qf4g";

    //API ACCESS
    public static final String REGISTER_URL = ENDPOINT_URI+"/api/user/register";
    public static final String LOGIN_URL = ENDPOINT_URI+"/api/user/login";
    public static final String GET_USER_BY_UUID_URL = ENDPOINT_URI+"api/getUserByUuid/";
    public static final String ROUTE_INFO_URL = ENDPOINT_URI+"/api/route/";
    public static final String ROUTE_UPSERT_URL = ENDPOINT_URI+"/api/route/upsert";
    public static final String ADD_USER_URL = ENDPOINT_URI+"/api/friend/addFriend";
    public static final String GET_FRIENDS_URL = ENDPOINT_URI+"/api/friend/getFriends/";

}
