package com.p4sqr.poc.p4sqr.ExpandableRecycler;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.p4sqr.poc.p4sqr.Model.VenueModel;
import com.p4sqr.poc.p4sqr.R;

public class VenueViewHolder extends ParentViewHolder {

    private TextView mVenueTextView;
    private TextView mVenueDistanceTextView;

    /**
     * Default constructor.
     *
     * @param itemView The {@link View} being hosted in this ViewHolder
     */
    public VenueViewHolder(@NonNull View itemView) {
        super(itemView);
    mVenueTextView= itemView.findViewById(R.id.venue_name);
//    mVenueDistanceTextView =itemView.findViewById(R.id.venue_distance);

    }

    public void bind(VenueName venue) {
        mVenueTextView.setText(venue.getName());
        mVenueDistanceTextView.setText(venue.getDistance());
    }
}
