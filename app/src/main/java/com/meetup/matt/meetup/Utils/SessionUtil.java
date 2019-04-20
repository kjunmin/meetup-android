package com.meetup.matt.meetup.Utils;

import com.meetup.matt.meetup.dto.MeetupSessionDTO;
import com.meetup.matt.meetup.dto.UserDTO;

public class SessionUtil {

    public static boolean isHost(UserDTO userDetails, MeetupSessionDTO sessionDetails) {
        return userDetails.getUserId().equals(sessionDetails.getHost().getUserId());
    }
}
