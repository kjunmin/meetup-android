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
import com.meetup.matt.meetup.Handlers.LocalStorageHandler;
import com.meetup.matt.meetup.Handlers.RouteHandler;
import com.meetup.matt.meetup.Handlers.SocketHandler;
import com.meetup.matt.meetup.Helpers.GeocodeHelper;
import com.meetup.matt.meetup.Listeners.ApiResponseListener;
import com.meetup.matt.meetup.Listeners.GetMeetupSessionListener;
import com.meetup.matt.meetup.WebApi.RouteApi;
import com.meetup.matt.meetup.WebApi.SessionApi;
import com.meetup.matt.meetup.config.Config;
import com.meetup.matt.meetup.dto.MeetupSessionDTO;
import com.meetup.matt.meetup.dto.RouteDTO;
import com.meetup.matt.meetup.dto.UserDTO;
import com.squareup.okhttp.Route;

import java.util.ArrayList;
import java.util.List;

public class MInstanceClient {

    private UserDTO userDetails;
    private MeetupSessionDTO sessionDetails;
    private ArrayList<Marker> lastUpdatedMarkers;
    private ArrayList<Polyline> lastUpdatedPolylines;
    private Marker destinationMarker;
    Socket socket;
    private GoogleMap map;
    private View view;
    private Context context;
    private LatLng markerDestinationPoint;
    private GeocodeHelper geocodeHelper;
    private boolean isDestinationSet;

    public MInstanceClient(GoogleMap map, Context context, View view, MeetupSessionDTO sessionDetails) {
        this.map = map;
        this.context = context;
        this.view = view;
        this.sessionDetails = sessionDetails;
        this.socket = SocketHandler.getSocket();
        this.lastUpdatedMarkers = new ArrayList<>();
        this.lastUpdatedPolylines = new ArrayList<>();
        this.destinationMarker = null;
        this.geocodeHelper = new GeocodeHelper(context);
        this.isDestinationSet = false;
        userDetails = LocalStorageHandler.getSessionUser(context, Config.SESSION_FILE_NAME);
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
        String addressValue = geocodeHelper.getAddressFromLatLng(latLng);
        displayLocationUI(addressValue, latLng);
    }

    public void displayLocationUI(final String addressValue, final LatLng latLng) {
        Snackbar.make(view, addressValue, Snackbar.LENGTH_LONG)
                .setAction("Add Destination", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        updateDestinationMarker(latLng);
                        sessionDetails.setDestinationLocation(latLng);
                        sessionDetails.setDestinationAddress(addressValue);
                        isDestinationSet = true;
                        emitSocketOnDestinationSetEvent(socket, sessionDetails);
                    }
                }).show();
    }


    public void onLocationChanged(LatLng userLocation) {

        socket.emit(SocketHandler.Event.Server.ON_USER_LOCATION_CHANGE, userDetails.getUserId(), userLocation.latitude, userLocation.longitude);
        SessionApi.handleGetMeetupSessionBySessionId(sessionDetails.getSessionId(), context, new GetMeetupSessionListener() {
            @Override
            public void onMeetupSessionRequestResponse(MeetupSessionDTO meetupSessionDetails) {
                sessionDetails = meetupSessionDetails;
                if (isDestinationSet) {
                    Log.d("Session", sessionDetails.getDestinationLocation().toString());
                    try {

                        UserDTO[] userlist = sessionDetails.getUsers();
                        ArrayList<RouteDTO> routes = new ArrayList<>();
                        updateMarkers(lastUpdatedMarkers);
                        for (UserDTO user : userlist) {

                            Marker marker = map.addMarker(new MarkerOptions().title(user.getFirstName()).position(user.getUserLocation()));
                            lastUpdatedMarkers.add(marker);
                            RouteDTO route = new RouteDTO.Builder(context).setOrigin(user.getUserLocation()).setDestination(sessionDetails.getDestinationLocation()).build();
                            routes.add(route);
                        }
                        plotPolylines(routes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

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

    private void plotPolylines(ArrayList<RouteDTO> routes) {

        updatePolylines(lastUpdatedPolylines);

        for (RouteDTO route : routes) {
            RouteApi.getRouteInformation(context, route, new ApiResponseListener() {
                @Override
                public void onApiResponse(String response) {
                    String polyString = RouteHandler.getPolyline(response);
                    List<LatLng> points = PolyUtil.decode(polyString);
                    Polyline polyline = map.addPolyline(new PolylineOptions().addAll(points));
                    lastUpdatedPolylines.add(polyline);
                }
            });
        }
    }

    private void updateDestinationMarker(LatLng destination) {
        if (destinationMarker != null) {
            destinationMarker.remove();
        }
        destinationMarker = map.addMarker(new MarkerOptions().title("dest").position(destination));
    }

    private void updatePolylines(ArrayList<Polyline> lastUpdatedPolylines) {
        for (Polyline polyline: lastUpdatedPolylines) {
            if (polyline != null) {
                polyline.remove();
            }
        }
    }

    private void updateMarkers(ArrayList<Marker> lastUpdatedMarkers) {
        for (Marker marker : lastUpdatedMarkers) {
            if (marker != null) {
                marker.remove();
            }
        }
    }


    public void startService() {
        enableMapActions();
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


}
