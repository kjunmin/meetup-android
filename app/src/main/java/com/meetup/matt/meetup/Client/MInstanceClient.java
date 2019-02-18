package com.meetup.matt.meetup.Client;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.meetup.matt.meetup.Handlers.RouteHandler;
import com.meetup.matt.meetup.Helpers.GeocodeHelper;
import com.meetup.matt.meetup.Listeners.DirectionsListener;
import com.meetup.matt.meetup.dto.RouteDTO;
import com.meetup.matt.meetup.dto.UserDTO;

import java.util.List;

public class MInstanceClient {

    private List<UserDTO> userList;
    private Marker lastUpdatedMarker;
    private Polyline lastUpdatedPolyline;
    private GoogleMap map;
    private View view;
    private Context context;
    private LatLng origin;
    private LatLng destination;
    private GeocodeHelper geocodeHelper;
    RouteDTO route;

    public MInstanceClient(GoogleMap map, Context context, View view) {
        this.map = map;
        this.context = context;
        this.view = view;
        this.lastUpdatedMarker = null;
        this.lastUpdatedPolyline = null;
        this.geocodeHelper = new GeocodeHelper(context);
        this.route = new RouteDTO.Builder().build();
    }

    private void enableMapActions() {
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                destination = latLng;
                route.setDestination(latLng);
                String addressValue = geocodeHelper.getAddressFromLatLng(latLng);
                displayLocationUI(addressValue);

            }
        });
    }


    public void startDirections(LatLng origin) {
        route.setOrigin(origin);

    }


    private void plotPolyline() {
        RouteHandler routeHandler = new RouteHandler(route, context);
        routeHandler.getRouteInformation(new DirectionsListener() {
            @Override
            public void onDirectionsResponse(String res) {
                String polyString = RouteHandler.getPolyline(res);
                List<LatLng> points = PolyUtil.decode(polyString);
                if (lastUpdatedPolyline != null) {
                    lastUpdatedPolyline.remove();
                }
                Polyline polyline = map.addPolyline(new PolylineOptions().addAll(points));
                lastUpdatedPolyline = polyline;
            }
        });
    }

    private void displayDestinationMarker() {
        if (lastUpdatedMarker != null) {
            lastUpdatedMarker.remove();
        }
        Marker marker = map.addMarker(new MarkerOptions().title("test").position(destination));
        lastUpdatedMarker = marker;
    }

    public void displayLocationUI(final String addressValue) {
        Snackbar.make(view, addressValue, Snackbar.LENGTH_LONG)
                .setAction("Add Destination", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (route.getDestination() != null && route.getOrigin() != null && route != null) {
                            plotPolyline();
                        }
                        displayDestinationMarker();
                    }
                }).show();
    }

    public void startService() {
        enableMapActions();
    }

    public void setRoute(RouteDTO route) {
        this.route = route;
    }

}
