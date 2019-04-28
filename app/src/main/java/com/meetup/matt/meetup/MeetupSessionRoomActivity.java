package com.meetup.matt.meetup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.meetup.matt.meetup.Adapters.FriendListAdapter;
import com.meetup.matt.meetup.Handlers.LocalStorageHandler;
import com.meetup.matt.meetup.Handlers.SocketHandler;
import com.meetup.matt.meetup.Helpers.ActivityTransitionHelper;
import com.meetup.matt.meetup.Listeners.SessionListeners;
import com.meetup.matt.meetup.Utils.SessionUtil;
import com.meetup.matt.meetup.WebApi.SessionApi;
import com.meetup.matt.meetup.config.Config;
import com.meetup.matt.meetup.dto.MeetupSessionDTO;
import com.meetup.matt.meetup.dto.SessionUserDTO;
import com.meetup.matt.meetup.dto.UserDTO;

import java.util.ArrayList;
import java.util.Arrays;

public class MeetupSessionRoomActivity extends AppCompatActivity {

    private MeetupSessionDTO sessionDetails;
    private UserDTO userDetails;
    private Socket socket;
    private TextView mRoomCodeView;
    private Button mLaunchMapsButton;
    private RecyclerView mSessionUserListView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup_session_room);
        mRoomCodeView = findViewById(R.id.room_code_textview);
        mLaunchMapsButton = findViewById(R.id.launch_maps_button);
        mSessionUserListView = findViewById(R.id.sessionlist_recycler_view);
        mSessionUserListView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(MeetupSessionRoomActivity.this);
        mSessionUserListView.setLayoutManager(layoutManager);

        userDetails = LocalStorageHandler.getSessionUser(getApplicationContext(), Config.SESSION_FILE_NAME);

        Boolean isExternalUser = getIntent().getBooleanExtra("isExternalUser", false);
        socket = SocketHandler.createSocketConnection();
        startSocketListener(socket);


        if (!isExternalUser) {
            mLaunchMapsButton.setVisibility(View.VISIBLE);
            initializeHostSession();
        } else {
            sessionDetails = LocalStorageHandler.getSessionDetails(getApplicationContext(), Config.SESSION_FILE_NAME);
            initializeFriendSession(sessionDetails);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (SessionUtil.isHost(userDetails, sessionDetails)) {
            socket.emit(SocketHandler.Event.Server.DELETE_SESSION, sessionDetails.getSessionId());
        } else {
            socket.emit(SocketHandler.Event.Server.DELETE_USER_FROM_SESSION, userDetails.getUserId(), sessionDetails.getSessionId());
        }

        SocketHandler.destroySocketConnection();
    }


    private void initializeFriendSession(MeetupSessionDTO meetupSessionDetails) {
        mRoomCodeView.setText(meetupSessionDetails.getSessionCode());
        final UserDTO userDetails = LocalStorageHandler.getSessionUser(getApplicationContext(), Config.SESSION_FILE_NAME);

        SessionApi.handleAddUserToMeetupSession(userDetails.getEmail(),
                meetupSessionDetails.getSessionId(),
                meetupSessionDetails.getHost().getUser().getUserId(),
                getApplicationContext(),
                new SessionListeners.GetMeetupSessionListener() {

                    @Override
                    public void onMeetupSessionRequestResponse(MeetupSessionDTO meetupSessionDetails) {
                        emitSocketOnJoinEvent(socket, userDetails, meetupSessionDetails);
                        loadSessionUserList(meetupSessionDetails.getSessionId());
                    }
                });
    };

    private void initializeHostSession() {
        final UserDTO hostDetails = LocalStorageHandler.getSessionUser(getApplicationContext(), Config.SESSION_FILE_NAME);
        SessionUserDTO hostSessionUser = new SessionUserDTO();
        hostSessionUser.setUser(hostDetails);
        MeetupSessionDTO meetupSessionDTO = new MeetupSessionDTO();
        meetupSessionDTO.setHost(hostSessionUser);
        SessionApi.handleCreateMeetupSession(meetupSessionDTO, getApplicationContext(), new SessionListeners.CreateMeetupSessionListener() {
            @Override
            public void onMeetupSessionCreateResponse(final MeetupSessionDTO meetupSessionDetails) {
                sessionDetails = meetupSessionDetails;
                mRoomCodeView.setText(meetupSessionDetails.getSessionCode());
                emitSocketOnJoinEvent(socket, hostDetails, meetupSessionDetails);
                loadSessionUserList(meetupSessionDetails.getSessionId());
                mLaunchMapsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        emitSocketOnHostStartEvent(socket, meetupSessionDetails);
                    }
                });
            }
        });
    }

    private void startSocketListener(Socket socket) {
        socket.on(SocketHandler.Event.Client.ON_USER_JOIN, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                final String sessionId = (String) args[0];
                loadSessionUserList(sessionId);
            }
        });

        socket.on(SocketHandler.Event.Client.ON_USER_DISCONNECT, new Emitter.Listener() {
            @Override
                public void call(Object... args) {
                    Log.d("Session", "User disconnected");
                }
        });

        socket.on(SocketHandler.Event.Client.ON_HOST_START, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String sessionId = (String) args[0];
                SessionApi.handleGetMeetupSessionBySessionId(sessionId, getApplicationContext(), new SessionListeners.GetMeetupSessionListener() {
                    @Override
                    public void onMeetupSessionRequestResponse(MeetupSessionDTO meetupSessionDetails) {
                        LocalStorageHandler.storeSessionDetails(getApplicationContext(), Config.SESSION_FILE_NAME, meetupSessionDetails);
                        Intent intent = new Intent(MeetupSessionRoomActivity.this, MapsActivity.class);
                        ActivityTransitionHelper.displayActivity(intent, false, getApplicationContext());
                    }
                });
            }
        });

        socket.on(SocketHandler.Event.Client.ON_USER_KICKED, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String userId = (String) args[0];
                if (userId.equals(userDetails.getUserId()) || userId.equals(sessionDetails.getHost().getUser().getUserId())) {
                    finish();
                }
            }
        });
    }

    private void emitSocketOnJoinEvent(Socket socket, UserDTO userDetails, MeetupSessionDTO sessionDetails) {
        socket.emit(SocketHandler.Event.Server.ON_USER_JOIN, userDetails.getFirstName(), sessionDetails.getSessionId());
    }

    private void emitSocketOnHostStartEvent(Socket socket, MeetupSessionDTO meetupSessionDTO) {
        socket.emit(SocketHandler.Event.Server.ON_HOST_START, meetupSessionDTO.getSessionId());
    }

    private void loadSessionUserList(String sessionId) {

        SessionApi.handleGetMeetupSessionBySessionId(sessionId, getApplicationContext(), new SessionListeners.GetMeetupSessionListener() {
            @Override
            public void onMeetupSessionRequestResponse(MeetupSessionDTO meetupSessionDetails) {
                ArrayList<SessionUserDTO> userList = new ArrayList<>(Arrays.asList(meetupSessionDetails.getUsers()));
                mAdapter = new FriendListAdapter(userList, meetupSessionDetails, meetupSessionDetails.getHost(), SessionUtil.isHost(userDetails, meetupSessionDetails));
                mSessionUserListView.setAdapter(mAdapter);
            }
        });
    }
}
