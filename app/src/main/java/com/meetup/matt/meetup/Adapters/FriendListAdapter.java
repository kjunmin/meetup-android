package com.meetup.matt.meetup.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meetup.matt.meetup.R;
import com.meetup.matt.meetup.dto.UserDTO;

import java.util.ArrayList;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendListViewHolder> {

    private ArrayList<UserDTO> mDataset;

    public static class FriendListViewHolder extends RecyclerView.ViewHolder {
        TextView flItemUsernameView;
        ImageView flItemAvatarView;

        public FriendListViewHolder(View v) {
            super(v);
            this.flItemUsernameView = (TextView) v.findViewById(R.id.friendlist_item_username);
            this.flItemAvatarView = (ImageView) v.findViewById(R.id.friendlist_item_avatar);
        }
    }

    public FriendListAdapter(ArrayList<UserDTO> dataset) {
        mDataset = dataset;
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
    public void onBindViewHolder(@NonNull FriendListViewHolder holder, int position) {
        TextView flItemUsernameView = holder.flItemUsernameView;
        ImageView flItemAvatarView = holder.flItemAvatarView;

        flItemUsernameView.setText(mDataset.get(position).getFirstName());
        flItemAvatarView.setImageResource(R.drawable.ic_placeholder_avatar_24dp);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
