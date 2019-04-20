package com.meetup.matt.meetup.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.meetup.matt.meetup.Handlers.SocketHandler;
import com.meetup.matt.meetup.R;
import com.meetup.matt.meetup.dto.MeetupSessionDTO;
import com.meetup.matt.meetup.dto.UserDTO;

import java.util.ArrayList;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendListViewHolder> {

    private boolean isHost;
    private MeetupSessionDTO sessionDetails;
    private UserDTO host;
    private ArrayList<UserDTO> mDataset;

    protected static class FriendListViewHolder extends RecyclerView.ViewHolder {
        TextView flItemUsernameView;
        ImageView flItemAvatarView;
        ImageButton flRemoveItemButton;

        private FriendListViewHolder(View v) {
            super(v);
            this.flItemUsernameView = v.findViewById(R.id.friendlist_item_username);
            this.flItemAvatarView =  v.findViewById(R.id.friendlist_item_avatar);
            this.flRemoveItemButton = v.findViewById(R.id.delete_item_button);
        }
    }

    public FriendListAdapter(ArrayList<UserDTO> dataset, MeetupSessionDTO sessionDetails, UserDTO hostUser, boolean isHost) {
        this.sessionDetails = sessionDetails;
        this.host = hostUser;
        this.mDataset = dataset;
        this.isHost = isHost;
    }


    @NonNull
    @Override
    public FriendListAdapter.FriendListViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friendlist_item, parent, false);
        FriendListViewHolder vh = new FriendListViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendListViewHolder holder, final int position) {
        TextView flItemUsernameView = holder.flItemUsernameView;
        ImageView flItemAvatarView = holder.flItemAvatarView;
        if (isHost) {

            ImageButton flDeleteItemButton = holder.flRemoveItemButton;
            flDeleteItemButton.setVisibility(View.VISIBLE);
            flDeleteItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SocketHandler.getSocket().emit(SocketHandler.Event.Server.ON_USER_KICKED, mDataset.get(position).getUserId(), sessionDetails.getSessionId());
                    mDataset.remove(position);
                    notifyDataSetChanged();
                }
            });
        }


        flItemUsernameView.setText(mDataset.get(position).getFirstName());
        flItemAvatarView.setImageResource(R.drawable.ic_placeholder_avatar_24dp);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
