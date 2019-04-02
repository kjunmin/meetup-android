package com.meetup.matt.meetup.Client;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
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
import com.meetup.matt.meetup.Listeners.ApiResponseListener;
import com.meetup.matt.meetup.WebApi.RouteApi;
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
    private LatLng markerDestinationPoint;
    private GeocodeHelper geocodeHelper;
    private RouteDTO route;

    public MInstanceClient(GoogleMap map, Context context, View view) {
        this.map = map;
        this.context = context;
        this.view = view;
        this.lastUpdatedMarker = null;
        this.lastUpdatedPolyline = null;
        this.geocodeHelper = new GeocodeHelper(context);
        this.route = new RouteDTO.Builder(context).build();
    }


    private void enableMapActions() {
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                markerDestinationPoint = latLng;
                route.setDestination(latLng);
                String addressValue = geocodeHelper.getAddressFromLatLng(latLng);
                displayLocationUI(addressValue);
            }
        });
    }


    public void startDirections(LatLng origin) {
        route.setOrigin(origin);
    }

    private void getDistance() {
        RouteApi.getDistanceMatrix(context, route, new ApiResponseListener() {
            @Override
            public void onApiResponse(String response) {
                String dist = RouteHandler.getDistanceMatrix(response);
                Log.d("API dist", dist);
            }
        });
    }

    private void plotPolyline() {

        RouteApi.getRouteInformation(context, route, new ApiResponseListener() {
            @Override
            public void onApiResponse(String response) {
                String polyString = RouteHandler.getPolyline(response);
                List<LatLng> points = PolyUtil.decode(polyString);
                if (lastUpdatedPolyline != null) {
                    lastUpdatedPolyline.remove();
                }
                lastUpdatedPolyline = map.addPolyline(new PolylineOptions().addAll(points));
            }
        });
    }

    private void displayDestinationMarker() {
        if (lastUpdatedMarker != null) {
            lastUpdatedMarker.remove();
        }
        lastUpdatedMarker = map.addMarker(new MarkerOptions().title("test").position(markerDestinationPoint));
    }

    public void displayLocationUI(final String addressValue) {
        Snackbar.make(view, addressValue, Snackbar.LENGTH_LONG)
                .setAction("Add Destination", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        enableDestination();
                    }
                }).show();
    }

    private void enableDestination() {
        if (route.getDestination() != null && route.getOrigin() != null && route != null) {
            plotPolyline();
        }
        getDistance();
        displayDestinationMarker();
    }

    public void startService() {
        enableMapActions();
    }

    public void setRoute(RouteDTO route) {
        this.route = route;
    }

}
