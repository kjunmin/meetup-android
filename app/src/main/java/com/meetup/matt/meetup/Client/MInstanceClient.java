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
import com.google.maps.android.PolyUtil;
import com.meetup.matt.meetup.Handlers.InstanceHandler;
import com.meetup.matt.meetup.Handlers.LocalStorageHandler;
import com.meetup.matt.meetup.Handlers.RouteHandler;
import com.meetup.matt.meetup.Handlers.SocketHandler;
import com.meetup.matt.meetup.Helpers.GeocodeHelper;
import com.meetup.matt.meetup.Listeners.ApiResponseListener;
import com.meetup.matt.meetup.Listeners.GetMeetupSessionListener;
import com.meetup.matt.meetup.Utils.PolylineOptionsUtil;
import com.meetup.matt.meetup.Utils.SessionUtil;
import com.meetup.matt.meetup.WebApi.RouteApi;
import com.meetup.matt.meetup.WebApi.SessionApi;
import com.meetup.matt.meetup.config.Config;
import com.meetup.matt.meetup.dto.MeetupSessionDTO;
import com.meetup.matt.meetup.dto.RouteDTO;
import com.meetup.matt.meetup.dto.SessionUserDTO;
import com.meetup.matt.meetup.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class MInstanceClient {

    private UserDTO userDetails;
    private MeetupSessionDTO sessionDetails;
    private Marker destinationMarker;
    private GoogleMap map;
    private View view;
    private Context context;
    private GeocodeHelper geocodeHelper;
    Socket socket;
    private boolean isDestinationSet;
    private InstanceHandler instanceHandler;
    private ArrayList<SessionUserDTO> sessionUsers;

    public MInstanceClient(GoogleMap map, Context context, View view, MeetupSessionDTO sessionDetails) {
        this.map = map;
        this.context = context;
        this.view = view;
        this.sessionDetails = sessionDetails;
        this.destinationMarker = null;
        this.geocodeHelper = new GeocodeHelper(context);
        this.socket = SocketHandler.getSocket();
        this.isDestinationSet = false;
        userDetails = LocalStorageHandler.getSessionUser(context, Config.SESSION_FILE_NAME);
        this.instanceHandler = new InstanceHandler(context, map);
        sessionUsers = new ArrayList<>();

        startService();
    }


    public void startService() {
        startSocketListener(socket);
        if (SessionUtil.isHost(userDetails, sessionDetails)) {
            enableMapActions();
        }
        SessionApi.handleGetMeetupSessionBySessionId(sessionDetails.getSessionId(), context, new GetMeetupSessionListener() {
            @Override
            public void onMeetupSessionRequestResponse(MeetupSessionDTO meetupSessionDetails) {
                sessionDetails = meetupSessionDetails;
                UserDTO[] users = meetupSessionDetails.getUsers();
                for (UserDTO user : users) {
                    SessionUserDTO sessionUser = new SessionUserDTO();
                    sessionUser.setUser(user);
                    sessionUsers.add(sessionUser);
                }
            }
        });
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

        if (isDestinationSet && sessionDetails != null) {
            SessionApi.handleGetMeetupSessionBySessionId(sessionDetails.getSessionId(), context, new GetMeetupSessionListener() {
                @Override
                public void onMeetupSessionRequestResponse(MeetupSessionDTO meetupSessionDetails) {
                    for (UserDTO user : meetupSessionDetails.getUsers()) {
                        for (SessionUserDTO sessionUser : sessionUsers)
                            if (user.getUserId().equals(sessionUser.getUser().getUserId())) {
                                sessionUser.getUser().setUserLocation(user.getUserLocation());
                            }
                    }
                    updateMapObjects(sessionUsers);
                }
            });

        }
    }

    private void updateMapObjects(ArrayList<SessionUserDTO> sessionUsers) {
        for (int i = 0; i < sessionUsers.size(); i++) {
            if (!isDeviceUser(sessionUsers.get(i).getUser())) {
                updateOtherUser(sessionUsers.get(i), i);
            } else {
                updateUser(sessionUsers.get(i));
            }
        }
    }



    private void updateUser(final SessionUserDTO sessionUser) {
        RouteDTO route = new RouteDTO.Builder(context).setOrigin(sessionUser.getUser().getUserLocation()).setDestination(sessionDetails.getDestinationLocation()).build();
        if (sessionUser.getPolyline() == null) {
            RouteApi.getRouteInformation(context, route, new ApiResponseListener() {
                @Override
                public void onApiResponse(String response) {
                    Polyline polyline;
                    polyline = instanceHandler.plotPolyline(response, Color.BLUE);
                    sessionUser.setPolyline(polyline);
                }
            });
        } else {
            RouteApi.getRouteInformation(context, route, new ApiResponseListener() {
                @Override
                public void onApiResponse(String response) {
                    String polyString = RouteHandler.getPolyline(response);
                    List<LatLng> points = PolyUtil.decode(polyString);
                    sessionUser.getPolyline().setPoints(points);
                }
            });
        }

    }

    private void updateOtherUser(final SessionUserDTO sessionUser, final int userIndex) {
        if (sessionUser.getMarker() == null) {
            Marker marker = map.addMarker(new MarkerOptions().title(sessionUser.getUser().getFirstName()).position(sessionUser.getUser().getUserLocation()));
            sessionUser.setMarker(marker);
        } else {
            sessionUser.getMarker().setPosition(sessionUser.getUser().getUserLocation());
        }
        RouteDTO route = new RouteDTO.Builder(context).setOrigin(sessionUser.getUser().getUserLocation()).setDestination(sessionDetails.getDestinationLocation()).build();
        if (sessionUser.getPolyline() == null) {
            RouteApi.getRouteInformation(context, route, new ApiResponseListener() {
                @Override
                public void onApiResponse(String response) {
                    Polyline polyline;
                    polyline = instanceHandler.plotPolyline(response, PolylineOptionsUtil.getColourByIndex(userIndex));
                    sessionUser.setPolyline(polyline);
                }
            });
        } else {
            RouteApi.getRouteInformation(context, route, new ApiResponseListener() {
                @Override
                public void onApiResponse(String response) {
                    String polyString = RouteHandler.getPolyline(response);
                    List<LatLng> points = PolyUtil.decode(polyString);
                    sessionUser.getPolyline().setPoints(points);
                }
            });
        }
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

    private boolean isDeviceUser(UserDTO user) {
        return userDetails.getUserId().equals(user.getUserId());
    }
}
