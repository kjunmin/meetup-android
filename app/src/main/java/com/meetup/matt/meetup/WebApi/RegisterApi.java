package com.meetup.matt.meetup.WebApi;

import android.content.Context;

import com.meetup.matt.meetup.config.Config;
import com.meetup.matt.meetup.dto.RegistrationDTO;

import java.util.HashMap;
import java.util.Map;

public class RegisterApi {

    private static Map buildRegistrationRequestObject(RegistrationDTO registrationDTO) {
        final Map<String, String> userRegistration = new HashMap<>();
        userRegistration.put("email", registrationDTO.getEmail());
        userRegistration.put("firstname", registrationDTO.getFirstname());
        userRegistration.put("lastname", registrationDTO.getLastname());
        userRegistration.put("password", registrationDTO.getPassword());
        return userRegistration;
    }

    public static void handleRegistration(RegistrationDTO registrationDTO, Context context) {
        String url = Config.REGISTER_URL;


    }
}
