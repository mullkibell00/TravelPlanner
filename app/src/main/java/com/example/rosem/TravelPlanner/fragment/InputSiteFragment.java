package com.example.rosem.TravelPlanner.fragment;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.activity.CreatePlanActivity;
import com.example.rosem.TravelPlanner.adapter.SiteListAdapter;
import com.example.rosem.TravelPlanner.object.Site;
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

/**
 * Created by rosem on 2017-03-06.
 */

public class InputSiteFragment extends Fragment {

    Typeface fontType;
    RecyclerView siteListView;
    TextView addSiteButton;
    ArrayList<Site> siteList = null;
    SiteListAdapter mAdapter;

    InputInfoDialog siteInfoDialog;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.plan_input_site,container,false);

        siteListView = (RecyclerView)view.findViewById(R.id.plan_site_list);
        addSiteButton = (TextView)view.findViewById(R.id.plan_site_add);
        addSiteButton.setTypeface(fontType);

        siteList = ((CreatePlanActivity)getActivity()).getSiteList();
        SiteListAdapter.ShowDialog showEditDialog = new SiteListAdapter.ShowDialog()
        {
            @Override
            public void showDialog(int idx) {
                siteInfoDialog.show(idx);
            }
        };
        if(siteList==null)
        {
            siteList = new ArrayList<Site>();
            mAdapter = new SiteListAdapter(getContext(),null,showEditDialog);
        }
        else
        {
            mAdapter = new SiteListAdapter(getContext(),siteList,showEditDialog);
        };

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

            }
        }
    }

    public void saveData()
    {

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
        Calendar visitStartCal = Calendar.getInstance();
        TextView visitStart;
        TextView visitEnd;

        public InputInfoDialog(Context context)
        {
            super(context);
            //set view of dialog
            dialogView = getLayoutInflater().inflate(R.layout.dialog_input_site_info,null);
            TextView textVistStart = (TextView)dialogView.findViewById(R.id.dialog_site_txt_visit_start);
            TextView textVisitEnd = (TextView)dialogView.findViewById(R.id.dialog_site_txt_visit_end);
            visitStart = (TextView)dialogView.findViewById(R.id.dialog_site_selected_visit_start);
            visitEnd = (TextView)dialogView.findViewById(R.id.dialog_site_selected_visit_end);
            textVistStart.setTypeface(fontType); textVisitEnd.setTypeface(fontType);
            visitStart.setTypeface(fontType); visitEnd.setTypeface(fontType);

            //set onClickListener in dialog
            visitStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new TimePickerDialog(getContext(),new VisitStartSetListener(),
                            visitStartCal.get(Calendar.HOUR_OF_DAY),visitStartCal.get(Calendar.MINUTE),false).show();
                }
            });
            visitEnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new TimePickerDialog(getContext(),new VisitEndSetListener(),
                            visitStartCal.get(Calendar.HOUR_OF_DAY),visitStartCal.get(Calendar.MINUTE),false).show();
                }
            });

            setTitle(getString(R.string.title_input_site_dialog));
            setView(dialogView);
            setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.site_dialog_positive), new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(siteInfoDialog.isShowing())
                    {
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
                    }
                }
            });

        }

        public void show(int pos) {
            idx = pos;
            this.show();
            Site site = mAdapter.getSite(idx);
            if(site.getVisitStart()!=null)
            {
                setVisitStart(site.getVisitStart());
            }
            if(site.getVisitEnd()!=null)
            {
                setVisitEnd(site.getVisitEnd());
            }
        }

        public void setVisitStart(Calendar cal)
        {
            visitStart.setSelected(true);
            visitStart.setText(cal.get(Calendar.HOUR_OF_DAY)+"시 "+cal.get(Calendar.MINUTE)+"분");
        }
        public void setVisitEnd(Calendar cal)
        {
            visitEnd.setSelected(true);
            visitEnd.setText(cal.get(Calendar.HOUR_OF_DAY)+"시 "+cal.get(Calendar.MINUTE)+"분");
        }
        private class VisitStartSetListener implements TimePickerDialog.OnTimeSetListener
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                visitStartCal.set(Calendar.HOUR_OF_DAY,hour);
                visitStartCal.set(Calendar.MINUTE,min);
                mAdapter.setVisitStart(idx,visitStartCal);
                visitStart.setSelected(true);
                visitStart.setText(hour+"시 "+min+"분");
            }
        }

        private class VisitEndSetListener implements TimePickerDialog.OnTimeSetListener
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY,hour);
                cal.set(Calendar.MINUTE,min);
                mAdapter.setVisitEnd(idx,cal);
                visitEnd.setSelected(true);
                visitEnd.setText(hour+"시 "+min+"분");
            }
        }
    }
}
