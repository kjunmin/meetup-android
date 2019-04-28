package com.meetup.matt.meetup.config;

public class Config {

    //Environment settings
    private static final String ENV_PROD = "release";
    private static final String ENV_DEV = "debug";
    private static final String ENV = ENV_DEV;

    //Local Storage Settings
    public static final String PREF_FILE_NAME = "Preferences";
    public static final String SETTINGS_FILE_NAME = "Settings";
    public static final String SESSION_FILE_NAME = "Session_Data";

    //Location Settings
    public static final int LOCATION_UPDATE_INTERVAL = 6000; //Time in millis
    public static final int FASTEST_UPDATE_INTERVAL = 4000; //Distance in meters

    //Directions Settings
    public static final String GOOGLE_API_KEY = "AIzaSyB_Pf1DWcEUPsqZRbDWRzUf41HW8sbqXVQ";
    public static final String GOOGLE_DIRECTIONS_API_URL = "https://maps.googleapis.com/maps/api/directions";
    public static final String GOOGLE_DISTANCE_MATRIX_API_URL = "https://maps.googleapis.com/maps/api/distancematrix";

    //Endpoint Settings
    private static final String PROD_URI = "http://meetupendpoint-env.g6hzsmdx3t.ap-southeast-1.elasticbeanstalk.com";
    public static final String DEV_URI = "http://10.0.2.2";
    public static final String DEV_PORT = "5000";
    public static final String ENDPOINT_URI = ENV.equals(ENV_PROD) ? PROD_URI : DEV_URI + ":" + DEV_PORT;

    //API ACCESS
    public static final String REGISTER_URL = ENDPOINT_URI+"/api/user/register";
    public static final String LOGIN_URL = ENDPOINT_URI+"/api/user/login";
    public static final String GET_USER_BY_UUID_URL = ENDPOINT_URI+"api/getUserByUuid/";
    public static final String ADD_USER_URL = ENDPOINT_URI+"/api/friend/addFriend";
    public static final String GET_FRIENDS_URL = ENDPOINT_URI+"/api/friend/getFriends/";
    public static final String CREATE_MEETUP_SESSION_URL = ENDPOINT_URI+"/api/session/create";
    public static final String GET_MEETUP_SESSION_BY_SESSID_URL = ENDPOINT_URI+"/api/session/getdetails/";
    public static final String GET_MEETUP_SESSION_BY_SESSCODE_URL = ENDPOINT_URI+"/api/session/";
    public static final String ADD_USER_TO_MEETUP_SESSION_URL = ENDPOINT_URI+"/api/session/adduser";
    public static final String GET_MEETUP_SESSION_USER = ENDPOINT_URI+"/api/sessionuser";
    public static final String GET_MEETUP_SESSION_USERS = ENDPOINT_URI+"/api/sessionusers";
    public static final String UPDATE_MEETUP_SESSION_USER = ENDPOINT_URI+"/api/session/userupdate";
}
