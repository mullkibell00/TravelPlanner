package com.example.rosem.TravelPlanner.plan;

import org.json.JSONArray;
import org.json.JSONException;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by rosem on 2017-02-18.
 */

public class Plan extends RealmObject {
    int numOfDays=0;
    String plan;

    boolean isFavorite;

    @Required
    @PrimaryKey
    String planName;

    @Ignore
    JSONArray planArray;

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public int getNumOfDays() {
        return numOfDays;
    }

    public void setNumOfDays(int numOfDays) {
        this.numOfDays = numOfDays;
    }

    public String getPlan() {
        return planArray.toString();
    }

    public void setPlan(String plan) {
        this.plan = plan;
        try {
            planArray = new JSONArray(plan);
            numOfDays = planArray.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONArray getDay(int i)
    {
        JSONArray day;
        try {
            day = planArray.getJSONArray(i);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return day;
    }

    public  JSONArray getPlanArray()
    {
        return planArray;
    }

    public void setPlanArray(JSONArray arr)
    {
        planArray = arr;
        numOfDays = planArray.length();
    }

    public void addDay(JSONArray courses)
    {
        planArray.put(courses);
        numOfDays++;
    }

    @Override
    public String toString() {
        return planArray.toString();
    }

    public void setPlanFromPlanArray()
    {
        plan = planArray.toString();
    }
}
