package com.meetup.matt.meetup.Listeners;

import com.meetup.matt.meetup.dto.SessionUserDTO;

public interface GetSessionUsersListener {
    public void onSessionUsersRequestResponse(SessionUserDTO[] sessionUsers);
}
