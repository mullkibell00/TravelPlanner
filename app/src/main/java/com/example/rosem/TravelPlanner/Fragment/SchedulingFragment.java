package com.example.rosem.TravelPlanner.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rosem.TravelPlanner.Interface.GoogleMapService;
import com.example.rosem.TravelPlanner.Interface.SavePlanService;
import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.Activity.CreatePlanActivity;
import com.example.rosem.TravelPlanner.course.Course;
import com.example.rosem.TravelPlanner.object.Schedule;
import com.example.rosem.TravelPlanner.object.Site;
import com.example.rosem.TravelPlanner.object.Time;
import com.example.rosem.TravelPlanner.plan.Plan;
import com.example.rosem.TravelPlanner.plan.PlanAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;

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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;

import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by rosem on 2017-03-07.
 */

public class SchedulingFragment extends Fragment {

    private int STEP_NUM;
    String [] messages;

    TextView planInfo;
    TextView planInfoCountry;
    TextView planInfoCostTime;

    Typeface fontType;
    ProgressDialog progressDialog;
    ProgressHandler handler;
    ServerResponseHandler serverHandler;

    //about handler message
    private final int UPDATE_UI = 1231;
    private final int SCHEDULE_DONE = 1466;
    private final int FINISH = 6323;
    private final int START = 3256;
    private final int SERVER_DONE = 1567;
    private String errorMsg;

    //var
    LinkedList<Site> hotel = null;
    ArrayList<Site> siteList;
    LinkedList<LinkedList<Integer>> resultSchedule = null;
    Time tourStart;
    Time tourEnd;
    Time departure;
    Time arrival;

    Plan plan = null;
    PlanAdapter mAdapter;

    //using in this fragment
    int numOfSite;
    int numOfHotel;
    int totalNodeNum;
    int timeUnit;
    int hourTimeUnit;
    //long [][] timeMatrix;
    //long [][] fareMatrix;

    AlertDialog saveDialog;

    private Schedule schedule = Schedule.getInstance();

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
       // messages[1] = getString(R.string.schedule_step_1);
        messages[1] = getString(R.string.schedule_step_2);
        //messages[3] = getString(R.string.schedule_step_3);
        messages[2] = getString(R.string.schedule_step_4);

        progressDialog = new ProgressDialog(getContext());
        handler = new ProgressHandler();
        serverHandler = new ServerResponseHandler();

        timeUnit = getResources().getInteger(R.integer.time_unit);
        hourTimeUnit = 60/timeUnit;

