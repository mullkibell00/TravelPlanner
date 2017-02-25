package com.example.rosem.TravelPlanner.activity;

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.object.Place;
import com.example.rosem.TravelPlanner.plan.Plan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.rosem.TravelPlanner.R.mipmap.next;

/**
 * Created by rosem on 2017-02-25.
 */

public class CreatePlanActivity extends AppCompatActivity {

    Typeface fontType;
    TextView title;
    Button nextButton;
    Button prevButton;
    FrameLayout container;
    int iconColor;
    PorterDuff.Mode iconMode;
    Realm db;

    Schedule schedule = new Schedule();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);

        fontType = Typeface.createFromAsset(getAssets(),getString(R.string.font_name));
        Toolbar titleBar = (Toolbar)findViewById(R.id.create_plan_toolbar);
        title = (TextView)titleBar.findViewById(R.id.create_plan_title);
        title.setTypeface(fontType);

        iconColor = ContextCompat.getColor(this,R.color.colorLightButton);
        iconMode = PorterDuff.Mode.SRC_IN;

        container = (FrameLayout)findViewById(R.id.container);

        nextButton = (Button)findViewById(R.id.create_plan_next);
        prevButton = (Button)findViewById(R.id.create_plan_prev);

        //setting icon
        Drawable nextImg = ContextCompat.getDrawable(this, next);
        nextImg.setColorFilter(iconColor,iconMode);
        Drawable prevImg = ContextCompat.getDrawable(this, R.mipmap.prev);
        prevImg.setColorFilter(iconColor,iconMode);
        nextButton.setBackground(nextImg);
        prevButton.setBackground(prevImg);

        db = Realm.getDefaultInstance();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(db!=null)
        {
            db.close();
        }
    }

    public boolean checkPlanName(String string)
    {
        RealmResults<Plan> results = db.where(Plan.class).equalTo("planName",string).findAll();
        if(results.size()>0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public Calendar getArrived() {
        return schedule.arrived;
    }

    public void setArrived(Calendar arrived) {
        schedule.arrived = arrived;
    }

    public Locale getCountry() {
        return schedule.country;
    }

    public void setCountry(Locale country) {
        schedule.country = country;
    }

    public Calendar getDepature() {
        return schedule.depature;
    }

    public void setDepature(Calendar depature) {
        schedule.depature = depature;
    }

    public Place getHotel() {
        return schedule.hotel;
    }

    public void setHotel(Place hotel) {
        schedule.hotel = hotel;
    }

    public boolean isHotelReserved() {
        return schedule.isHotelReserved;
    }

    public void setHotelReserved(boolean hotelReserved) {
        schedule.isHotelReserved = hotelReserved;
    }

    public int getNumOfDays() {
        return schedule.numOfDays;
    }

    public void setNumOfDays(int numOfDays) {
        schedule.numOfDays = numOfDays;
    }

    public ArrayList<Place> getPlaceList() {
        return schedule.placeList;
    }

    public void setPlaceList(ArrayList<Place> placeList) {
        schedule.placeList = placeList;
    }

    public String getPlanName() {
        return schedule.planName;
    }

    public void setPlanName(String planName) {
        schedule.planName = planName;
    }

    public Calendar getTourEnd() {
        return schedule.tourEnd;
    }

    public void setTourEnd(Calendar tourEnd) {
        schedule.tourEnd = tourEnd;
    }

    public Calendar getTourStart() {
        return schedule.tourStart;
    }

    public void setTourStart(Calendar tourStart) {
        schedule.tourStart = tourStart;
    }

    public void addPlace(Place p)
    {
        schedule.placeList.add(p);
    }

    public int getScheduleSize()
    {
        return schedule.placeList.size();
    }


     private class Schedule
    {
        public String planName;
        public int numOfDays = 0;
        public Calendar depature;
        public Calendar arrived;
        public boolean isHotelReserved;
        public Place hotel;
        public Locale country;
        public Calendar tourStart;
        public Calendar tourEnd;
        public ArrayList<Place> placeList = new ArrayList<Place>();
    }
}
