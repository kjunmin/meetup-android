package com.meetup.matt.meetup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.meetup.matt.meetup.Handlers.LoginHandler;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEmailView;
    private EditText mFirstnameView;
    private EditText mLastnameView;
    private EditText mPasswordView;
    private EditText mPasswordCheckView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailView = (EditText) findViewById(R.id.register_email);
        mFirstnameView = (EditText) findViewById(R.id.firstname);
        mLastnameView = (EditText) findViewById(R.id.lastname);
        mPasswordView = (EditText) findViewById(R.id.register_password);
        mPasswordCheckView = (EditText) findViewById(R.id.register_password_check);

        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });
    }

    private void resetErrors() {
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mPasswordCheckView.setError(null);
    }

    private void validateFields() {
        String email = mEmailView.getText().toString();
        String firstname = mFirstnameView.getText().toString();
        String lastname = mLastnameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String passwordCheck = mPasswordCheckView.getText().toString();

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
        } else if (!LoginHandler.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
        }

        if (TextUtils.isEmpty(firstname)) {
            mFirstnameView.setError(getString(R.string.error_field_required));
        }

        if (TextUtils.isEmpty(lastname)) {
            mLastnameView.setError(getString(R.string.error_field_required));
        }

        if (TextUtils.isEmpty(password) && !LoginHandler.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
        }

        if (TextUtils.isEmpty(password) && !LoginHandler.isPasswordValid(password)) {
            mPasswordCheckView.setError(getString(R.string.error_invalid_password));
        } else if (!password.equals(passwordCheck)){
            mPasswordCheckView.setError("Passwords do not match");
        }
    }

    private void attemptRegister() {

        resetErrors();
        validateFields();

    }
}
