package com.meetup.matt.meetup.Helpers;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeocodeHelper {
    Context context;

    public GeocodeHelper(Context context) {
        this.context = context;
    }

    public String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses = null;
        String addressLine = null;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses == null || addresses.size() == 0) {
            Log.e("TestError", "No Address Found");
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }

            addressLine =  TextUtils.join(System.getProperty("line.separator"), addressFragments);
        }
        return addressLine;
    }
}
