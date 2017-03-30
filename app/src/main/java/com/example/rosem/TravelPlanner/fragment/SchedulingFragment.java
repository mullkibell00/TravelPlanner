package com.example.rosem.TravelPlanner.fragment;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.activity.CreatePlanActivity;
import com.example.rosem.TravelPlanner.object.Site;
import com.example.rosem.TravelPlanner.object.Time;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

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
    ArrayList<Site> hotel = null;
    ArrayList<Site> siteList = new ArrayList<Site>();
    LinkedList<LinkedList<Integer>> resultSchedule = null;

    //using in this fragment
    int totalNodeNum;
    int timeUnit;
    int timeUnitSecond;
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


        timeUnit = getResources().getInteger(R.integer.time_unit);
        timeUnitSecond = timeUnit*60;

        totalNodeNum = siteList.size()+hotel.size();

        timeMatrix = new long[totalNodeNum][totalNodeNum];
        fareMatrix = new long[totalNodeNum][totalNodeNum];

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.plan_schedule,container,false);

        if(resultSchedule==null)
        {
            Scheduling getPlan = new Scheduling();
            progressDialog.show();
            getPlan.run();
        }

        Button nextButton = (Button)getActivity().findViewById(R.id.create_plan_next);
        Button prevButton = (Button)getActivity().findViewById(R.id.create_plan_prev);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resultSchedule!=null)
                {
                    saveData();
                }
                ((CreatePlanActivity)getActivity()).movePrev();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //saveData();
                if(resultSchedule!=null)
                {
                    saveData();
                }
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

    private int hourToUnit(int hour)
    {
        return  (60/timeUnit)*hour;
    }
    private int minuteToUnit(int min)
    {
        return (min/timeUnit);
    }
    private int timeToUnit(Time time)
    {
        return (60/timeUnit)*time.hour+(time.min/timeUnit);
    }

    private Time getTimeDiff(Calendar start, Calendar end)
    {
        Time diff = new Time();
        diff.hour = end.get(Calendar.HOUR_OF_DAY)-start.get(Calendar.HOUR_OF_DAY);
        int minS = start.get(Calendar.MINUTE); int minE = end.get(Calendar.MINUTE);
        if(minE<minS)
        {
            diff.min = minS-minE;
            diff.hour--;
        }
        else
        {
            diff.min = minE-minS;
        }
        return diff;
    }

    private long intToLong(int i)
    {
        return Integer.valueOf(i).longValue();
    }
    private int longToInt(long l)
    {
        return Long.valueOf(l).intValue();
    }

    private String latlngToString(double lat, double lng)
    {
        String str = Double.toString(lat)+","+Double.toString(lng);
        return str;
    }

   private class Scheduling extends Thread
   {
       boolean [] isSelected;
       String headStr = getString(R.string.google_http_matrix_head);
       String tailStr = "&mode=transit&language=ko&key="+getString(R.string.google_http_api_key);
       String requestUrl;
       JSONArray results = new JSONArray();
       @Override
       public void run() {
           //super.run();
           //get fare and duration matrix from google
           int limit = 25;
           String separator="%7C";
           String originData="origins=";
           String destData="&destinations=";

           String originBody =siteList.get(0).getLatLngStr();
           for(int i= 1; i< totalNodeNum; i++)
           {
               originBody+=separator;
               originBody=originBody+siteList.get(i).getLatLngStr();
           }

           for(int i =0; i<totalNodeNum; i++)
           {
               String destBody = siteList.get(i).getLatLngStr();
               requestUrl = headStr+originData+originBody+destData+destBody+tailStr;
               if(!request(requestUrl))
               {
                   //show toast failed
                   return;
               }
           }

           JSONObject inputData = new JSONObject();
           try {
               inputData.put("results",results);
               resultSchedule = ((CreatePlanActivity)getActivity()).getSchedule(timeUnit,inputData);
           } catch (JSONException e) {
               e.printStackTrace();
           }
       }

       public boolean request(String data)
       {
           InputStream inputStream = null;
           BufferedReader rd = null;
           StringBuilder response = new StringBuilder();

           HttpClient httpClient = new DefaultHttpClient();

           try {
               HttpPost httpPost = new HttpPost(data);
               //서버로 전송 & 받아오기
               HttpResponse httpResponse = httpClient.execute(httpPost);
               Log.v("Schedule::Request", "sending success");

               inputStream = httpResponse.getEntity().getContent();
               rd = new BufferedReader(new InputStreamReader(inputStream));
               String line;
               while((line=rd.readLine())!=null)
               {
                   response.append(line);
               }
               Log.v("Schedule::Request","result:"+response.toString());

               //parsing
               JSONObject responseObj = null;
               try
               {
                   responseObj = new JSONObject(response.toString());
                   results.put(responseObj);
               }
               catch (JSONException e)
               {
                   JSONObject empty = new JSONObject();
                   results.put(empty);
                   e.printStackTrace();
                   return false;
               }//end of parsing try/ catch

               //data 처리
           } catch (IOException e) {
               e.printStackTrace();
               Log.v("Schedule::Request", "sending failed");
               return false;
           }

           return true;

       }

   }


}
