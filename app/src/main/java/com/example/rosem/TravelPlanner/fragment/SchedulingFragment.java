package com.example.rosem.TravelPlanner.Fragment;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.Activity.CreatePlanActivity;
import com.example.rosem.TravelPlanner.course.Course;
import com.example.rosem.TravelPlanner.object.Site;
import com.example.rosem.TravelPlanner.object.Time;
import com.example.rosem.TravelPlanner.plan.Plan;
import com.example.rosem.TravelPlanner.plan.PlanAdapter;

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

import io.realm.Realm;

/**
 * Created by rosem on 2017-03-07.
 */

public class SchedulingFragment extends Fragment {

    private int STEP_NUM;
    String [] messages;

    Typeface fontType;
    ProgressDialog progressDialog;
    ProgressHandler handler;

    //about handler message
    private final int UPDATE_UI = 1231;
    private final int SCHEDULE_DONE = 1466;
    private final int FINISH = 6323;
    private final int START = 3256;

    //var
    ArrayList<Site> hotel = null;
    ArrayList<Site> siteList;
    LinkedList<LinkedList<Integer>> resultSchedule = null;
    Calendar departure = Calendar.getInstance();
    Calendar arrival = Calendar.getInstance();
    Time tourStart;
    Time tourEnd;

    Plan plan = null;
    PlanAdapter mAdapter;

    //using in this fragment
    int numOfSite;
    int numOfHotel;
    int totalNodeNum;
    int timeUnit;
    int hourTimeUnit;
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
        siteList = ((CreatePlanActivity)getActivity()).getSite();
        hotel = ((CreatePlanActivity)getActivity()).getHotel();
        arrival = ((CreatePlanActivity)getActivity()).getArrived();
        departure = ((CreatePlanActivity)getActivity()).getDeparture();
        tourStart = ((CreatePlanActivity)getActivity()).getTourStart();
        tourEnd = ((CreatePlanActivity)getActivity()).getTourEnd();

        STEP_NUM = getResources().getInteger(R.integer.scheduling_steps);
        messages = new String[STEP_NUM];
        messages[0] = getString(R.string.schedule_step_0);
       // messages[1] = getString(R.string.schedule_step_1);
        messages[1] = getString(R.string.schedule_step_2);
        //messages[3] = getString(R.string.schedule_step_3);
        messages[2] = getString(R.string.schedule_step_4);

        progressDialog = new ProgressDialog(getContext());
        handler = new ProgressHandler();

        timeUnit = getResources().getInteger(R.integer.time_unit);
        hourTimeUnit = 60/timeUnit;

        numOfSite = siteList.size();
        numOfHotel = hotel.size();
        totalNodeNum = numOfSite+numOfHotel;

