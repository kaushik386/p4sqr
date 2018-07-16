package com.p4sqr.poc.p4sqr.Services;

import com.p4sqr.poc.p4sqr.Model.AllData;
import com.p4sqr.poc.p4sqr.Model.AutoCompleteResponse;
import com.p4sqr.poc.p4sqr.Model.FourSquareResponse;
import com.p4sqr.poc.p4sqr.Model.VenuePhoto.VenuePhotoResponse;
import com.p4sqr.poc.p4sqr.Model.VenueSearch.VenueSearchResponse;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

       @GET("v2/venues/explore")
       Single<FourSquareResponse> getExploreResult(@Query("client_id") String client_id, @Query("client_secret") String client_code, @Query("v") String date,@Query("ll")String ll,@Query("section") String section,@Query("limit")String limit);


       @GET("v2/venues/search")
       Single<VenueSearchResponse> getSearchResult(@Query("client_id") String client_id, @Query("client_secret") String client_code, @Query("v") String date, @Query("ll")String ll, @Query("query") String section, @Query("limit")String limit);


       @GET("v2/venues/{id}/photos?")
        Single<VenuePhotoResponse> getVenueDetails(@Path("id") String aa, @Query("client_id") String client_id, @Query("client_secret") String client_code, @Query("v") String date,@Query("limit")String limit);

       @GET("v2/venues/{id}/photos?")
        Single<VenuePhotoResponse> getVenuePictures(@Path("id") String aa, @Query("client_id") String client_id, @Query("client_secret") String client_code, @Query("v") String date,@Query("limit")String limit);


}


