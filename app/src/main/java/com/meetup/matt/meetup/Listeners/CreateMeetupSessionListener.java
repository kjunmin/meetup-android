package com.meetup.matt.meetup.Listeners;

import com.meetup.matt.meetup.dto.MeetupSessionDTO;

public interface CreateMeetupSessionListener {
    public void onMeetupSessionCreateResponse(MeetupSessionDTO meetupSessionDetails);
}
