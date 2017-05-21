package com.example.rosem.TravelPlanner.Interface;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by bianca on 2017-05-21.
 */

public interface SavePlanService {
    @Headers( "Content-Type: application/json; charset=utf-8")
    @POST("savePlan/")
    Call<ResponseBody> savePlan(@Body String body);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://52.88.195.16:8080/travelPlanner/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
