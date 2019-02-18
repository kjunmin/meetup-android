package com.meetup.matt.meetup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btLogin;
    private TextView tvLoginLocked;
    private TextView tvLoginAttemptsLeft;
    private TextView tvRemainingLoginAttempts;
    int remainingLoginAttempts = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void authenticateLogin(View view) {
        if (etUsername.getText().toString())
    }
}