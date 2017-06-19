package com.example.rosem.TravelPlanner.Interface;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Headers;

/**
 * Created by rosem on 2017-06-19.
 */

public interface GoogleMapService {
    @Headers( "Content-Type: application/json; charset=utf-8")
    Call<ResponseBody> getSiteInfo();

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
