package com.example.rosem.TravelPlanner.course;

/**
 * Created by rosem on 2017-01-30.
 */

public class Course {
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

}