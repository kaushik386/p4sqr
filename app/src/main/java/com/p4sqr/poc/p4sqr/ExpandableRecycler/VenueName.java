package com.p4sqr.poc.p4sqr.ExpandableRecycler;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

public class VenueName implements Parent<VenueDetail> {

    private String mName;
    private  int distance;
    private VenueDetail mVenueDetails;
    private List<VenueDetail> mVenueDetaillist;


    public VenueName(String mName,  VenueDetail mVenueDetails) {
        this.mName = mName;
        this.mVenueDetails = mVenueDetails;
    }

    public VenueName() {
    }
    public void setName(String mName) {
        this.mName = mName;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public VenueDetail getmVenueDetails() {
        return mVenueDetails;
    }

    public void setmVenueDetails(VenueDetail mVenueDetails) {
        this.mVenueDetails = mVenueDetails;
    }

    public VenueDetail getChild() {
        return mVenueDetails;
    }

    public String getName() {
        return mName;
    }

    @Override
    public List<VenueDetail> getChildList() {
        return mVenueDetaillist;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
