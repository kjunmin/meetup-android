package com.meetup.matt.meetup.Listeners;

import com.meetup.matt.meetup.dto.MeetupSessionDTO;
import com.meetup.matt.meetup.dto.SessionUserDTO;

public class SessionListeners {

    public interface CreateMeetupSessionListener {
        void onMeetupSessionCreateResponse(MeetupSessionDTO meetupSessionDetails);
    }

    public interface GetMeetupSessionListener {
        void onMeetupSessionRequestResponse(MeetupSessionDTO meetupSessionDetails);
    }

    public interface GetSessionUserListener {
        public void onSessionUserRequestResponse(SessionUserDTO sessionUser);
    }

    public interface GetSessionUsersListener {
        public void onSessionUsersRequestResponse(SessionUserDTO[] sessionUsers);
    }

}
