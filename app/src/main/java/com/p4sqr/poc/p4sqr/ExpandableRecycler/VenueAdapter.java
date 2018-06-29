package com.p4sqr.poc.p4sqr.ExpandableRecycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.p4sqr.poc.p4sqr.R;

import java.util.List;

public class VenueAdapter extends ExpandableRecyclerAdapter<VenueName,VenueDetail,VenueViewHolder,VenueDetailViewHolder> {


    /**
     * Primary constructor. Sets up {@link #mParentList} and {@link #mFlatItemList}.
     * <p>
     * Any changes to {@link #mParentList} should be made on the original instance, and notified via
     * {@link #notifyParentInserted(int)}
     * {@link #notifyParentRemoved(int)}
     * {@link #notifyParentChanged(int)}
     * {@link #notifyParentRangeInserted(int, int)}
     * {@link #notifyChildInserted(int, int)}
     * {@link #notifyChildRemoved(int, int)}
     * {@link #notifyChildChanged(int, int)}
     * methods and not the notify methods of RecyclerView.Adapter.
     *
     * @param parentList List of all parents to be displayed in the RecyclerView that this
     *                   adapter is linked to
     */

    private LayoutInflater mInflater;
    public VenueAdapter(@NonNull List<VenueName> parentList, Context context) {
        super(parentList);

        mInflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public VenueViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {

        View venueView = mInflater.inflate(R.layout.venue_layout,parentViewGroup,false);
        return new VenueViewHolder(venueView);
    }

    @NonNull
    @Override
    public VenueDetailViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View detailsView = mInflater.inflate(R.layout.venue_detail_layout,childViewGroup,false);

        return new VenueDetailViewHolder(detailsView);
    }

    @Override
    public void onBindParentViewHolder(@NonNull VenueViewHolder parentViewHolder, int parentPosition, @NonNull VenueName parent) {
        parentViewHolder.bind(parent);
    }

    @Override
    public void onBindChildViewHolder(@NonNull VenueDetailViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull VenueDetail child) {
        childViewHolder.bind(child);
    }
}
