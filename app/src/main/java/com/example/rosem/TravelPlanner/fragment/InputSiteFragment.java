package com.example.rosem.TravelPlanner.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.activity.CreatePlanActivity;
import com.example.rosem.TravelPlanner.adapter.SiteListAdapter;
import com.example.rosem.TravelPlanner.object.Site;
import com.example.rosem.TravelPlanner.object.Time;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

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
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by rosem on 2017-03-06.
 */

public class InputSiteFragment extends Fragment {

    Typeface fontType;
    RecyclerView siteListView;
    TextView addSiteButton;
    ArrayList<Site> siteList = null;
    LinkedList<Site> fixedHourSiteList = new LinkedList<Site>();
    LinkedList<Site> overHourSiteList = new LinkedList<Site>();
    LinkedList<Site> fixedDateSiteList = new LinkedList<>();
    SiteListAdapter mAdapter;

    InputInfoDialog siteInfoDialog;
    InputSpendTimeDialog timeDialog;
    boolean isDateSet = false;
    boolean isTimeSet = false;

    private final int PLACE_PICK_REQUEST = 1213;
    private final int SET_PLACE_INFO =1215;

    static public InputSiteFragment newInstance()
    {
        InputSiteFragment fragment = new InputSiteFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fontType = ((CreatePlanActivity)getActivity()).getFontType();

        siteInfoDialog = new InputInfoDialog(getContext());
        timeDialog = new InputSpendTimeDialog(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.plan_input_site,container,false);

        siteListView = (RecyclerView)view.findViewById(R.id.plan_site_list);
        addSiteButton = (TextView)view.findViewById(R.id.plan_site_add);
        addSiteButton.setTypeface(fontType);

        siteList = ((CreatePlanActivity)getActivity()).getSite();
        SiteListAdapter.ShowDialog showEditDialog = new SiteListAdapter.ShowDialog()
        {
            @Override
            public void showDialog(int idx) {
                siteInfoDialog.show(idx);
            }
        };
        SiteListAdapter.ShowDialog showSpendTimeDialog = new SiteListAdapter.ShowDialog() {
            @Override
            public void showDialog(int idx) {
                timeDialog.show(idx);
            }
        };
        if(siteList==null)
        {
            siteList = new ArrayList<Site>();
            //mAdapter = new SiteListAdapter(getContext(),null,showEditDialog);
        }
        //else
        //{
            mAdapter = new SiteListAdapter(getContext(),siteList,showEditDialog, showSpendTimeDialog);
        //};

        siteListView.setAdapter(mAdapter);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        siteListView.setLayoutManager(manager);
        //set divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(),R.drawable.list_line_divider));
        siteListView.addItemDecoration(dividerItemDecoration);

        Button prevButton = (Button)getActivity().findViewById(R.id.create_plan_prev);
        Button nextButton = (Button)getActivity().findViewById(R.id.create_plan_next);

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

        addSiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                    Intent intent = builder.build(getActivity());
                    startActivityForResult(intent,PLACE_PICK_REQUEST);
                }
                catch (GooglePlayServicesRepairableException e)
                {
                    e.printStackTrace();
                }
                catch (GooglePlayServicesNotAvailableException e)
                {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PLACE_PICK_REQUEST)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                Place selectedPlace = PlacePicker.getPlace(getContext(),data);
                Site site = ((CreatePlanActivity)getActivity()).setSiteFromPlace(selectedPlace);

                SendRequest getLocality = new SendRequest(site.getPlaceId());
                String local = getLocality.postData(null);
                site.setLocality(local);

