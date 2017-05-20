package com.example.rosem.TravelPlanner.object;

import java.util.Calendar;
import java.util.List;

/**
 * Created by rosem on 2017-02-25.
 */

public class Site {
    private String placeId;
    private String placeName;
    private List<Integer> placeType;
    private String address;
    private double lat;
    private double lng;
    private String locality;
    private Time visitTime=null;//방문 시작 시간
    private Calendar visitDay=null;//방문 종료 시간
    private Time spendTime=null;//소요예정시간

    public Site()
    {

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public List<Integer> getPlaceType() {
        return placeType;
    }

    public void setPlaceType(List<Integer> placeType) {
        this.placeType = placeType;
    }

    public Time getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(Time spendTime) {
        this.spendTime = spendTime;
    }

    public Calendar getVisitDay() {
        return visitDay;
    }

    public void setVisitDay(Calendar visitDay) {
        this.visitDay = visitDay;
    }

    public Time getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Time visitTime) {
        this.visitTime = visitTime;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getLatLngStr()
    {
        String str = Double.toString(lat)+","+Double.toString(lng);
        return str;
    }
}
