package com.p4sqr.poc.p4sqr.Model;

import com.google.gson.annotations.SerializedName;

public class Predictions {

    String description;

    public String getDescription() {
        return description;
    }

    public String getPlaceID() {
        return placeID;
    }

    @SerializedName("place_id")
    String placeID;

}
