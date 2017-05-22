package com.example.rosem.TravelPlanner.Interface;

import org.json.JSONArray;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by rosem on 2017-05-22.
 */

public interface GetRecommendService {
    @Headers( "Content-Type: application/json; charset=utf-8")
    @GET("getRecommendList/{country}")
    Call<ResponseBody> getRecommend(@Path("country") String country, @Query("page")int page, @Query("loadNum")int loadNum);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://52.88.195.16:8080/travelPlanner/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
