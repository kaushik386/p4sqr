package com.p4sqr.poc.p4sqr;

import com.p4sqr.poc.p4sqr.Model.LocationModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetPlacesRecommendation {

        @GET("/photos")
    Call<List<LocationModel>> getAllphotos();
}
