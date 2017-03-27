package com.example.rosem.TravelPlanner.object;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

/**
 * Created by rosem on 2017-03-27.
 */

public class Schedule {

    public String planName;
    public int numOfDays = 0;
    public Calendar depature;
    public Calendar arrived;
    public boolean isHotelReserved;
    public ArrayList<Calendar> checkInList = null;
    public ArrayList<Calendar> checkOutList = null;
    public ArrayList<Site> hotel = null;
    public ArrayList<Site> recommendHotelList = null;
    public String country;
    public Calendar tourStart;
    public Calendar tourEnd;
    public ArrayList<Site> siteList = new ArrayList<Site>();
    public LinkedList<Site> fixedHourSiteList = new LinkedList<Site>();
    public LinkedList<Site> overHourSiteList = new LinkedList<Site>();
    //for calculation
    private int touringHourInUnit =0;
    private int numOfSites = 0;
    private int numOfHotels = 0;
    private int totalNum = 0;
    private long [][] timeMat;
    private int [][] costMat;
    private int [][] unitMat;
    private int TIME_UNIT = 0;

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

    public Calendar getDepature() {
        return this.depature;
    }

    public void setDepature(Calendar depature) {
        this.depature = depature;
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

    public Calendar getTourEnd() {
        return this.tourEnd;
    }

    public void setTourEnd(Calendar tourEnd) {
        this.tourEnd = tourEnd;
    }

    public Calendar getTourStart() {
        return this.tourStart;
    }

    public void setTourStart(Calendar tourStart) {
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
}
