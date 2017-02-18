package com.example.rosem.TravelPlanner.PlanListShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rosem on 2017-02-18.
 */

public class Plan extends RealmObject {
    int numOfDays;
    String plan;
    @PrimaryKey
    String planName;

    JSONArray planArray;

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
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
        try {
            JSONObject obj = new JSONObject(plan);
            planArray = obj.getJSONArray("plan");
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
        plan = planArray.toString();
    }

    public void addDay(JSONArray courses)
    {
        planArray.put(courses);
    }
}
