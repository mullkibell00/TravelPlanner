package com.example.rosem.TravelPlanner.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import java.util.Calendar;

/**
 * Created by rosem on 2017-02-26.
 */

public class InputPlanInfoFragment extends Fragment {

    private final int mTxtArrival = 0;
    private final int mTxtDeparture=1;
    private final int mTxtTravelingCountry=2;
    private final int mSelectedCountry=3;
    private final int mSelectedCountryShowArrival = 4;
    private final int mSelectedCountryShowDepart = 5;
    private final int mSelectedArrivalDate=6;
    private final int mSelectedArrivalTime=7;
    private final int mSelectedDepartDate=8;
    private final int mSelectedDepartTime=9;

    private final int planInfoTextNum=10;
    Typeface fontType;

    private final int PLACE_PICK_REQUEST = 1213;

    TextView[] texts = new TextView[planInfoTextNum];

    private DatePickerDialog.OnDateSetListener arrivalDateSetListener;
    private DatePickerDialog.OnDateSetListener departDateSetListener;
    private TimePickerDialog.OnTimeSetListener arrivalTimeSetListener;
    private TimePickerDialog.OnTimeSetListener departTimeSetListener;

    //current
    int curYear, curMonth, curDay, curHour,curMinute;

    public static InputPlanInfoFragment newInstance()
    {
        InputPlanInfoFragment fragment = new InputPlanInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fontType = ((CreatePlanActivity)getActivity()).getFontType();
        arrivalDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                texts[mSelectedArrivalDate].setText(year+"년 "+month+"월 "+day+"일");
            }
        };
        arrivalTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                texts[mSelectedArrivalTime].setBackgroundColor(Color.TRANSPARENT);
                texts[mSelectedArrivalTime].setTextColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
                texts[mSelectedArrivalTime].setText(hour+"시 "+minute+"분");
            }
        };
        departDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                texts[mSelectedDepartDate].setText(year+"년 "+month+"월 "+day+"일");
            }
        };
        departTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                texts[mSelectedDepartTime].setBackgroundColor(Color.TRANSPARENT);
                texts[mSelectedDepartTime].setTextColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
                texts[mSelectedDepartTime].setText(hour+"시 "+minute+"분");
            }
        };

        Calendar curCalendar = Calendar.getInstance();
        curYear = curCalendar.get(Calendar.YEAR);
        curMonth = curCalendar.get(Calendar.MONTH);
        curDay = curCalendar.get(Calendar.DATE);
        curHour = curCalendar.get(Calendar.HOUR_OF_DAY);
        curMinute = curCalendar.get(Calendar.MINUTE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.plan_input_plan_info,container,false);

        settingTextView(view);


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


        return view;
    }

    public void saveData()
    {

    }

    public void settingTextView(ViewGroup view)
    {
        texts[mTxtTravelingCountry] = (TextView)view.findViewById(R.id.plan_info_txt_travel_country);
        texts[mTxtArrival] = (TextView)view.findViewById(R.id.plan_info_txt_arrival_time);
        texts[mTxtDeparture] = (TextView)view.findViewById(R.id.plan_info_txt_departure_time);
        texts[mSelectedCountry] = (TextView)view.findViewById(R.id.plan_info_txt_selected_country);
        texts[mSelectedCountryShowArrival] = (TextView)view.findViewById(R.id.plan_info_country_name1);
        texts[mSelectedCountryShowDepart] = (TextView)view.findViewById(R.id.plan_info_country_name2);
        texts[mSelectedArrivalDate] = (TextView)view.findViewById(R.id.plan_info_txt_select_arrival_date);
        texts[mSelectedArrivalTime] = (TextView)view.findViewById(R.id.plan_info_txt_select_arrival_time);
        texts[mSelectedDepartDate] = (TextView)view.findViewById(R.id.plan_info_txt_select_departure_date);
        texts[mSelectedDepartTime] = (TextView)view.findViewById(R.id.plan_info_txt_select_departure_time);

        for(int i =0; i<planInfoTextNum;i++)
        {
            texts[i].setTypeface(fontType);
        }

        //set onClickListener
        texts[mSelectedCountry].setOnClickListener(new View.OnClickListener() {
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

        texts[mSelectedArrivalDate].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(),arrivalDateSetListener,curYear,curMonth,curDay).show();
            }
        });
        texts[mSelectedArrivalTime].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getContext(),arrivalTimeSetListener,curHour,curDay,false).show();
            }
        });
        texts[mSelectedDepartDate].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(),departDateSetListener,curYear,curMonth,curDay).show();
            }
        });
        texts[mSelectedDepartTime].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getContext(),departTimeSetListener,curHour,curDay,false).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PLACE_PICK_REQUEST)
        {
            if(resultCode == getActivity().RESULT_OK)
            {
                Place selectedPlace = PlacePicker.getPlace(getContext(),data);
                SendRequest request = new SendRequest(selectedPlace.getId());
                request.postData(null);
            }
        }
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

        public void postData(String str)
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
                Log.v("PlanInfo::bring success","result:"+response.toString());
                String country=null;
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
                        if(types.getString(0).equals("country"))
                        {
                            country = addr.getString("long_name");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(country!=null)
                {
                    for(int i = mSelectedCountry;i<=mSelectedCountryShowDepart;i++ )
                    {
                        texts[i].setText(country);
                    }
                    texts[mSelectedCountry].setBackgroundColor(Color.TRANSPARENT);
                    texts[mSelectedCountry].setTextColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.v("******server", "send msg failed");
            }
        }
    }
}
