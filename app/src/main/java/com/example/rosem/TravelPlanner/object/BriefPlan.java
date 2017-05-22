package com.example.rosem.TravelPlanner.object;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bianca on 2017-05-23.
 */

public class BriefPlan {
    @SerializedName("country")
    private String country;
    @SerializedName("planName")
    private String planName;
    @SerializedName("id")
    private long id;
    @SerializedName("numOfDay")
    private int numOfDay;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumOfDay() {
        return numOfDay;
    }

    public void setNumOfDay(int numOfDay) {
        this.numOfDay = numOfDay;
    }
}
