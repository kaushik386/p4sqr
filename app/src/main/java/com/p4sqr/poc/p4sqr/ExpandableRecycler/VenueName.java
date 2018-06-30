package com.p4sqr.poc.p4sqr.ExpandableRecycler;



public class VenueName  {

    private String mName;
    private  int distance;
    private VenueDetail mVenueDetails;


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


}
