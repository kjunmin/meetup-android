package com.meetup.matt.meetup.Listeners;

import com.meetup.matt.meetup.dto.UserDTO;

public interface LoginListener {

    public void onAuthResponse(boolean response, UserDTO userLoginDetails);
}
