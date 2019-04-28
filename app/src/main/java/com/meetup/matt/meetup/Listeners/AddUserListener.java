package com.meetup.matt.meetup.Listeners;

import com.meetup.matt.meetup.dto.ResponseDTO;
import com.meetup.matt.meetup.dto.UserDTO;

public interface AddUserListener {

    void onUserAddedResponse(ResponseDTO response);
}
