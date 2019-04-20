package com.meetup.matt.meetup.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.meetup.matt.meetup.R;
import com.meetup.matt.meetup.Utils.PolylineOptionsUtil;
import com.meetup.matt.meetup.dto.SessionUserDTO;

import java.util.ArrayList;

public class RouteInfoAdapter extends RecyclerView.Adapter<RouteInfoAdapter.RouteInfoViewHolder> {

    private ArrayList<SessionUserDTO> mDataset;

    protected static class RouteInfoViewHolder extends RecyclerView.ViewHolder {
        TextView riItemUsernameView;
        ImageButton riToggleDisplayButton;

        private RouteInfoViewHolder(View v) {
            super(v);
            this.riItemUsernameView = v.findViewById(R.id.routeinfo_username);
            this.riToggleDisplayButton = v.findViewById(R.id.routeinfo_toggle_display);
        }
    }

    public RouteInfoAdapter(ArrayList<SessionUserDTO> dataset) {
        this.mDataset = dataset;
    }


    @NonNull
    @Override
    public RouteInfoAdapter.RouteInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.routeinfo_item, parent, false);
        RouteInfoAdapter.RouteInfoViewHolder vh = new RouteInfoAdapter.RouteInfoViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RouteInfoAdapter.RouteInfoViewHolder holder, final int position) {
        TextView riItemUsernameView = holder.riItemUsernameView;
        ImageButton riItemToggleDisplayButton = holder.riToggleDisplayButton;

        riItemUsernameView.setText(mDataset.get(position).getUser().getFirstName());
        int colorVal = PolylineOptionsUtil.getColourByIndex(position);
        riItemToggleDisplayButton.setBackgroundColor(colorVal);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
