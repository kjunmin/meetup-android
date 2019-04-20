package com.meetup.matt.meetup.Utils;

import android.graphics.Color;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.meetup.matt.meetup.dto.MeetupSessionDTO;
import com.meetup.matt.meetup.dto.UserDTO;

import java.util.HashMap;
import java.util.Map;

public class SessionUtil {

    public static int getColourByIndex(int index) {
        Map<Integer, Integer> colourMap = new HashMap<>();
        colourMap.put(0, Color.BLUE);
        colourMap.put(1, Color.MAGENTA);
        colourMap.put(2, Color.GREEN);
        colourMap.put(3, Color.YELLOW);
        colourMap.put(4, Color.CYAN);
        if (index < colourMap.size()) {
            return colourMap.get(index);
        } else {
            return Color.RED;
        }
    }

    public static BitmapDescriptor getBitmapDescriptor(int index) {
        int colorVal = getColourByIndex(index);
        float[] hsv = new float[3];
        Color.colorToHSV(colorVal, hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    public static boolean isHost(UserDTO userDetails, MeetupSessionDTO sessionDetails) {
        return userDetails.getUserId().equals(sessionDetails.getHost().getUserId());
    }
}
