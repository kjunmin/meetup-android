package com.meetup.matt.meetup.Client;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.meetup.matt.meetup.Adapters.RouteInfoAdapter;
import com.meetup.matt.meetup.Handlers.InstanceHandler;
import com.meetup.matt.meetup.Handlers.LocalStorageHandler;
import com.meetup.matt.meetup.Handlers.SocketHandler;
import com.meetup.matt.meetup.Helpers.GeocodeHelper;
import com.meetup.matt.meetup.Listeners.GetMeetupSessionListener;
import com.meetup.matt.meetup.Listeners.GetSessionUsersListener;
import com.meetup.matt.meetup.R;
import com.meetup.matt.meetup.Utils.SessionUtil;
import com.meetup.matt.meetup.WebApi.SessionApi;
import com.meetup.matt.meetup.config.Config;
import com.meetup.matt.meetup.dto.MeetupSessionDTO;
import com.meetup.matt.meetup.dto.RouteDTO;
import com.meetup.matt.meetup.dto.SessionUserDTO;
import com.meetup.matt.meetup.dto.UserDTO;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

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
    private ArrayList<SessionUserDTO> globalSessionUsers;
    private RecyclerView mRouteInfoRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

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
        this.globalSessionUsers = new ArrayList<>();

        startService();
    }

    private void loadRouteInfoListView(ArrayList<SessionUserDTO> sessionUsers) {
        mRouteInfoRecyclerView = view.findViewById(R.id.routeinfo_recycler_view);
        mRouteInfoRecyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(context);
        mRouteInfoRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new RouteInfoAdapter(sessionUsers);
        mRouteInfoRecyclerView.setAdapter(mAdapter);
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
                SessionUserDTO[] users = meetupSessionDetails.getUsers();
                ArrayList<SessionUserDTO> sessionUserList = new ArrayList<>(Arrays.asList(users));
                globalSessionUsers = sessionUserList;
                loadRouteInfoListView(sessionUserList);
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
        socket.emit(SocketHandler.Event.Server.ON_USER_LOCATION_CHANGE, sessionDetails.getSessionId(), userDetails.getUserId(), userLocation.latitude, userLocation.longitude);

        if (isDestinationSet && sessionDetails != null) {
            SessionApi.handleGetMeetupSessionUsers(sessionDetails.getSessionId(), context, new GetSessionUsersListener() {
                @Override
                public void onSessionUsersRequestResponse(SessionUserDTO[] sessionUsers) {
                    globalSessionUsers = instanceHandler.updateSessionUsers(sessionUsers, globalSessionUsers);
                    updateMapObjects(globalSessionUsers);
                }
            });

        }
    }

    private void updateMapObjects(ArrayList<SessionUserDTO> sessionUsers) {
        for (int i = 0; i < sessionUsers.size(); i++) {
            updateUserMapObjects(sessionUsers.get(i), i);
        }
    }


    private void updateUserMapObjects(final SessionUserDTO sessionUser, final int userIndex) {
        RouteDTO route = new RouteDTO.Builder(context).setOrigin(sessionUser.getUserLocation()).setDestination(sessionDetails.getDestinationLocation()).build();
        if (!isDeviceUser(sessionUser.getUser())) {
            instanceHandler.setMarker(route, sessionUser, userIndex);
        }
        instanceHandler.setPolyline(route, sessionUser, userIndex);
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
                        destinationMarker = instanceHandler.updateDestinationMarker(destinationMarker, sessionDetails.getDestinationLocation());
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
