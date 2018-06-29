package com.p4sqr.poc.p4sqr.ExpandableRecycler;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.p4sqr.poc.p4sqr.R;

public class VenueDetailViewHolder extends ChildViewHolder {


    private TextView mVenueCategory;
    private TextView mVenueCity;
    private TextView mVenueState;
    private TextView mVenueCountry;
    /**
     * Default constructor.
     *
     * @param itemView The {@link View} being hosted in this ViewHolder
     */
    public VenueDetailViewHolder(@NonNull View itemView) {
        super(itemView);

        mVenueCategory = itemView.findViewById(R.id.venue_category);
        mVenueCity = itemView.findViewById(R.id.city);
        mVenueState = itemView.findViewById(R.id.state);
        mVenueCountry = itemView.findViewById(R.id.country);
    }


    public void bind(VenueDetail venueDetail)
    {
        mVenueCategory.setText(venueDetail.getCategory());
        mVenueCity.setText(venueDetail.getCity());
        mVenueState.setText(venueDetail.getState());
        mVenueCountry.setText(venueDetail.getCountry());

    }
}
