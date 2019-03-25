package com.meetup.matt.meetup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.meetup.matt.meetup.Controllers.LoginController;
import com.meetup.matt.meetup.Helpers.ActivityTransitionHelper;
import com.meetup.matt.meetup.Listeners.RegisterListener;
import com.meetup.matt.meetup.WebApi.RegisterApi;
import com.meetup.matt.meetup.dto.RegistrationDTO;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEmailView;
    private EditText mFirstnameView;
    private EditText mLastnameView;
    private EditText mPasswordView;
    private EditText mPasswordCheckView;
    private View mProgressView;
    private View mRegistrationFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailView = findViewById(R.id.register_email);
        mFirstnameView = findViewById(R.id.firstname);
        mLastnameView = findViewById(R.id.lastname);
        mPasswordView = findViewById(R.id.register_password);
        mPasswordCheckView = findViewById(R.id.register_password_check);

        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

        mProgressView = findViewById(R.id.register_progress);
        mRegistrationFormView = findViewById(R.id.register_form);
    }

    private void resetErrors() {
        mEmailView.setError(null);
        mFirstnameView.setError(null);
        mLastnameView.setError(null);
        mPasswordView.setError(null);
        mPasswordCheckView.setError(null);
    }

    private boolean validateFields(RegistrationDTO registrationDTO) {

        if (TextUtils.isEmpty(registrationDTO.getEmail())) {
            mEmailView.setError(getString(R.string.error_field_required));
            return false;
        } else if (!LoginController.isEmailValid(registrationDTO.getEmail())) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            return false;
        }

        if (TextUtils.isEmpty(registrationDTO.getFirstname())) {
            mFirstnameView.setError(getString(R.string.error_field_required));
            return false;
        }

        if (TextUtils.isEmpty(registrationDTO.getLastname())) {
            mLastnameView.setError(getString(R.string.error_field_required));
            return false;
        }

        if (TextUtils.isEmpty(registrationDTO.getPassword()) && !LoginController.isPasswordValid(registrationDTO.getPassword())) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            return false;
        }

        if (TextUtils.isEmpty(registrationDTO.getPasswordCheck()) && !LoginController.isPasswordValid(registrationDTO.getPasswordCheck())) {
            mPasswordCheckView.setError(getString(R.string.error_invalid_password));
            return false;
        } else if (!registrationDTO.getPassword().equals(registrationDTO.getPasswordCheck())){
            mPasswordView.setError("Passwords do not match");
            mPasswordCheckView.setError("Passwords do not match");
            return false;
        }
        return true;
    }

    private void attemptRegister() {
        String email = mEmailView.getText().toString();
        String firstname = mFirstnameView.getText().toString();
        String lastname = mLastnameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String passwordCheck = mPasswordCheckView.getText().toString();

        RegistrationDTO registrationDTO = new RegistrationDTO(email, firstname, lastname, password, passwordCheck);

        resetErrors();
        boolean validFields = validateFields(registrationDTO);

        if(validFields) {
            startRegistrationProcess(registrationDTO);
        }
    }

    private void startRegistrationProcess(RegistrationDTO registrationDTO) {
        showProgress(true);

        RegisterApi.handleRegistration(registrationDTO, getApplicationContext(), new RegisterListener() {
            @Override
            public void onRegisterResponse(boolean isRegisterSuccess, String responseMessage) {
                if (isRegisterSuccess) {
                    showProgress(false);
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    ActivityTransitionHelper.displayActivity(intent, false, getApplicationContext());
                    Toast.makeText(getApplicationContext(), responseMessage, Toast.LENGTH_SHORT).show();
                } else {
                    showProgress(false);
                    Toast.makeText(getApplicationContext(), responseMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mRegistrationFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mRegistrationFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mRegistrationFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

    }
}
