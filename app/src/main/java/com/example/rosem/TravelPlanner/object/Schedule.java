package com.example.rosem.TravelPlanner.object;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Timer;

/**
 * Created by rosem on 2017-03-27.
 */

public class Schedule extends Thread {

    private String planName;
    private int numOfDays = 0;
    private Calendar departure;
    private Calendar arrived;
    private boolean isHotelReserved;
    private ArrayList<Calendar> checkInList = null;
    private ArrayList<Calendar> checkOutList = null;
    private ArrayList<Site> hotel = null;
    private ArrayList<Site> recommendHotelList = null;
    private String country;
    private Time tourStart;
    private Time tourEnd;
    private ArrayList<Site> siteList = new ArrayList<Site>();
    private LinkedList<Site> fixedHourSiteList = new LinkedList<Site>();
    private LinkedList<Site> overHourSiteList = new LinkedList<Site>();
    //for calculation
    private int touringHourInUnit =0;
    private int numOfSites = 0;
    private int numOfHotels = 0;
    private int totalNum = 0;
    private long [][] timeMat = null;
    private int [][] costMat = null;
    private int [][] unitMat= null;
    private int TIMEUNIT = 0;
    private int HOUR_IN_TIMEUNIT=0;
    private boolean [] isSelected= null;

    private Comparator<Site> sortByVisitTimeLate = new Comparator<Site>()
    {

        @Override
        public int compare(Site s1, Site s2) {
            // TODO Auto-generated method stub
            //visitTIme이 늦은 순으로 정렬
            return -(s1.getVisitStart().compareTo(s2.getVisitStart()));
        }

    });

    private Comparator<Site> sortByVisitTimeEarly = new Comparator<Site>()
    {

        @Override
        public int compare(Site s1, Site s2) {
            // TODO Auto-generated method stub
            //visitTime이 빠른 순으로 정렬
            return (s1.getVisitStart().compareTo(s2.getVisitStart()));
        }

    };

    //about schedule class
    public Calendar getArrived() {
        return this.arrived;
    }

    public void setArrived(Calendar arrived) {
        this.arrived = arrived;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Calendar getDeparture() {
        return this.departure;
    }

    public void setDeparture(Calendar departure) {
        this.departure = departure;
    }

    public ArrayList<Site> getHotel() {
        return this.hotel;
    }

    public void setHotel(ArrayList<Site> hotel) {
        this.hotel = hotel;
    }

    public boolean isHotelReserved() {
        return this.isHotelReserved;
    }

    public void setHotelReserved(boolean hotelReserved) {
        this.isHotelReserved = hotelReserved;
    }

    public int getNumOfDays() {
        return this.numOfDays;
    }

    public void setNumOfDays(int numOfDays) {
        this.numOfDays = numOfDays;
    }

    public ArrayList<Site> getSiteList() {
        return this.siteList;
    }

    public void setSiteList(ArrayList<Site> siteList) {
        this.siteList = siteList;
    }

    public String getPlanName() {
        return this.planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Time getTourEnd() {
        return this.tourEnd;
    }

    public void setTourEnd(Time tourEnd) {
        this.tourEnd = tourEnd;
    }

    public Time getTourStart() {
        return this.tourStart;
    }

    public void setTourStart(Time tourStart) {
        this.tourStart = tourStart;
    }

    public void addSite(Site p)
    {
        this.siteList.add(p);
    }

    public int getScheduleSize()
    {
        return this.siteList.size();
    }


    public ArrayList<Calendar> getCheckInList() {
        return this.checkInList;
    }

    public void setCheckInList(ArrayList<Calendar> checkInList) {
        this.checkInList = checkInList;
    }

    public ArrayList<Calendar> getCheckOutList() {
        return this.checkOutList;
    }

    public void setCheckOutList(ArrayList<Calendar> checkOutList) {
        this.checkOutList = checkOutList;
    }

    public ArrayList<Site> getRecommendHotelList()
    {
        return this.recommendHotelList;
    }

    public void setRecommendHotelList(ArrayList<Site> list)
    {
        this.recommendHotelList =list;
    }


    public void getSchedule(int tu, JSONObject json)
    {
        //set datas
        int touringHourInUnit =0;
        numOfSites = siteList.size();
        numOfHotels = hotel.size();
        totalNum = numOfHotels+numOfSites;
        timeMat = new long[totalNum][totalNum];
        costMat = new int[totalNum][totalNum];
        unitMat = new int[totalNum][totalNum];
        TIMEUNIT = tu;
        HOUR_IN_TIMEUNIT = 60/tu;
        isSelected = new boolean[numOfSites];

        try {
            JSONArray response = json.getJSONArray("results");
            //set matrix
            for(int i =0; i<response.length();i++)
            {
                JSONObject obj = response.getJSONObject(i);
                JSONArray rows = obj.getJSONArray("rows");
                for(int j = 0; j<rows.length();j++)
                {
                    JSONObject rowObj = rows.getJSONObject(j);
                    JSONArray elements = rowObj.getJSONArray("elements");
                    JSONObject elementObj = elements.getJSONObject(0);
                    JSONObject duration = elementObj.getJSONObject("duration");

                    timeMat[j][i] = duration.getLong("value");
                    //j = dest
                    // i = start
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.run();
    }

    @Override
    public void run() {
        //super.run();
    }

    private Time durationToTime(long duration) {
        Time t = new Time();
        t.hour = Long.valueOf(duration / 3600).intValue();
        t.min = Long.valueOf((duration - (t.hour * 3600)) / 60).intValue();
        return t;
    }

    private int hourToUnit(int hour) {
        return (hour * 60) / TIMEUNIT;
    }

    private int hourToUnit(Time t) {
        return (t.hour * 60) / TIMEUNIT;
    }

    private int minToUnit(int min) {
        int unit = min / TIMEUNIT;
        if (min % TIMEUNIT != 0) {
            if(unit>=0)
            {
                unit++;
            }
            else
            {
                unit--;
            }
        }
        return unit;
    }

    private int minToUnit(Time t) {
        int unit = t.min / TIMEUNIT;
        if (t.min % TIMEUNIT != 0) {
            if(unit>=0)
            {
                unit++;
            }
            else
            {
                unit--;
            }
        }
        return unit;
    }

    private int timeToUnit(Time t) {
        return (hourToUnit(t.hour)) + minToUnit(t.min);
    }
    private Time unitToTime(int unit)
    {
        Time t = new Time();
        while(unit > HOUR_IN_TIMEUNIT)
        {
            t.hour++;
            unit -= HOUR_IN_TIMEUNIT;
        }
        t.min = unit*TIMEUNIT;
        return t;
    }

    private Time getTimeDiff(Time t1, Time t2)
    {
        return t1.sub(t2);
    }



    class CourseCostResult
    {
        public int cost;
        public int timeUnit;

        public CourseCostResult()
        {
            cost = 0;
            timeUnit=0;
        }

        public CourseCostResult(int c, int t)
        {
            cost = c;
            timeUnit= t;
        }
    }
}
