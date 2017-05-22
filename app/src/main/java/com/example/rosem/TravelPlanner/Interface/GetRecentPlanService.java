package com.example.rosem.TravelPlanner.Interface;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by bianca on 2017-05-22.
 */

public interface GetRecentPlanService {
    @Headers( "Content-Type: application/json; charset=utf-8")
    @GET("getRecentPlan")
    Call<ResponseBody> getRecentPlans(@Query("page")int page, @Query("loadNum")int loadNum);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://52.88.195.16:8080/travelPlanner/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
