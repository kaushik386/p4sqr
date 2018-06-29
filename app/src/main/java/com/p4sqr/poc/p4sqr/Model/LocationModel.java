package com.p4sqr.poc.p4sqr.Model;

import com.google.gson.annotations.SerializedName;

public class LocationModel {

private String place;
private double latitude;
private double longitude;


    public LocationModel(String place, double latitude, double longitude) {
        this.place = place;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationModel() {
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}


