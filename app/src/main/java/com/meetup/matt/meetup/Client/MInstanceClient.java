package com.meetup.matt.meetup.Client;

import android.content.Context;
import android.graphics.Color;
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
import com.google.gson.Gson;
import com.meetup.matt.meetup.Handlers.InstanceHandler;
import com.meetup.matt.meetup.Handlers.LocalStorageHandler;
import com.meetup.matt.meetup.Handlers.RouteHandler;
import com.meetup.matt.meetup.Handlers.SocketHandler;
import com.meetup.matt.meetup.Helpers.GeocodeHelper;
import com.meetup.matt.meetup.Listeners.ApiResponseListener;
import com.meetup.matt.meetup.Listeners.GetMeetupSessionListener;
import com.meetup.matt.meetup.Utils.PolylineOptionsUtil;
import com.meetup.matt.meetup.WebApi.RouteApi;
import com.meetup.matt.meetup.WebApi.SessionApi;
import com.meetup.matt.meetup.config.Config;
import com.meetup.matt.meetup.dto.MeetupSessionDTO;
import com.meetup.matt.meetup.dto.RouteDTO;
import com.meetup.matt.meetup.dto.UserDTO;

import java.util.ArrayList;

public class MInstanceClient {

    private UserDTO userDetails;
    private MeetupSessionDTO sessionDetails;
    private ArrayList<Marker> lastUpdatedMarkers;
    private ArrayList<Polyline> lastUpdatedPolylines;
    private Marker destinationMarker;
    private GoogleMap map;
    private View view;
    private Context context;
    private GeocodeHelper geocodeHelper;
    Socket socket;
    private boolean isDestinationSet;
    private InstanceHandler instanceHandler;

    public MInstanceClient(GoogleMap map, Context context, View view, MeetupSessionDTO sessionDetails) {
        this.map = map;
        this.context = context;
        this.view = view;
        this.sessionDetails = sessionDetails;
        this.lastUpdatedMarkers = new ArrayList<>();
        this.lastUpdatedPolylines = new ArrayList<>();
        this.destinationMarker = null;
        this.geocodeHelper = new GeocodeHelper(context);
        this.socket = SocketHandler.getSocket();
        this.isDestinationSet = false;
        userDetails = LocalStorageHandler.getSessionUser(context, Config.SESSION_FILE_NAME);
        this.instanceHandler = new InstanceHandler(context, map);

        startService();
    }


    public void startService() {
        startSocketListener(socket);
        if (isHost()) {
            enableMapActions();
        }
    }

    private void enableMapActions() {

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                selectMarkerDestinationPoint(latLng);
            }
        });
    }

    private void selectMarkerDestinationPoint(LatLng latLng) {
        String addressValue = geocodeHelper.getAddressFromLatLng(latLng);
        displayLocationUI(addressValue, latLng);
    }


    private void setDestination(LatLng latLng, String addressValue) {
        destinationMarker = instanceHandler.updateDestinationMarker(destinationMarker, latLng);
        sessionDetails.setDestinationLocation(latLng);
        sessionDetails.setDestinationAddress(addressValue);
        isDestinationSet = true;
        emitSocketOnDestinationSetEvent(socket, sessionDetails);
    }

    public void displayLocationUI(final String addressValue, final LatLng latLng) {
        Snackbar.make(view, addressValue, Snackbar.LENGTH_LONG)
                .setAction("Add Destination", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                       setDestination(latLng, addressValue);
                    }
                }).show();
    }


    public void onLocationChanged(LatLng userLocation) {
        socket.emit(SocketHandler.Event.Server.ON_USER_LOCATION_CHANGE, userDetails.getUserId(), userLocation.latitude, userLocation.longitude);
        updateMapObjects();
    }

    private void updateMapObjects() {
        SessionApi.handleGetMeetupSessionBySessionId(sessionDetails.getSessionId(), context, new GetMeetupSessionListener() {
            @Override
            public void onMeetupSessionRequestResponse(MeetupSessionDTO meetupSessionDetails) {
                sessionDetails = meetupSessionDetails;
                if (isDestinationSet) {
                    try {
                        UserDTO[] userlist = sessionDetails.getUsers();
                        instanceHandler.removeMarkers(lastUpdatedMarkers);
                        instanceHandler.removePolylines(lastUpdatedPolylines);

                        for (int i = 0; i < userlist.length; i++) {
                            UserDTO user = userlist[i];
                            if (!isDeviceUser(user)) {
                                updateOtherUser(user, i);
                            } else {
                                updateUser(user);
                            }
                        }

//                        if (lastUpdatedPolylines.size() > 1) {
//                            List<LatLng> points = PolylineOptionsUtil.constructPolylineMap(lastUpdatedPolylines);
//                            Log.d("points", "common: " + points.toString());
//                            PolylineOptions pOptions = new PolylineOptions().width(10).color(Color.RED);
//                            Polyline polyline = map.addPolyline(pOptions.addAll(points));
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }



    private void updateUser(UserDTO user) {
        RouteDTO route = new RouteDTO.Builder(context).setOrigin(user.getUserLocation()).setDestination(sessionDetails.getDestinationLocation()).build();
        plotPolyline(route, Color.BLUE);
    }

    private void updateOtherUser(UserDTO user, int userIndex) {
        Marker marker = map.addMarker(new MarkerOptions().title(user.getFirstName()).position(user.getUserLocation()));
        lastUpdatedMarkers.add(marker);
        RouteDTO route = new RouteDTO.Builder(context).setOrigin(user.getUserLocation()).setDestination(sessionDetails.getDestinationLocation()).build();
        int polylineColor = PolylineOptionsUtil.getColourByIndex(userIndex);
        plotPolyline(route, polylineColor);
    }

    private void getDistance(RouteDTO route) {
        RouteApi.getDistanceMatrix(context, route, new ApiResponseListener() {
            @Override
            public void onApiResponse(String response) {
                String dist = RouteHandler.getDistanceMatrix(response);
                Log.d("API dist", dist);
            }
        });
    }

    private void plotPolyline(RouteDTO route, final int colorVal) {

        RouteApi.getRouteInformation(context, route, new ApiResponseListener() {
            @Override
            public void onApiResponse(String response) {
                Polyline polyline;
                polyline = instanceHandler.plotPolyline(response, colorVal);
                lastUpdatedPolylines.add(polyline);
            }
        });
    }


    private void emitSocketOnDestinationSetEvent(Socket socket, MeetupSessionDTO meetupSessionDTO) {
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
                        isDestinationSet = true;
                    }
                });
            }
        });
    }

    private boolean isHost() {
        return sessionDetails.getHost().getUserId().equals(userDetails.getUserId());
    }

    private boolean isDeviceUser(UserDTO user) {
        return userDetails.getUserId().equals(user.getUserId());
    }
}
