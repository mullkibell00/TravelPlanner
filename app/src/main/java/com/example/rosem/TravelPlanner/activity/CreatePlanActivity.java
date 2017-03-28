package com.example.rosem.TravelPlanner.activity;

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.fragment.HotelRecommendFragment;
import com.example.rosem.TravelPlanner.fragment.InputHotelInfoFragment;
import com.example.rosem.TravelPlanner.fragment.InputPlanInfoFragment;
import com.example.rosem.TravelPlanner.fragment.InputSiteFragment;
import com.example.rosem.TravelPlanner.fragment.InputTitleFragment;
import com.example.rosem.TravelPlanner.fragment.SchedulingFragment;
import com.example.rosem.TravelPlanner.object.Schedule;
import com.example.rosem.TravelPlanner.object.Site;
import com.example.rosem.TravelPlanner.object.Time;
import com.example.rosem.TravelPlanner.plan.Plan;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by rosem on 2017-02-25.
 */

public class CreatePlanActivity extends AppCompatActivity {

    Typeface fontType;
    TextView title;
    Button nextButton;
    Button prevButton;
    private FrameLayout container;
    int iconColor;
    PorterDuff.Mode iconMode;
    Realm db;

    private Schedule schedule = new Schedule();

    private int STEP_NUM;
    Fragment [] stepFragments;
    private int currentStep = 0;
    private static final int NEXT_STEP = 112;
    private static final int PREV_STEP = 113;
    private int HOTEL_RECOMMEND;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        fontType = Typeface.createFromAsset(getAssets(),getString(R.string.font_name));
        Toolbar titleBar = (Toolbar)findViewById(R.id.create_plan_toolbar);
        title = (TextView)titleBar.findViewById(R.id.create_plan_title);
        title.setTypeface(fontType);

        iconColor = ContextCompat.getColor(this,R.color.colorLightButton);
        iconMode = PorterDuff.Mode.SRC_IN;
        STEP_NUM = this.getResources().getInteger(R.integer.create_plan_steps);
        HOTEL_RECOMMEND = getResources().getInteger(R.integer.create_plan_hotel_recommend);

        container = (FrameLayout)findViewById(R.id.container);

        nextButton = (Button)findViewById(R.id.create_plan_next);
        prevButton = (Button)findViewById(R.id.create_plan_prev);

        db = Realm.getDefaultInstance();

        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {

                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .build();
        stepFragments = new Fragment[STEP_NUM];
        stepFragments[0] = InputTitleFragment.newInstance();
        stepFragments[1] = InputPlanInfoFragment.newInstance();
        stepFragments[2] = InputHotelInfoFragment.newInstance();
        stepFragments[3] = InputSiteFragment.newInstance();
        stepFragments[4] = HotelRecommendFragment.newInstance();
        stepFragments[5] = SchedulingFragment.newInstance();

        changeStep(getSupportFragmentManager(),currentStep);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(db!=null)
        {
            db.close();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
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

    public void changeStep(FragmentManager fm, int order)
    {
        Fragment selected = null;

        //checking order
        if(order==currentStep)
        {
            //do nothing
        }
        else
        {
            if(order==NEXT_STEP)
            {
                currentStep++;
            }
            else
            {
                currentStep--;
            }
        }

        //disable button / able button
        if(currentStep==0)
        {
            prevButton.setVisibility(View.INVISIBLE);
        }
        else if(currentStep==STEP_NUM)
        {
            currentStep--;
        }
        else if(currentStep==HOTEL_RECOMMEND)
        {
            if(schedule.isHotelReserved())
            {
                if(order==NEXT_STEP)
                {
                    moveNext();
                }
                else if(order==PREV_STEP)
                {
                    movePrev();
                }
                return;
            }
        }
        else
        {
            prevButton.setVisibility(View.VISIBLE);
        }

        fm.beginTransaction().replace(R.id.container,stepFragments[currentStep]).commit();
    }

    public void moveNext()
    {
        changeStep(getSupportFragmentManager(),NEXT_STEP);
    }
    public void movePrev()
    {
        changeStep(getSupportFragmentManager(),PREV_STEP);
    }


    public Typeface getFontType()
    {
        return fontType;
    }

    public void setTimeText(TextView view, int hour, int min)
    {
        view.setText(hour+"시 "+min+"분");
    }

    public void setTimerText(TextView view, int hour, int min)
    {
        if(hour==0)
        {
            view.setText(min+"분");
        }
        else
        {
            view.setText(hour+"시간 "+min+"분");;
        }
    }

    public void setDateText(TextView view, int year, int month, int day)
    {
        view.setText(year+"년 "+(month+1)+"월 "+day+"일");
    }

    //about schedule class
    public Calendar getArrived() {
        return schedule.getArrived();
    }

    public void setArrived(Calendar arrived) {
        schedule.setArrived(arrived);
    }

    public String getCountry() {
        return schedule.getCountry();
    }

    public void setCountry(String country) {
        schedule.setCountry(country);
    }

    public Calendar getDeparture() {
        return schedule.getDeparture();
    }

    public void setDeparture(Calendar departure) {
        schedule.setDeparture(departure);;
    }

    public ArrayList<Site> getHotel() {
        return schedule.getHotel();
    }

    public void setHotel(ArrayList<Site> hotel) {
        schedule.setHotel(hotel);
    }

    public boolean isHotelReserved() {
        return schedule.isHotelReserved();
    }

    public void setHotelReserved(boolean hotelReserved) {
        schedule.setHotelReserved(hotelReserved);
    }

    public int getNumOfDays() {
        return schedule.getNumOfDays();
    }

    public void setNumOfDays(int numOfDays) {
        schedule.setNumOfDays(numOfDays);
    }

    public ArrayList<Site> getSiteList() {
        return schedule.getSiteList();
    }

    public void setSiteList(ArrayList<Site> siteList) {
        schedule.setSiteList(siteList);
    }

    public String getPlanName() {
        return schedule.getPlanName();
    }

    public void setPlanName(String planName) {
        schedule.setPlanName(planName);
    }

    public Time getTourEnd() {
        return schedule.getTourEnd();
    }

    public void setTourEnd(Time tourEnd) {
        schedule.setTourEnd(tourEnd);
    }

    public Time getTourStart() {
        return schedule.getTourStart();
    }

    public void setTourStart(Time tourStart) {
        schedule.setTourStart(tourStart);
    }

    public void addSite(Site p)
    {
        schedule.addSite(p);
    }

    public int getScheduleSize()
    {
        return schedule.getScheduleSize();
    }


    public ArrayList<Calendar> getCheckInList() {
        return schedule.getCheckInList();
    }

    public void setCheckInList(ArrayList<Calendar> checkInList) {
        schedule.setCheckInList(checkInList);
    }

    public ArrayList<Calendar> getCheckOutList() {
        return schedule.getCheckOutList();
    }

    public void setCheckOutList(ArrayList<Calendar> checkOutList) {
        schedule.setCheckOutList(checkOutList);
    }

    public ArrayList<Site> getRecommendHotelList()
    {
        return schedule.getRecommendHotelList();
    }

    public void setRecommendHotelList(ArrayList<Site> list)
    {
        schedule.setRecommendHotelList(list);
    }

    public Site setSiteFromPlace(Place place)
    {
        Site site = new Site();

        site.setLat(place.getLatLng().latitude);
        site.setPlaceName(place.getName().toString());
        site.setPlaceType(place.getPlaceTypes());
        site.setAddress(place.getAddress().toString());
        site.setLng(place.getLatLng().longitude);
        site.setPlaceId(place.getId());

        return site;
    }
}
