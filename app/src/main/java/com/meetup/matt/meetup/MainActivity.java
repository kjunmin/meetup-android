package com.meetup.matt.meetup;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.meetup.matt.meetup.Handlers.LocalStorageHandler;
import com.meetup.matt.meetup.Helpers.ActivityTransitionHelper;
import com.meetup.matt.meetup.Listeners.GetMeetupSessionListener;
import com.meetup.matt.meetup.WebApi.SessionApi;
import com.meetup.matt.meetup.config.Config;
import com.meetup.matt.meetup.dto.MeetupSessionDTO;
import com.meetup.matt.meetup.dto.UserDTO;

public class MainActivity extends AppCompatActivity {

    private TextView mWelcomeTextView;
    private EditText mRoomCodeInputField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRoomCodeInputField = findViewById(R.id.session_code_input_field);
        mWelcomeTextView = findViewById(R.id.welcome_text);

        UserDTO userDetails = getIntent().getParcelableExtra("userDetails");
        LocalStorageHandler.storeSessionUser(getApplicationContext(), Config.SESSION_FILE_NAME, userDetails);

        mWelcomeTextView.setText(String.format("Welcome %s!", userDetails.getFirstName()));

        ImageButton startApplicationButton = findViewById(R.id.launch_application_button);
        startApplicationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                ActivityTransitionHelper.displayActivity(intent, false, getApplicationContext());
            }
        });

        ImageButton launchFriendlistButton = findViewById(R.id.launch_friendlist_button);
        launchFriendlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FriendListActivity.class);
                ActivityTransitionHelper.displayActivity(intent, false, getApplicationContext());
            }
        });

        ImageButton startSessionButton = findViewById(R.id.launch_session_button);
        startSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MeetupSessionRoomActivity.class);
                ActivityTransitionHelper.displayActivity(intent, false, getApplicationContext());
            }
        });

        Button joinSessionButton = findViewById(R.id.join_session_button);
        joinSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleJoinMeetupSession(mRoomCodeInputField.getText().toString());
            }
        });

        mRoomCodeInputField.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(5)});
    }

    private void handleJoinMeetupSession(String sessionCode) {
        SessionApi.handleGetMeetupSessionBySessionCode(sessionCode, getApplicationContext(), new GetMeetupSessionListener() {
            @Override
            public void onMeetupSessionRequestResponse(MeetupSessionDTO meetupSessionDetails) {
                if (meetupSessionDetails != null) {
                    Intent intent = new Intent(MainActivity.this, MeetupSessionRoomActivity.class);
                    intent.putExtra("sessionDetails", meetupSessionDetails);
                    ActivityTransitionHelper.displayActivity(intent, false, getApplicationContext());
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid Room Code", Toast.LENGTH_SHORT).show();
                }
            }
        });
    };

}