        //timeMatrix = new long[totalNodeNum][totalNodeNum];
        //fareMatrix = new long[totalNodeNum][totalNodeNum];

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getString(R.string.alert_save));
        builder.setNegativeButton(getString(R.string.txt_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton(getString(R.string.txt_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveData();
            }
        });
        saveDialog = builder.create();

        errorMsg = getString(R.string.error);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.plan_schedule,container,false);

        siteList = schedule.getSite();
        tourStart = schedule.getTourStart();
        tourEnd = schedule.getTourEnd();
        hotel = new LinkedList<Site>(schedule.getHotel());
        hotel.addFirst(schedule.getStartPoint());
        hotel.addLast(schedule.getEndPoint());

        numOfSite = siteList.size();
        numOfHotel = hotel.size();
        totalNodeNum = numOfSite+numOfHotel;

      //  if(resultSchedule==null)
      //  {
            GetSchedule getSchedule = new GetSchedule();
            handler.sendEmptyMessage(START);
            getSchedule.execute();
      //  }

        TabLayout tabs=(TabLayout)view.findViewById(R.id.plan_tabs);
        ViewPager pager = (ViewPager)view.findViewById(R.id.plan_pager);
        mAdapter = new PlanAdapter(getChildFragmentManager());
        mAdapter.setCourse(plan);
        pager.setAdapter(mAdapter);

        tabs.setupWithViewPager(pager);

        planInfo = (TextView)view.findViewById(R.id.plan_info);
        planInfo.setText(schedule.getPlanName()+getString(R.string.plan_info_title));
        planInfoCountry = (TextView)view.findViewById(R.id.plan_info_country);
        planInfoCostTime = (TextView)view.findViewById(R.id.plan_info_cost_time);
        planInfo.setTypeface(fontType); planInfoCountry.setTypeface(fontType);
        planInfoCostTime.setTypeface(fontType);
        planInfoCountry.setText(getString(R.string.plan_info_country)+schedule.getCountry());


        Button nextButton = (Button)getActivity().findViewById(R.id.create_plan_next);
        Button prevButton = (Button)getActivity().findViewById(R.id.create_plan_prev);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CreatePlanActivity)getActivity()).movePrev();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //saveData();
                if(plan!=null && !saveDialog.isShowing())
                {
                    saveDialog.show();
                }
            }
        });
        return view;
    }

    private void saveData()
    {
        //realm에 저장
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

        //서버에 전송
        JSONObject body = new JSONObject();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null)
        {
            return;
        }

        //creating body
        try {
            body.put("country",plan.getCountry());
            body.put("planName",plan.getPlanName());
            body.put("numOfDay",plan.getNumOfDays());
            body.put("userId",user.getUid());
            body.put("totalCostTime",plan.getTotalCostTime());
            if(plan.setPlanArrayFromPlan())
            {
                body.put("plan",plan.getPlan());
            }
            else
            {
                Toast.makeText(getContext(),
                        errorMsg+getResources().getInteger(R.integer.error_server_plan_to_json), Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrash.report(e);
            Toast.makeText(getContext(), errorMsg+getResources().getInteger(R.integer.error_server_body_json), Toast.LENGTH_SHORT).show();
        }
        //send to server
        try {
            String strBody = URLEncoder.encode(body.toString(),"UTF-8");
            new SaveToServer(strBody,serverHandler).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            FirebaseCrash.report(e);
            Toast.makeText(getContext(), errorMsg+getResources().getInteger(R.integer.error_server_encoding), Toast.LENGTH_SHORT).show();
        }

    }

    private void updateList()
    {
        //handler.sendEmptyMessage(UPDATE_UI);
       //
        plan = new Plan();
        ArrayList<Site> sites = schedule.getSiteList();

        String [][] fareStringMat = schedule.getFareStringMat();
        int [][] costMat = schedule.getCostMat();
        arrival = schedule.getFirstDayStart();
        departure = schedule.getLastDayEnd();

        int totalDay = resultSchedule.size()-1;
        int totalTransportTime = 0;
        for(int dayIdx =0; dayIdx<=totalDay;dayIdx++)
        {
            LinkedList<Integer> daySchedule = resultSchedule.get(dayIdx);
            int courseNum = daySchedule.size();
            JSONArray day = new JSONArray();
            Time presentTime = new Time();

            if(dayIdx == 0 || dayIdx == totalDay)
            {
                if(dayIdx==0)
                {
                    presentTime = arrival.copyOf();
                }
                else
                {
                    if(departure.compareTo(tourStart)==-1)
                    {
                        //일정 시작시간보다 마지막날 출발 시간이 더 빠른경우
                        presentTime = departure.copyOf();
                        presentTime = presentTime.sub(unitToTime(costMat[daySchedule.getLast()][daySchedule.getFirst()]));
                    }
                    else
                    {
                        presentTime = tourStart.copyOf();
                    }
                }
            }
            else
            {
                presentTime = tourStart.copyOf();
            }

            int prevSiteIdx = -1;
            for(int courseIdx = 0; courseIdx<courseNum;courseIdx++)
            {
                Course c = new Course();
               // Course travel = null;
                int curSiteIdx = daySchedule.get(courseIdx);
                Site site = sites.get(curSiteIdx);
                Time startTime = new Time();
                Time costTime = new Time();
                String costMoney = null;
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
                        totalTransportTime+=costMat[curSiteIdx][prevSiteIdx];
                        costTime = unitToTime(costMat[curSiteIdx][prevSiteIdx]);
                        //traveling time setting
                      //  travel = new Course();
                     //   travel.setName("Travel Time");
                     //   travel.setTime(presentTime.toString(), presentTime.add(costTime).toString());
                     //   travel.setSpendTime(costTime.toStringInText());
                    }
                    if(fareStringMat!=null)
                    {
                        fare = fareStringMat[curSiteIdx][prevSiteIdx];
                      //  travel.setCostMoney(fare);
                    }
                    if(site.getVisitTime()!=null)
                    {
                        startTime = site.getVisitTime();
                        freeStartTime = presentTime.copyOf();
                        freeEndTime = site.getVisitTime().sub(costTime);
                        if(freeEndTime.compareTo(freeStartTime)==1)
                        {
                            freeTimeCourse = new Course();
                            freeTimeCourse.setName(getString(R.string.free_time));
                            freeTimeCourse.setTime(freeStartTime.toString(), freeEndTime.toString());
                            freeTimeCourse.setSpendTime(freeEndTime.sub(freeStartTime).toStringInText());
                            //freeTimeCourse.setCostTime(costTime.toStringInText());
                        }
                    }
                    else
                    {
                        startTime = presentTime.add(costTime);
                    }
                }
                endTime = startTime.add(site.getSpendTime());
/*
                if(travel!=null)
                {
                    day.put(travel);
                }
               */
                if(freeTimeCourse!=null)
                {
                    day.put(freeTimeCourse);
                }
                c.setName(site.getPlaceName());
                if(courseIdx == 0 || courseIdx == (courseNum-1))
                {
                    c.setTime(endTime.toString());
                    if(courseIdx==(courseNum-1))
                    {
                        c.setCostTime(costTime.toStringInText());
                    }
                }
                else
                {
                    c.setTime(startTime.toString(), endTime.toString());
                    c.setSpendTime(site.getSpendTime().toStringInText());
                    c.setCostTime(costTime.toStringInText());
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

        plan.setPlanName(schedule.getPlanName());
        plan.setFavorite(false);
        plan.setCountry(schedule.getCountry());
        plan.setTotalCostTime(unitToTime(totalTransportTime).toStringInText());
        plan.setPlanFromPlanArray();
        Log.v("Main:::","plan\n"+plan.toString());

        //update recylerView list
        mAdapter.setCourse(plan);
        planInfoCostTime.setText(getString(R.string.total_cost_time)+plan.getTotalCostTime());

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
        if(t==null)
        {
            return 0;
        }
        return ((t.hour * 60) / timeUnit )+ minToUnit(t.min);
    }
    private Time unitToTime(int unit)
    {
        Time t = new Time();
        while(unit >= hourTimeUnit)
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

    private class GetSchedule extends AsyncTask<Call<ResponseBody>,Void, Void>
    {
        //boolean [] isSelected;
        int limit = getResources().getInteger(R.integer.google_api_limit);
        String lang = getString(R.string.google_api_language);
        String mode = getString(R.string.google_api_mode);
        String key = getString(R.string.google_http_api_key);
        JSONArray results = new JSONArray();

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            if(!progressDialog.isShowing())
            {
                progressDialog.setMessage(messages[0]);
                progressDialog.show();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            if(progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }
        }

        @Override
        protected Void doInBackground(Call<ResponseBody>... params) {
            String origin = null;
            String dest = null;
            String separator="|";

            //creating param of request
            origin =siteList.get(0).getLatLngStr();
            for(int i= 1; i< numOfSite ; i++)
            {
                origin+=separator;
                origin=origin+siteList.get(i).getLatLngStr();
            }
            for(int i =0; i< numOfHotel;)
            {
                Site s = hotel.get(i);
                origin+=separator;
                origin=origin+s.getLatLngStr();
                i = hotel.lastIndexOf(s)+1;
            }

            for(int i =0; i<numOfSite; i++)
            {
                dest = siteList.get(i).getLatLngStr();
                if(!request(origin,dest))
                {
                    //fail
                    return null;
                }
            }
            for(int i =0; i<numOfHotel;)
            {
                Site s = hotel.get(i);
                dest = s.getLatLngStr();
                if(!request(origin,dest))
                {
                    //fail
                    return null;
                }
                i = hotel.lastIndexOf(s)+1;
            }
            JSONObject inputData = new JSONObject();
            try {
                handler.sendEmptyMessage(UPDATE_UI);
                inputData.put("results",results);
                resultSchedule = schedule.getSchedule(timeUnit,inputData);
                handler.sendEmptyMessage(SCHEDULE_DONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        public boolean request(String origin, String dest)
        {
            String contents = null;
            GoogleMapService service = GoogleMapService.retrofit.create(GoogleMapService.class);
            Call<ResponseBody>  call = service.getDistanceMatrix(origin,dest,mode,lang,key);
            ResponseBody response = null;
            try {
                response = call.execute().body();
            } catch (IOException e)
            {
                e.printStackTrace();
                FirebaseCrash.report(e);
                //Toast.makeText(getContext(), errorMsg+getResources().getInteger(R.integer.error_server_netowrk), Toast.LENGTH_SHORT).show();
                return false;
            }
            if(response!=null)
            {
                try {
                    contents = response.string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                return false;
            }
            if(contents!=null)
            {
                //parsing
                JSONObject responseObj = null;
                try
                {
                    responseObj = new JSONObject(contents);
                    results.put(responseObj);
                    return true;
                }
                catch (JSONException e)
                {
                    JSONObject empty = new JSONObject();
                    results.put(empty);
                    e.printStackTrace();
                    FirebaseCrash.report(e);
                    return false;
                }//end of parsing try/ catch
            }
            return false;
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
                    step=0;
                    break;
                case UPDATE_UI:
                    step++;
                    if(step==STEP_NUM)
                    {
                        step--;
                    }
                    progressDialog.setMessage(messages[step]);
                    break;
                case SCHEDULE_DONE:
                    updateList();
                    break;
                case FINISH:
                    break;
            }
        }
    }

    private class ServerResponseHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what)
            {
                case SERVER_DONE:
                    ((CreatePlanActivity)getActivity()).moveNext();
                    Toast.makeText(getContext(), getString(R.string.save_success), Toast.LENGTH_SHORT).show();
                    Schedule.clear();
            }
        }
    }

    private class SaveToServer extends AsyncTask<Call<ResponseBody>,Void,String>
    {
        String body;
        ServerResponseHandler handler;
        public SaveToServer(String body, ServerResponseHandler handler) {
            super();
            this.body = body;
            this.handler = handler;
        }

        @Override
        protected String doInBackground(Call<ResponseBody>... params) {
            SavePlanService saveService = SavePlanService.retrofit.create(SavePlanService.class);
            Call<ResponseBody>  call = saveService.savePlan(body);
            ResponseBody response = null;
            try {
                 response = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
                FirebaseCrash.report(e);
                Toast.makeText(getContext(), errorMsg+getResources().getInteger(R.integer.error_server_netowrk), Toast.LENGTH_SHORT).show();
            }
            if(response!=null)
            {
                try {
                    return response.string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            if(s!=null)
            {
                Log.v("SERVER_SAVE::",s);
                //Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
            handler.sendEmptyMessage(SERVER_DONE);
        }
    }
}
