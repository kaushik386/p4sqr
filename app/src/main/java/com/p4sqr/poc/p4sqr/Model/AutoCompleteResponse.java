package com.p4sqr.poc.p4sqr.Model;



import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AutoCompleteResponse {

    @SerializedName("status")
    String status;
    @SerializedName("predictions")
    List<Predictions> predictions;
    @SerializedName("types")
    List<Types> types;

    public String getStatus() {
        return status;
    }

    public List<Predictions> getPredictions() {
        return predictions;
    }
}

class Types{
    String name;

    public String getName() {
        return name;
    }
}