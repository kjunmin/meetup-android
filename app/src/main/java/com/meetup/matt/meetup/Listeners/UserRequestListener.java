package com.meetup.matt.meetup.Listeners;

import com.meetup.matt.meetup.dto.UserDTO;

public interface UserRequestListener {

    public void onUserReqResponse(UserDTO userDetails);
}
