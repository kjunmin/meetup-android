package com.meetup.matt.meetup.Utils;

import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PolylineOptionsUtil {

    public static int getColourByIndex(int index) {
        Map colourMap = new HashMap();
        colourMap.put(0, Color.MAGENTA);
        colourMap.put(1, Color.RED);
        colourMap.put(2, Color.GREEN);
        colourMap.put(3, Color.YELLOW);
        colourMap.put(4, Color.CYAN);
        if (index < colourMap.size()) {
            return (int)colourMap.get(index);
        } else {
            return Color.RED;
        }
    }

    public static List<LatLng> getOverlappingPoints(ArrayList<List<LatLng>> polylinePoints) {
        for (int i = 0; i< polylinePoints.size(); i++) {
            for (int j = i + 1; j < polylinePoints.size(); j++) {
                return getOverlapPoints(polylinePoints.get(i), polylinePoints.get(j));
            }
        }
        return null;
    }

    private static List<LatLng> getOverlapPoints(List<LatLng> list1, List<LatLng> list2) {
        int[][] table= new int[list1.size()][list2.size()];
        int longest = 0;
        List<LatLng> result = new ArrayList<>();
        for (int i = 0; i < list1.size(); i++) {
            for (int j = 0; j < list2.size(); j++) {
                if (!list1.get(i).equals(list2.get(j))) {
                    continue;
                }
                table[i][j] = (i == 0 || j == 0) ? 1 : 1 + table[i - 1][j - 1];
                if (table[i][j] > longest) {
                    longest = table[i][j];
                    result.clear();
                }
                if (table[i][j] == longest) {
                    for (int k = i - longest + 1; k < i + 1; k++) {
                        result.add(list1.get(k));
                    }
                }
            }
        }
        return result;
    }

    public static List<LatLng> constructPolylineMap(ArrayList<Polyline> polylines) {
        ArrayList<List<LatLng>> list = new ArrayList<>();
        for (Polyline polyline : polylines) {
            list.add(polyline.getPoints());
        }
        List<LatLng> out = getOverlappingPoints(list);
        return out;
    }

    public static PolylineOptions buildPolylineOptions(int colorValue) {
        PolylineOptions polylineOptions = new PolylineOptions().width(10).color(colorValue);
        return polylineOptions;
    }
}
