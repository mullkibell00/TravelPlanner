package com.example.rosem.TravelPlanner.fragment;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.activity.CreatePlanActivity;
import com.example.rosem.TravelPlanner.object.Day;
import com.example.rosem.TravelPlanner.object.Local;
import com.example.rosem.TravelPlanner.object.Site;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by rosem on 2017-03-07.
 */

public class SchedulingFragment extends Fragment {

    private int STEP_NUM;
    String [] messages;

    Typeface fontType;
    ProgressDialog progressDialog;

    //about handler message
    private final int UPDATE_UI = 1231;
    private final int FINISH = 6323;

    //var
    int numOfDays;
    int numOfSite;
    Calendar arrival;
    Calendar depart;
    ArrayList<Calendar> checkInList = null;
    ArrayList<Calendar> checkOutList = null;
    ArrayList<Site> hotel = null;
    Calendar tourStart;
    Calendar tourEnd;
    Time touringHour;
    ArrayList<Site> siteList = new ArrayList<Site>();

    //using in this fragment
    ArrayList<Local> locals = new ArrayList<Local>();
    Day [] schedule;

    int timeUnit;
    int timeUnitSecond;
    int touringHourInUnit;
    long [][] timeMatrix;
    long [][] fareMatrix;

    public static SchedulingFragment newInstance()
    {
        SchedulingFragment fragment = new SchedulingFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fontType = ((CreatePlanActivity)getActivity()).getFontType();

        STEP_NUM = getResources().getInteger(R.integer.scheduling_steps);
        messages = new String[STEP_NUM];
        messages[0] = getString(R.string.schedule_step_0);
        messages[1] = getString(R.string.schedule_step_1);
        messages[2] = getString(R.string.schedule_step_2);
        messages[3] = getString(R.string.schedule_step_3);
        messages[4] = getString(R.string.schedule_step_4);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(messages[0]);

        //set vars
        numOfDays = ((CreatePlanActivity)getActivity()).getNumOfDays();
        arrival = ((CreatePlanActivity)getActivity()).getArrived();
        depart= ((CreatePlanActivity)getActivity()).getDepature();
        checkInList = ((CreatePlanActivity)getActivity()).getCheckInList();
        checkOutList = ((CreatePlanActivity)getActivity()).getCheckOutList();
        hotel = ((CreatePlanActivity)getActivity()).getHotel();
        tourStart= ((CreatePlanActivity)getActivity()).getTourStart();
        tourEnd = ((CreatePlanActivity)getActivity()).getTourEnd();
        siteList = ((CreatePlanActivity)getActivity()).getSiteList();
        schedule = new Day[numOfDays];

        timeUnit = getResources().getInteger(R.integer.time_unit);
        timeUnitSecond = timeUnit*60;
        touringHour = new Time();
        touringHour.hour = tourEnd.get(Calendar.HOUR_OF_DAY)-tourStart.get(Calendar.HOUR_OF_DAY);
        int minS = tourStart.get(Calendar.MINUTE); int minE = tourEnd.get(Calendar.MINUTE);
        if(minE<minS)
        {
            touringHour.min = minS-minE;
            touringHour.hour--;
        }
        else
        {
            touringHour.min = minE-minS;
        }
        touringHourInUnit = (60/timeUnit)*touringHour.hour+(touringHour.min/timeUnit);
        numOfSite = siteList.size();

        timeMatrix = new long[numOfSite][numOfSite];
        fareMatrix = new long[numOfSite][numOfSite];

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.plan_schedule,container,false);




        Button nextButton = (Button)getActivity().findViewById(R.id.create_plan_next);
        Button prevButton = (Button)getActivity().findViewById(R.id.create_plan_prev);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                ((CreatePlanActivity)getActivity()).movePrev();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                ((CreatePlanActivity)getActivity()).moveNext();
            }
        });
        return view;
    }

    private void saveData()
    {
        //save plan
    }

    private Time getTime(long sec)
    {
        Time t = new Time();
        //int hour = 3600;
        //int min = 60;

        t.hour = Long.valueOf(sec/3600).intValue();
        t.min = Long.valueOf((sec-Integer.valueOf(t.hour*3600).longValue())/60).intValue();

        return t;
    }

    private long intToLong(int i)
    {
        return Integer.valueOf(i).longValue();
    }
    private int longToInt(long l)
    {
        return Long.valueOf(l).intValue();
    }

   private class Scheduling extends Thread
   {
       @Override
       public void run() {
           //super.run();
           int limit = 25;
           if(numOfSite>limit)
           {

           }
           else
           {
               String originData;
               String destData;
           }
       }
   }

    class RequestMatrix extends AsyncTask<String,String,String>
    {
        String headStr;
        String tailStr;
        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }

    class Time
    {
        int hour;
        int min;
    }

}
