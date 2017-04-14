package com.example.rosem.TravelPlanner.course;

import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rosem on 2017-01-30.
 */

public class Course {
    private String mName;
    private String mAddr;
    private String mTime;
    private String mSpendTime;
    private String mCostMoney;
    public static final int START=0;
    public static final int END = 1;

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
    public String getSpendTime()
    {
        return mSpendTime;
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
    public void setTime(String start, String end){ mTime = start+"~"+end; }
    public void setSpendTime(String time)
    {
        mSpendTime = time;
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
            if(courseJson.has("mSpendTime"))
            {
                c.setSpendTime(courseJson.getString("mSpendTime"));
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