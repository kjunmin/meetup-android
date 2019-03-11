package com.meetup.matt.meetup;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.meetup.matt.meetup.Helpers.ActivityTransitionHelper;
import com.meetup.matt.meetup.dto.UserDTO;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserDTO userDetails = (UserDTO) getIntent().getParcelableExtra("userDetails");

        Toast.makeText(this, userDetails.getFirstName(), Toast.LENGTH_SHORT).show();

        Button test = (Button) findViewById(R.id.btn_start);
        test.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                ActivityTransitionHelper.displayActivity(intent, false, getApplicationContext());
            }
        });
    }



}
