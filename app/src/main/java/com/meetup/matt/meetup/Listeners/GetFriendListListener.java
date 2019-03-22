package com.meetup.matt.meetup.Listeners;

import com.meetup.matt.meetup.dto.UserDTO;

import java.util.ArrayList;

public interface GetFriendListListener {
    public void onDataReceived(ArrayList<UserDTO> fList);
}
