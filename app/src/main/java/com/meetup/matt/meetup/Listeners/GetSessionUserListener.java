package com.meetup.matt.meetup.Listeners;

import com.meetup.matt.meetup.dto.SessionUserDTO;

public interface GetSessionUserListener {
        public void onSessionUserRequestResponse(SessionUserDTO sessionUser);
}
