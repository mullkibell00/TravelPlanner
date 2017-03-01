package com.example.rosem.TravelPlanner.object;

import java.util.Calendar;

/**
 * Created by rosem on 2017-02-25.
 */

public class Site {
    private String placeId;
    private String placeName;
    private int placeType;
    private String address;
    private double lat;
    private double lng;
    private Calendar visitStart;//방문 시작 시간
    private Calendar visitEnd;//방문 종료 시간
    private Calendar spendTime;//소요예정시간

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

    public int getPlaceType() {
        return placeType;
    }

    public void setPlaceType(int placeType) {
        this.placeType = placeType;
    }

    public Calendar getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(Calendar spendTime) {
        this.spendTime = spendTime;
    }

    public Calendar getVisitEnd() {
        return visitEnd;
    }

    public void setVisitEnd(Calendar visitEnd) {
        this.visitEnd = visitEnd;
    }

    public Calendar getVisitStart() {
        return visitStart;
    }

    public void setVisitStart(Calendar visitStart) {
        this.visitStart = visitStart;
    }
}
