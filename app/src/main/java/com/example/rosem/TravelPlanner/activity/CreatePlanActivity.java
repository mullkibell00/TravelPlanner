package com.example.rosem.TravelPlanner.activity;

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.example.rosem.TravelPlanner.fragment.InputPlanInfoFragment;
import com.example.rosem.TravelPlanner.fragment.InputTitleFragment;
import com.example.rosem.TravelPlanner.object.Place;
import com.example.rosem.TravelPlanner.plan.Plan;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

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
    private FrameLayout container;
    int iconColor;
    PorterDuff.Mode iconMode;
    Realm db;

    Schedule schedule = new Schedule();

    private int STEP_NUM;
    Fragment [] stepFragments;
    private int currentStep = 0;
    private static final int NEXT_STEP = 112;
    private static final int PREV_STEP = 113;

    private GoogleApiClient mGoogleApiClient;

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
        STEP_NUM = this.getResources().getInteger(R.integer.create_plan_steps);

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



    //about schedule class
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
