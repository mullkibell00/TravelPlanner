package com.example.rosem.TravelPlanner.object;

import java.util.ArrayList;

/**
 * Created by rosem on 2017-03-09.
 */

public class Local {
    String locality=null;
    ArrayList<Site> siteList;
    int specialVisitTime = 0;
    long totalTourHour = 0;
    long totalTourUnit = 0;
    long [][] costHourMatrix = null;
    long [][] costUnitMatrix = null;

    public Local()
    {
        siteList = new ArrayList<Site>();
    }

    public Local(ArrayList<Site> list)
    {
        if(list!=null)
        {
            siteList = list;
        }
        else
        {
            siteList = new ArrayList<Site>();
        }
    }

    public void addSite(Site s)
    {
        siteList.add(s);
    }

    public void createMatrix()
    {
       int size = siteList.size();
        costHourMatrix = new long [size][size];
        costUnitMatrix = new long [size][size];
    }

    public long[][] getCostHourMatrix() {
        return costHourMatrix;
    }

    public void setCostHourMatrix(long[][] costHourMatrix) {
        this.costHourMatrix = costHourMatrix;
    }

    public long[][] getCostUnitMatrix() {
        return costUnitMatrix;
    }

    public void setCostUnitMatrix(long[][] costUnitMatrix) {
        this.costUnitMatrix = costUnitMatrix;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public ArrayList<Site> getSiteList() {
        return siteList;
    }

    public void setSiteList(ArrayList<Site> siteList) {
        this.siteList = siteList;
    }

    public int getSpecialVisitTime() {
        return specialVisitTime;
    }

    public void setSpecialVisitTime(int specialVisitTime) {
        this.specialVisitTime = specialVisitTime;
    }

    public long getTotalTourHour() {
        return totalTourHour;
    }

    public void setTotalTourHour(long totalTourHour) {
        this.totalTourHour = totalTourHour;
    }

    public long getTotalTourUnit() {
        return totalTourUnit;
    }

    public void setTotalTourUnit(long totalTourUnit) {
        this.totalTourUnit = totalTourUnit;
    }
}
