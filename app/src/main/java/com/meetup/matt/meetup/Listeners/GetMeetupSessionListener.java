package com.meetup.matt.meetup.Listeners;

import com.meetup.matt.meetup.dto.MeetupSessionDTO;

public interface GetMeetupSessionListener {
    public void onMeetupSessionRequestResponse(MeetupSessionDTO meetupSessionDetails);
}
