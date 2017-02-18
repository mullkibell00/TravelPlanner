package com.example.rosem.TravelPlanner.course;

import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;

/**
 * Created by rosem on 2017-01-30.
 */

public class Course extends RealmObject{
    private String mName;
    private String mAddr;
    private String mTime;
    private String mCostTime;
    private String mCostMoney;

    public Course()
    {

    }
    public String getName()
    {
        return mName;
    }
    public String getAddr()
    {
        return mAddr;
    }
    public String getTime()
    {
        return mTime;
    }
    public String getCostTime()
    {
        return mCostTime;
    }
    public String getCostMoney()
    {
        return mCostMoney;
    }

    public void setName(String name)
    {
        mName = name;
    }
    public void setAddr(String addr)
    {
        mAddr = addr;
    }
    public void setTime(String time)
    {
        mTime = time;
    }
    public void setCostTime(String time)
    {
        mCostTime = time;
    }
    public void setCostMoney(String money)
    {
        mCostMoney = money;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this,Course.class);
    }

    public static Course getCourseFromDay(int i, JSONArray day)
    {
        try {
            JSONObject courseJson = new JSONObject(day.getString(i));

            Course c = new Course();
            if(courseJson.has("mName"))
            {
                c.setName(courseJson.getString("mName"));
            }
            if(courseJson.has("mAddr"))
            {
                c.setAddr(courseJson.getString("mAddr"));
            }
            if(courseJson.has("mCostMoney"))
            {
                c.setCostMoney(courseJson.getString("mCostMoney"));
            }
            if(courseJson.has("mCostTime"))
            {
                c.setCostTime(courseJson.getString("mCostTime"));
            }
            if(courseJson.has("mTime"))
            {
                c.setTime(courseJson.getString("mTime"));
            }

            return c;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}