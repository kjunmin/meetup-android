package com.meetup.matt.meetup;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.meetup.matt.meetup.Handlers.LocalStorageHandler;
import com.meetup.matt.meetup.Helpers.ActivityTransitionHelper;
import com.meetup.matt.meetup.config.Config;
import com.meetup.matt.meetup.dto.UserDTO;

public class MainActivity extends AppCompatActivity {

    private TextView mWelcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWelcomeTextView = (TextView) findViewById(R.id.welcome_text);

        UserDTO userDetails = (UserDTO) getIntent().getParcelableExtra("userDetails");
        LocalStorageHandler.storeSessionUser(getApplicationContext(), Config.SESSION_FILE_NAME, userDetails);

        mWelcomeTextView.setText(String.format("Welcome %s!", userDetails.getFirstName()));
//        Toast.makeText(this, userDetails.getFirstName(), Toast.LENGTH_SHORT).show();

        Button startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                ActivityTransitionHelper.displayActivity(intent, false, getApplicationContext());
            }
        });

        Button friendListButton = (Button) findViewById(R.id.friendlist_button);
        friendListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FriendListActivity.class);
                ActivityTransitionHelper.displayActivity(intent, false, getApplicationContext());
            }
        });
    }



}
