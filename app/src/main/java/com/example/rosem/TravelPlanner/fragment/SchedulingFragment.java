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
import com.example.rosem.TravelPlanner.object.Day;
import com.example.rosem.TravelPlanner.object.Local;
import com.example.rosem.TravelPlanner.object.Site;

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

        Scheduling getPlan = new Scheduling();
        getPlan.run();


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

    private String latlngToString(double lat, double lng)
    {
        String str = Double.toString(lat)+","+Double.toString(lng);
        return str;
    }

   private class Scheduling extends Thread
   {
       @Override
       public void run() {
           //super.run();
           //get fare and duration matrix from google
           int limit = 25;
           String separator="%7C";
           String originData="origins=";
           String destData="&destinations=";
           if(numOfSite>limit)
           {

           }
           else
           {
               String bodyData = siteList.get(0).getLatLngStr();
               for(int i =1; i<numOfSite;i++)
               {
                   bodyData+=separator;
                   bodyData=bodyData+siteList.get(i).getLatLngStr();
               }

               RequestMatrix request = new RequestMatrix(originData+bodyData+destData+bodyData,0,0);
               request.run();
           }
       }

   }

    class RequestMatrix extends Thread
    {
        String headStr = getString(R.string.google_http_matrix_head);
        String bodyStr=null;
        String tailStr = "&mode=transit&language=ko&key="+getString(R.string.google_http_api_key);
        String data = null;
        int rowStart; int colStart;
        public RequestMatrix(String str,int rowStart, int colStart)
        {
            if(str!=null)
            {
                bodyStr=str;
            }
            this.rowStart = rowStart;
            this.colStart = colStart;
        }

        @Override
        public void run() {
           // super.run();
            request(rowStart,colStart);
        }

        public void request(int rowStart, int colStart )
        {
            data = headStr+bodyStr+tailStr;

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
                JSONObject result = null;
                JSONArray rows = null;
                try
                {
                    int rowSize = 0;
                    result = new JSONObject(response.toString());
                    if(result!=null && result.has("rows"))
                    {
                        rows = result.getJSONArray("rows");
                        rowSize = rows.length();
                    }
                    for(int row = 0, i =rowStart ; row<rowSize;row++, i++)
                    {
                        JSONObject columns = rows.getJSONObject(row);
                        JSONArray elements = null;

                        int colSize = 0;

                        if(columns!=null&&columns.has("elements"))
                        {
                            elements = columns.getJSONArray("elements");
                            colSize = elements.length();
                        }
                        for(int col= 0, j = colStart; col<colSize;col++,j++)
                        {
                            JSONObject matrixObj = elements.getJSONObject(col);
                            JSONObject fare = null; JSONObject duration = null;
                            if(matrixObj!=null&&matrixObj.has("duration"))
                            {
                                duration = matrixObj.getJSONObject("duration");
                                timeMatrix[i][j] = duration.getLong("value");
                            }
                            if(matrixObj!=null&&matrixObj.has("fare"))
                            {
                                fare = matrixObj.getJSONObject("fare");
                                fareMatrix[i][j] = fare.getInt("fare");
                            }
                        }
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }//end of parsing try/ catch

                //data 처리
            } catch (IOException e) {
                e.printStackTrace();
                Log.v("Schedule::Request", "sending failed");
            }

        }
    }

    class Time
    {
        int hour;
        int min;
    }

}