                mAdapter.addSite(site);
                int pos = mAdapter.getIndexOf(site);
                timeDialog.show(pos);

            }
        }
    }

    public void saveData()
    {
        ((CreatePlanActivity)getActivity()).setSite(mAdapter.getSiteList());
        ArrayList<Site> list = mAdapter.getSiteList();
        Iterator<Site> it = list.iterator();
        while(it.hasNext())
        {
            Site site = it.next();
            if(site.getVisitDay()!=null)
            {
                fixedDateSiteList.add(site);
            }
            if(site.getVisitTime()!=null)
            {
                Time time = site.getVisitTime().add(site.getSpendTime());
                Time endTime = ((CreatePlanActivity)getActivity()).getTourEnd();
                if(time.compareTo(endTime)<=0)
                {
                    fixedHourSiteList.add(site);
                }
                else
                {
                    overHourSiteList.add(site);
                }
            }
        }//end of while iteration of list
        ((CreatePlanActivity)getActivity()).setFixedDateSiteList(fixedDateSiteList);
        ((CreatePlanActivity)getActivity()).setFixedHourSiteList(fixedHourSiteList);
        ((CreatePlanActivity)getActivity()).setOverHourSiteList(overHourSiteList);
    }

    private class SendRequest extends AsyncTask<String,String,String>
    {
        String urlString = getString(R.string.google_geocoding_base);

        public SendRequest(String placeId)
        {
            urlString +=placeId;
            urlString+="&language=ko&key=";
            urlString+=getString(R.string.google_http_api_key);
        }
        @Override
        protected String doInBackground(String... strings) {
            postData(urlString);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            Log.v("Main::","start send");
        }

        public String postData(String str)
        {
            InputStream inputStream = null;
            BufferedReader rd = null;
            StringBuilder response = new StringBuilder();

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(urlString);

            try {
                //서버로 전송 & 받아오기
                HttpResponse httpResponse = httpClient.execute(httpPost);
                Log.v("******server", "send msg successed");

                inputStream = httpResponse.getEntity().getContent();
                rd = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line=rd.readLine())!=null)
                {
                    response.append(line);
                }
                Log.v("InputSite::bringSuccess","result:"+response.toString());
                String locality=null;
                //parsing
                try {
                    JSONObject responseObj = new JSONObject(response.toString());
                    JSONArray result = responseObj.getJSONArray("results");
                    JSONObject firstObj = result.getJSONObject(0);
                    JSONArray addrComponent = firstObj.getJSONArray("address_components");
                    for(int i=0; i< addrComponent.length();i++)
                    {
                        JSONObject addr = addrComponent.getJSONObject(i);
                        JSONArray types = addr.getJSONArray("types");
                        if(types.getString(0).equals("locality") && locality==null)
                        {
                            locality = addr.getString("short_name");
                        }
                    }
                    /*
                    if(locality==null)
                    {
                        for(int i=0; i< addrComponent.length();i++)
                        {
                            JSONObject addr = addrComponent.getJSONObject(i);
                            JSONArray types = addr.getJSONArray("types");
                            if(types.getString(0).equals("sublocality"))
                            {
                                locality = addr.getString("short_name");
                            }
                        }
                    }
                    */
                    if(locality==null)
                    {
                        for(int i=0; i< addrComponent.length();i++)
                        {
                            JSONObject addr = addrComponent.getJSONObject(i);
                            JSONArray types = addr.getJSONArray("types");
                            if(types.getString(0).equals("administrative_area_level_2")&& locality==null)
                            {
                                locality = addr.getString("short_name");
                            }
                        }
                    }
                    if(locality==null)
                    {
                        for(int i=0; i< addrComponent.length();i++)
                        {
                            JSONObject addr = addrComponent.getJSONObject(i);
                            JSONArray types = addr.getJSONArray("types");
                            if(types.getString(0).equals("administrative_area_level_1")&& locality==null)
                            {
                                locality = addr.getString("short_name");
                            }
                        }
                    }

                    return locality;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //data 처리

            } catch (IOException e) {
                e.printStackTrace();
                Log.v("******server", "send msg failed");
            }
            return null;
        }
    }

    public class InputInfoDialog extends AlertDialog
    {
        int idx = 0;
        View dialogView;
        View titleView;
        Calendar visitTime = Calendar.getInstance();
        Calendar visitDay = Calendar.getInstance();
        TextView visitDayBtn;
        TextView visitTimeBtn;

        public InputInfoDialog(Context context)
        {
            super(context);
            //set view of dialog
            dialogView = getLayoutInflater().inflate(R.layout.dialog_input_site_info,null);
            titleView = getLayoutInflater().inflate(R.layout.dialog_title,null);
            TextView title = (TextView)titleView.findViewById(R.id.dialog_title_view);
            title.setTypeface(fontType); title.setText(getString(R.string.title_input_site_dialog));
            TextView textVistDay = (TextView)dialogView.findViewById(R.id.dialog_site_txt_visit_day);
            TextView textVisitTime = (TextView)dialogView.findViewById(R.id.dialog_site_txt_visit_time);
            visitDayBtn = (TextView)dialogView.findViewById(R.id.dialog_site_selected_visit_day);
            visitTimeBtn = (TextView)dialogView.findViewById(R.id.dialog_site_selected_visit_time);
            textVistDay.setTypeface(fontType); textVisitTime.setTypeface(fontType);
            visitDayBtn.setTypeface(fontType); visitTimeBtn.setTypeface(fontType);

            //set onClickListener in dialog
            visitDayBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DatePickerDialog(getContext(),new VisitDaySetListener(),
                            visitDay.get(Calendar.YEAR), visitDay.get(Calendar.MONTH),visitDay.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
            visitTimeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new TimePickerDialog(getContext(),new VisitTimeSetListener(),
                            visitTime.get(Calendar.HOUR_OF_DAY), visitTime.get(Calendar.MINUTE),false).show();
                }
            });

            //setTitle(getString(R.string.title_input_site_dialog));
            setCustomTitle(titleView);
            setView(dialogView);

            setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.site_dialog_positive), new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(siteInfoDialog.isShowing())
                    {
                        if(isDateSet)
                        {
                            mAdapter.setVisitDay(idx, visitDay);
                        }
                        if(isTimeSet)
                        {
                            mAdapter.setVisitTime(idx, visitTime);
                        }
                        isDateSet = false;
                        isTimeSet = false;
                        siteInfoDialog.dismiss();
                    }
                }
            });

            setButton(AlertDialog.BUTTON_NEGATIVE,getString(R.string.site_dialog_negative), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(siteInfoDialog.isShowing())
                    {
                        siteInfoDialog.dismiss();
                        isDateSet = false;
                        isTimeSet= false;
                    }
                }
            });

            this.setOnShowListener(new OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    Button posButton = getButton(AlertDialog.BUTTON_POSITIVE);
                    posButton.setTypeface(fontType);

                    Button negButton = getButton(AlertDialog.BUTTON_NEGATIVE);
                    negButton.setTypeface(fontType);
                }
            });

        }

        public void show(int pos) {
            idx = pos;
            this.show();
            Site site = mAdapter.getSite(idx);
            if(site.getVisitTime()!=null)
            {
                setVisitTimeBtn(site.getVisitTime().getCalendar());
            }
            if(site.getVisitDay()!=null)
            {
                setVisitDayBtn(site.getVisitDay());
            }
        }

        public void setVisitDayBtn(Calendar cal)
        {
            visitDayBtn.setSelected(true);
            ((CreatePlanActivity)getActivity()).setDateText(visitDayBtn,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
            //visitDayBtn.setText(cal.get(Calendar.HOUR_OF_DAY)+"시 "+cal.get(Calendar.MINUTE)+"분");
        }
        public void setVisitTimeBtn(Calendar cal)
        {
            visitTimeBtn.setSelected(true);
            ((CreatePlanActivity)getActivity()).setTimeText(visitTimeBtn,cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE));
            //visitTimeBtn.setText(cal.get(Calendar.HOUR_OF_DAY)+"시 "+cal.get(Calendar.MINUTE)+"분");
        }
        private class VisitDaySetListener implements DatePickerDialog.OnDateSetListener
        {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                visitDay.set(Calendar.YEAR,year);
                visitDay.set(Calendar.MONTH, month);
                visitDay.set(Calendar.DAY_OF_MONTH,day);
                setVisitDayBtn(visitDay);
                isDateSet = true;
            }
        }

        private class VisitTimeSetListener implements TimePickerDialog.OnTimeSetListener
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                visitTime.set(Calendar.HOUR_OF_DAY,hour);
                visitTime.set(Calendar.MINUTE,min);
                setVisitTimeBtn(visitTime);
                isTimeSet = true;
            }
        }
    }

    public class InputSpendTimeDialog extends AlertDialog
    {
        int pos = 0;
        View titleView;
        View dialogView;
        Calendar spendTime = Calendar.getInstance();
        TextView timeText;

        protected InputSpendTimeDialog(Context context) {
            super(context);

            dialogView = getLayoutInflater().inflate(R.layout.dialog_input_spend_time,null);
            titleView = getLayoutInflater().inflate(R.layout.dialog_title,null);
            TextView title = (TextView)titleView.findViewById(R.id.dialog_title_view);
            title.setTypeface(fontType); title.setText(getString(R.string.title_input_spend_time_dialog));

            spendTime.set(Calendar.HOUR_OF_DAY,0);
            spendTime.set(Calendar.MINUTE,0);

            TextView textView = (TextView)dialogView.findViewById(R.id.dialog_time_txt);
            textView.setTypeface(fontType);
            timeText = (TextView)dialogView.findViewById(R.id.dialog_time_selected);
            timeText.setTypeface(fontType);
            timeText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new TimePickerDialog(getContext(),new TimeSetListener(),
                            spendTime.get(Calendar.HOUR_OF_DAY),spendTime.get(Calendar.MINUTE),true).show();
                }
            });

            setCustomTitle(titleView);
            setView(dialogView);

            setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.site_dialog_positive), new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(timeDialog.isShowing())
                    {
                        mAdapter.setSpendTime(pos,spendTime);
                        mAdapter.notifyDataSetChanged();
                        timeDialog.dismiss();
                    }
                }
            });

            setButton(AlertDialog.BUTTON_NEGATIVE,getString(R.string.site_dialog_negative), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(timeDialog.isShowing())
                    {
                        timeDialog.dismiss();
                    }
                }
            });

            this.setOnShowListener(new OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    Button posButton = getButton(AlertDialog.BUTTON_POSITIVE);
                    posButton.setTypeface(fontType);

                    Button negButton = getButton(AlertDialog.BUTTON_NEGATIVE);
                    negButton.setTypeface(fontType);
                }
            });

        }

        public void show(Site site)
        {
            this.show();
            this.pos = mAdapter.getIndexOf(site);
        }

        public void show(int pos)
        {
            this.pos = pos;
            this.show();
        }

        private class TimeSetListener implements TimePickerDialog.OnTimeSetListener
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                spendTime.set(Calendar.HOUR_OF_DAY,hour);
                spendTime.set(Calendar.MINUTE,min);
                timeText.setSelected(true);
                ((CreatePlanActivity)getActivity()).setTimerText(timeText,hour,min);
            }
        }
    }
}
