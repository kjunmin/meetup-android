package com.meetup.matt.meetup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.meetup.matt.meetup.Handlers.LocalStorageHandler;
import com.meetup.matt.meetup.Listeners.CreateMeetupSessionListener;
import com.meetup.matt.meetup.WebApi.SessionApi;
import com.meetup.matt.meetup.config.Config;
import com.meetup.matt.meetup.dto.MeetupSessionDTO;
import com.meetup.matt.meetup.dto.UserDTO;

import org.w3c.dom.Text;

public class MeetupSessionRoomActivity extends AppCompatActivity {

    private TextView mRoomCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup_session_room);
        mRoomCodeView = findViewById(R.id.room_code_textview);
        MeetupSessionDTO meetupSessionDetails = getIntent().getParcelableExtra("sessionDetails");
        if (meetupSessionDetails != null) {
            mRoomCodeView.setText(meetupSessionDetails.getSessionCode());
        } else {
            initializeSession();
        }

    }

    private void initializeSession() {
        UserDTO hostDetails = LocalStorageHandler.getSessionUser(getApplicationContext(), Config.SESSION_FILE_NAME);
        MeetupSessionDTO meetupSessionDTO = new MeetupSessionDTO();
        meetupSessionDTO.setHost(hostDetails);
        SessionApi.handleCreateMeetupSession(meetupSessionDTO, getApplicationContext(), new CreateMeetupSessionListener() {
            @Override
            public void onMeetupSessionCreateResponse(MeetupSessionDTO meetupSessionDetails) {
                mRoomCodeView.setText(meetupSessionDetails.getSessionCode());
            }
        });
    }
}
