package com.meetup.matt.meetup.Client;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.maps.android.PolyUtil;
import com.meetup.matt.meetup.Handlers.RouteHandler;
import com.meetup.matt.meetup.Handlers.SocketHandler;
import com.meetup.matt.meetup.Helpers.GeocodeHelper;
import com.meetup.matt.meetup.Listeners.ApiResponseListener;
import com.meetup.matt.meetup.Listeners.GetMeetupSessionListener;
import com.meetup.matt.meetup.WebApi.RouteApi;
import com.meetup.matt.meetup.WebApi.SessionApi;
import com.meetup.matt.meetup.dto.MeetupSessionDTO;
import com.meetup.matt.meetup.dto.RouteDTO;
import com.meetup.matt.meetup.dto.UserDTO;

import java.util.List;

public class MInstanceClient {

    private MeetupSessionDTO sessionDetails;
    private Marker lastUpdatedMarker;
    private Polyline lastUpdatedPolyline;
    Socket socket;
    private GoogleMap map;
    private View view;
    private Context context;
    private LatLng markerDestinationPoint;
    private GeocodeHelper geocodeHelper;
    private RouteDTO route;

    public MInstanceClient(GoogleMap map, Context context, View view, MeetupSessionDTO sessionDetails) {
        this.map = map;
        this.context = context;
        this.view = view;
        this.sessionDetails = sessionDetails;
        this.socket = SocketHandler.getSocket();
        this.lastUpdatedMarker = null;
        this.lastUpdatedPolyline = null;
        this.geocodeHelper = new GeocodeHelper(context);
        this.route = new RouteDTO.Builder(context).build();
    }


    private void enableMapActions() {
        startSocketListener(socket);
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                setMarkerDestinationPoint(latLng);
            }
        });
    }

    private void setMarkerDestinationPoint(LatLng latLng) {
        markerDestinationPoint = latLng;
        route.setDestination(latLng);
        String addressValue = geocodeHelper.getAddressFromLatLng(latLng);
        displayLocationUI(addressValue, latLng);
    }

    public void displayLocationUI(final String addressValue, final LatLng latLng) {
        Snackbar.make(view, addressValue, Snackbar.LENGTH_LONG)
                .setAction("Add Destination", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        enableDestination();
                        emitSocketOnDestinationSetEvent(socket, sessionDetails, latLng);
                    }
                }).show();
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

    private void emitSocketOnDestinationSetEvent(Socket socket, MeetupSessionDTO meetupSessionDTO, LatLng destinationLatLng) {
        meetupSessionDTO.setDestinationLocation(destinationLatLng);
        Gson gson = new Gson();
        String req = gson.toJson(meetupSessionDTO);
        socket.emit(SocketHandler.Event.Server.ON_DESTINATION_UPDATE, req);
    }

    private void startSocketListener(Socket socket) {
        socket.on(SocketHandler.Event.Client.ON_DESTINATION_UPDATE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String sessionId = (String) args[0];
                SessionApi.handleGetMeetupSessionBySessionId(sessionId, context, new GetMeetupSessionListener() {
                    @Override
                    public void onMeetupSessionRequestResponse(MeetupSessionDTO meetupSessionDetails) {
                        sessionDetails = meetupSessionDetails;
                    }
                });
            }
        });
    }

    public void setRoute(RouteDTO route) {
        this.route = route;
    }

}
