package com.meetup.matt.meetup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.meetup.matt.meetup.Handlers.LocalStorageHandler;
import com.meetup.matt.meetup.Helpers.ActivityTransitionHelper;
import com.meetup.matt.meetup.config.Config;
import com.meetup.matt.meetup.dto.UserDTO;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView mWelcomeTextView = findViewById(R.id.welcome_text);

        UserDTO userDetails = getIntent().getParcelableExtra("userDetails");
        LocalStorageHandler.storeSessionUser(getApplicationContext(), Config.SESSION_FILE_NAME, userDetails);

        mWelcomeTextView.setText(String.format("Welcome %s!", userDetails.getFirstName()));

        ImageButton startApplicationButton = findViewById(R.id.launch_application_button);
        startApplicationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
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
    }



}
