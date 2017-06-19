package com.example.rosem.TravelPlanner.Interface;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by rosem on 2017-06-19.
 */

public interface GoogleMapService {
    @Headers( "Content-Type: application/json; charset=utf-8")
    @GET("/maps/api/geocode/json")
    Call<ResponseBody> getSiteInfo(@Query("place_id")String placeId, @Query("language")String lang, @Query("key")String key);
    @GET("/maps/api/place/nearbysearch/json")
    Call<ResponseBody> getRecommendHotelInfo(@Query("location")String location, @Query("radius")int radius,
                                             @Query("language")String lang, @Query("types")String type,@Query("key")String key );
    @GET("/maps/api/distancematrix/json")
    Call<ResponseBody> getDistanceMatrix(@Query("mode")String mode, @Query("language")String lang, @Query("key")String key);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