        timeMatrix = new long[totalNodeNum][totalNodeNum];
        fareMatrix = new long[totalNodeNum][totalNodeNum];

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.plan_schedule,container,false);

      //  if(resultSchedule==null)
      //  {
            Scheduling getPlan = new Scheduling();
            handler.sendEmptyMessage(START);
            getPlan.run();
      //  }

        TabLayout tabs=(TabLayout)view.findViewById(R.id.plan_tabs);
        ViewPager pager = (ViewPager)view.findViewById(R.id.plan_pager);
        mAdapter = new PlanAdapter(getChildFragmentManager());
        mAdapter.setCourse(plan);
        pager.setAdapter(mAdapter);

        tabs.setupWithViewPager(pager);


        Button nextButton = (Button)getActivity().findViewById(R.id.create_plan_next);
        Button prevButton = (Button)getActivity().findViewById(R.id.create_plan_prev);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData(-1);
                ((CreatePlanActivity)getActivity()).movePrev();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //saveData();
                saveData(1);
                ((CreatePlanActivity)getActivity()).moveNext();
            }
        });
        return view;
    }

    private void saveData(int order)
    {

        if(order==1 && plan!=null)
        {
            //save plan
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            //만약 코드에서 생성한 객체를 집어넣으려면 copyTo를!
            realm.copyToRealmOrUpdate(plan);
            realm.commitTransaction();
            if(realm!=null)
            {
                realm.close();
            }
        }
       else if(order==0)
        {
            //save plan temporarily
        }
    }

    private void updateList()
    {
        handler.sendEmptyMessage(UPDATE_UI);
       //
        plan = new Plan();
        ArrayList<Site> sites = ((CreatePlanActivity)getActivity()).getSiteList();

        String [][] fareStringMat = ((CreatePlanActivity)getActivity()).getFareStringMat();
        int [][] costMat = ((CreatePlanActivity)getActivity()).getCostMat();

        int totalDay = resultSchedule.size();
        for(int dayIdx =0; dayIdx<totalDay;dayIdx++)
        {
            LinkedList<Integer> daySchedule = resultSchedule.get(dayIdx);
            int courseNum = daySchedule.size();
            JSONArray day = new JSONArray();
            Time presentTime = new Time();

            if(dayIdx == 0 || dayIdx == totalDay)
            {
                if(dayIdx==0)
                {
                    presentTime.hour = arrival.get(Calendar.HOUR_OF_DAY);
                    presentTime.min = arrival.get(Calendar.MINUTE);
                }
                else
                {
                    presentTime.hour = departure.get(Calendar.HOUR_OF_DAY);
                    presentTime.min = departure.get(Calendar.MINUTE);
                }
            }
            else
            {
                presentTime.hour = tourStart.hour;
                presentTime.min = tourStart.min;
            }

            int prevSiteIdx = -1;
            for(int courseIdx = 0; courseIdx<courseNum;courseIdx++)
            {
                Course c = new Course();
                Course travel = null;
                int curSiteIdx = daySchedule.get(courseIdx);
                Site site = sites.get(curSiteIdx);
                Time startTime = new Time();
                Time costTime = new Time();
                Time endTime = null;
                Time freeStartTime = null;
                Time freeEndTime = null;
                Course freeTimeCourse = null;
                String fare = null;

                startTime = presentTime.copyOf();

                if(courseIdx == 0)
                {
                    //do nothing
                }
                else
                {
                    if(costMat!=null)
                    {
                        costTime = unitToTime(costMat[curSiteIdx][prevSiteIdx]);
                        //traveling time setting
                        travel = new Course();
                        travel.setName("Travel Time");
                        travel.setTime(presentTime.toString(), presentTime.add(costTime).toString());
                        travel.setSpendTime(costTime.toStringInText());
                    }
                    if(fareStringMat!=null)
                    {
                        fare = fareStringMat[curSiteIdx][prevSiteIdx];
                        travel.setCostMoney(fare);
                    }
                    if(site.getVisitTime()!=null)
                    {
                        startTime = site.getVisitTime();
                        freeStartTime = presentTime.add(costTime);
                        freeEndTime = startTime.copyOf();
                        if(freeEndTime.compareTo(freeStartTime)==1)
                        {
                            freeTimeCourse = new Course();
                            freeTimeCourse.setName("Free Time");
                            freeTimeCourse.setTime(freeStartTime.toString(), freeEndTime.toString());
                            freeTimeCourse.setSpendTime(freeEndTime.sub(freeStartTime).toStringInText());
                        }
                    }
                    else
                    {
                        startTime = presentTime.add(costTime);
                    }
                }
                endTime = startTime.add(site.getSpendTime());

                if(travel!=null)
                {
                    day.put(travel);
                }
                if(freeTimeCourse!=null)
                {
                    day.put(freeTimeCourse);
                }
                c.setName(site.getPlaceName());
                if(courseIdx == 0 || courseIdx == (courseNum-1))
                {
                    c.setTime(endTime.toString());
                }
                else
                {
                    c.setTime(startTime.toString(), endTime.toString());
                    c.setSpendTime(site.getSpendTime().toStringInText());
                }
                //c.setCostMoney(fare);
                c.setAddr(site.getAddress());
                Log.v("Main:::","course\n"+c.toString());

                day.put(c);
                prevSiteIdx = curSiteIdx;
                //presentTime = presentTime.add(costTime.add(site.getSpendTime()));
                presentTime = endTime.copyOf();
            }
            if(courseNum!=0)
            {
                plan.addDay(day);
            }
        }

        plan.setPlanName(((CreatePlanActivity)getActivity()).getPlanName());
        plan.setFavorite(false);
        plan.setPlanFromPlanArray();
        Log.v("Main:::","plan\n"+plan.toString());

        //update recylerView list
        mAdapter.setCourse(plan);

        handler.sendEmptyMessage(FINISH);
    }

    private int minToUnit(int min) {
        int unit = min / timeUnit;
        if (min % timeUnit != 0) {
            if(unit>=0)
            {
                unit++;
            }
            else
            {
                unit--;
            }
        }
        return unit;
    }

    private int timeToUnit(Time t) {
        return ((t.hour * 60) / timeUnit )+ minToUnit(t.min);
    }
    private Time unitToTime(int unit)
    {
        Time t = new Time();
        while(unit > hourTimeUnit)
        {
            t.hour++;
            unit -= hourTimeUnit;
        }
        t.min = unit*timeUnit;
        return t;
    }

    private Time getTimeDiff(Time t1, Time t2)
    {
        return t1.sub(t2);
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
           for(int i= 1; i< numOfSite ; i++)
           {
               originBody+=separator;
               originBody=originBody+siteList.get(i).getLatLngStr();
           }
           for(int i =0; i< numOfHotel;i++)
           {
               originBody+=separator;
               originBody=originBody+hotel.get(i).getLatLngStr();
           }

           for(int i =0; i<numOfSite; i++)
           {
               String destBody = siteList.get(i).getLatLngStr();
               requestUrl = headStr+originData+originBody+destData+destBody+tailStr;
               if(!request(requestUrl))
               {
                   //show toast failed
                   return;
               }
           }
           for(int i =0; i<numOfHotel; i++)
           {
               String destBody = hotel.get(i).getLatLngStr();
               requestUrl = headStr+originData+originBody+destData+destBody+tailStr;
               if(!request(requestUrl))
               {
                   //show toast failed
                   return;
               }
           }

           handler.sendEmptyMessage(UPDATE_UI);

           JSONObject inputData = new JSONObject();
           try {
               inputData.put("results",results);
               resultSchedule = ((CreatePlanActivity)getActivity()).getSchedule(timeUnit,inputData);
               handler.sendEmptyMessage(SCHEDULE_DONE);
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

    private class ProgressHandler extends Handler
    {
        int step = 0;
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what)
            {
                case START:
                    progressDialog.setMessage(messages[step]);
                    progressDialog.show();
                    break;
                case UPDATE_UI:
                    step++;
                    if(step==STEP_NUM)
                    {
                        step--;
                    }
                    progressDialog.setMessage(messages[step]);
                    progressDialog.invalidateOptionsMenu();
                    break;
                case SCHEDULE_DONE:
                    updateList();
                case FINISH:
                    progressDialog.dismiss();
                    break;
            }
        }
    }
}
