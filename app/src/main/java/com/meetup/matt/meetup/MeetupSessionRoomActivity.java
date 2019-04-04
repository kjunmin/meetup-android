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
import com.meetup.matt.meetup.Listeners.CreateMeetupSessionListener;
import com.meetup.matt.meetup.Listeners.GetMeetupSessionListener;
import com.meetup.matt.meetup.WebApi.SessionApi;
import com.meetup.matt.meetup.config.Config;
import com.meetup.matt.meetup.dto.MeetupSessionDTO;
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

        userDetails = LocalStorageHandler.getSessionUser(getApplicationContext(), Config.SESSION_FILE_NAME);

        socket = SocketHandler.createSocketConnection();
        startSocketListener(socket);

        sessionDetails = getIntent().getParcelableExtra("sessionDetails");
        if (sessionDetails != null) {
            Log.d("Session", "FriendJoin");
           initializeFriendSession(sessionDetails);
        } else {
            Log.d("Session", "HostCreate");
            mLaunchMapsButton.setVisibility(View.VISIBLE);
            initializeHostSession();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isHost(userDetails, sessionDetails)) {
            socket.emit(SocketHandler.Event.Server.DELETE_SESSION, sessionDetails.getSessionId());
        } else {
            socket.emit(SocketHandler.Event.Server.DELETE_USER_FROM_SESSION, userDetails.getUserId(), sessionDetails.getSessionId());
        }

        SocketHandler.destroySocketConnection();
    }

    private boolean isHost(UserDTO userDetails, MeetupSessionDTO sessionDetails) {
        return userDetails.getUserId().equals(sessionDetails.getHost().getUserId());
    }

    private void initializeFriendSession(MeetupSessionDTO meetupSessionDetails) {
        mRoomCodeView.setText(meetupSessionDetails.getSessionCode());
        final UserDTO userDetails = LocalStorageHandler.getSessionUser(getApplicationContext(), Config.SESSION_FILE_NAME);

        SessionApi.handleAddUserToMeetupSession(userDetails.getEmail(), meetupSessionDetails.getSessionId(), meetupSessionDetails.getHost().getUserId(), getApplicationContext(), new GetMeetupSessionListener() {
            @Override
            public void onMeetupSessionRequestResponse(MeetupSessionDTO meetupSessionDetails) {
                emitSocketOnJoinEvent(socket, userDetails, meetupSessionDetails);
                loadSessionUserList(meetupSessionDetails);
            }
        });
    };

    private void initializeHostSession() {
        final UserDTO hostDetails = LocalStorageHandler.getSessionUser(getApplicationContext(), Config.SESSION_FILE_NAME);
        MeetupSessionDTO meetupSessionDTO = new MeetupSessionDTO();
        meetupSessionDTO.setHost(hostDetails);
        SessionApi.handleCreateMeetupSession(meetupSessionDTO, getApplicationContext(), new CreateMeetupSessionListener() {
            @Override
            public void onMeetupSessionCreateResponse(final MeetupSessionDTO meetupSessionDetails) {
                sessionDetails = meetupSessionDetails;
                mRoomCodeView.setText(meetupSessionDetails.getSessionCode());
                emitSocketOnJoinEvent(socket, hostDetails, meetupSessionDetails);
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
                Log.d("Session", "User Joined");
            }
        });

        socket.on(SocketHandler.Event.Client.ON_USER_DISCONNECT, new Emitter.Listener() {
            @Override
                public void call(Object... args) {
                    Log.d("Session", "User DCed");
                }
        });

        socket.on(SocketHandler.Event.Client.ON_HOST_START, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String sessionId = (String) args[0];
                SessionApi.handleGetMeetupSessionBySessionId(sessionId, getApplicationContext(), new GetMeetupSessionListener() {
                    @Override
                    public void onMeetupSessionRequestResponse(MeetupSessionDTO meetupSessionDetails) {
                        Intent intent = new Intent(MeetupSessionRoomActivity.this, MapsActivity.class);
                        intent.putExtra("sessiondetails", meetupSessionDetails);
                        ActivityTransitionHelper.displayActivity(intent, false, getApplicationContext());
                    }
                });
            }
        });
    }

    private void emitSocketOnJoinEvent(Socket socket, UserDTO userDetails, MeetupSessionDTO sessionDetails) {
        socket.emit(SocketHandler.Event.Server.ON_USER_JOIN, userDetails.getFirstName(), sessionDetails.getSessionId());
    }

    private void emitSocketOnHostStartEvent(Socket socket, MeetupSessionDTO meetupSessionDTO) {
        socket.emit(SocketHandler.Event.Server.ON_HOST_START, meetupSessionDTO.getSessionId());
    }

    private void loadSessionUserList(MeetupSessionDTO sessionDetails) {
        mSessionUserListView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(MeetupSessionRoomActivity.this);
        mSessionUserListView.setLayoutManager(layoutManager);


        SessionApi.handleGetMeetupSessionBySessionId(sessionDetails.getSessionId(), getApplicationContext(), new GetMeetupSessionListener() {
            @Override
            public void onMeetupSessionRequestResponse(MeetupSessionDTO meetupSessionDetails) {
                ArrayList<UserDTO> userList = new ArrayList<>(Arrays.asList(meetupSessionDetails.getUsers()));
                mAdapter = new FriendListAdapter(userList);
                mSessionUserListView.setAdapter(mAdapter);
            }
        });
    }
}
