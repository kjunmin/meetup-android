package com.meetup.matt.meetup.Helpers;

import com.google.android.gms.location.LocationRequest;
import com.meetup.matt.meetup.config.Config;

public class LocationHelper {

    public static LocationRequest createLocationRequest() {
        LocationRequest request = new LocationRequest();
        request.setInterval(Config.LOCATION_UPDATE_INTERVAL);
        request.setFastestInterval(Config.FASTEST_UPDATE_INTERVAL);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return request;
    }

}
