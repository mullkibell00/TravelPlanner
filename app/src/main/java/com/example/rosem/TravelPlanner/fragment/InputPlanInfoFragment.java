package com.example.rosem.TravelPlanner.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
    private final int mTxtExplain = 3;
    private final int mTxtTourStart =4;
    private final int mTxtTourEnd = 5;
    private final int mSelectedCountry=6;
    private final int mSelectedCountryShowArrival = 7;
    private final int mSelectedCountryShowDepart = 8;
    private final int mSelectedArrivalDate=9;
    private final int mSelectedArrivalTime=10;
    private final int mSelectedDepartDate=11;
    private final int mSelectedDepartTime=12;
    private final int mSelectedTourStart=13;
    private final int mSelectedTourEnd = 14;



    private final int planInfoTextNum=15;
    Typeface fontType;

    private final int PLACE_PICK_REQUEST = 1213;

    TextView[] texts = new TextView[planInfoTextNum];
    private DatePickerDialog.OnDateSetListener arrivalDateSetListener;
    private DatePickerDialog.OnDateSetListener departDateSetListener;
    private TimePickerDialog.OnTimeSetListener arrivalTimeSetListener;
    private TimePickerDialog.OnTimeSetListener departTimeSetListener;

    //current
    int curYear, curMonth, curDay, curHour,curMinute;
    Calendar selectedArrival;
    Calendar selectedDepart;
    int travelingPeriod;

    //newly added(About tour info)
    private TimePickerDialog.OnTimeSetListener tourStartSetListener;
    private TimePickerDialog.OnTimeSetListener tourEndSetListener;

    //data
    Calendar tourStartTime;
    Calendar tourEndTime;

    public static InputPlanInfoFragment newInstance()
    {
        InputPlanInfoFragment fragment = new InputPlanInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fontType = ((CreatePlanActivity)getActivity()).getFontType();
        selectedArrival = Calendar.getInstance();
        selectedDepart = Calendar.getInstance();

        tourStartSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                texts[mSelectedTourStart].setSelected(true);
                texts[mSelectedTourStart].setText(hour+"시 "+minute+"분");
                tourStartTime.set(Calendar.HOUR_OF_DAY,hour);
                tourStartTime.set(Calendar.MINUTE,minute);
            }
        };
        tourEndSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                texts[mSelectedTourEnd].setSelected(true);
                texts[mSelectedTourEnd].setText(hour+"시 "+minute+"분");
                tourEndTime.set(Calendar.HOUR_OF_DAY,hour);
                tourEndTime.set(Calendar.MINUTE,minute);
            }
        };

        arrivalDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                texts[mSelectedArrivalDate].setText(year+"년 "+(month+1)+"월 "+day+"일");
                selectedArrival.set(Calendar.YEAR,year); selectedArrival.set(Calendar.MONTH,month);
                selectedArrival.set(Calendar.DAY_OF_MONTH,day);
            }
        };
        arrivalTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                //texts[mSelectedArrivalTime].setBackgroundColor(Color.TRANSPARENT);
                //texts[mSelectedArrivalTime].setTextColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
                texts[mSelectedArrivalTime].setSelected(true);
                texts[mSelectedArrivalTime].setText(hour+"시 "+minute+"분");
                selectedArrival.set(Calendar.HOUR_OF_DAY,hour);
                selectedArrival.set(Calendar.MINUTE,minute);
            }
        };
        departDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                texts[mSelectedDepartDate].setText(year+"년 "+(month+1)+"월 "+day+"일");
                selectedDepart.set(Calendar.YEAR,year); selectedDepart.set(Calendar.MONTH,month);
                selectedDepart.set(Calendar.DAY_OF_MONTH,day);
            }
        };
        departTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                //texts[mSelectedDepartTime].setBackgroundColor(Color.TRANSPARENT);
                //texts[mSelectedDepartTime].setTextColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
                texts[mSelectedDepartTime].setSelected(true);
                texts[mSelectedDepartTime].setText(hour+"시 "+minute+"분");
                selectedDepart.set(Calendar.HOUR_OF_DAY,hour);
                selectedDepart.set(Calendar.MINUTE,minute);
            }
        };

        Calendar curCalendar = Calendar.getInstance();
        curYear = curCalendar.get(Calendar.YEAR);
        curMonth = curCalendar.get(Calendar.MONTH);
        curDay = curCalendar.get(Calendar.DATE);
        curHour = curCalendar.get(Calendar.HOUR_OF_DAY);
        curMinute = curCalendar.get(Calendar.MINUTE);
        selectedArrival.set(Calendar.YEAR,curYear); selectedArrival.set(Calendar.MONTH,curMonth);
        selectedArrival.set(Calendar.DAY_OF_MONTH,curDay); selectedArrival.set(Calendar.HOUR_OF_DAY,curHour);
        selectedArrival.set(Calendar.MINUTE,curMinute);
        tourStartTime = Calendar.getInstance();
        tourEndTime = Calendar.getInstance();
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
        //numofDays, arrival,departure,country
        ((CreatePlanActivity)getActivity()).setArrived(selectedArrival);
        ((CreatePlanActivity)getActivity()).setDepature(selectedDepart);
        ((CreatePlanActivity)getActivity()).setCountry(texts[mSelectedCountry].getText().toString());
        ((CreatePlanActivity)getActivity()).setTourStart(tourStartTime);
        ((CreatePlanActivity)getActivity()).setTourEnd(tourEndTime);

        travelingPeriod = 0;
        if(selectedArrival.get(Calendar.MONTH)==selectedDepart.get(Calendar.MONTH))
        {
            travelingPeriod = selectedDepart.get(Calendar.DAY_OF_MONTH)-selectedArrival.get(Calendar.DAY_OF_MONTH)+1;
        }
        else
        {
            travelingPeriod = selectedArrival.getActualMaximum(Calendar.DAY_OF_MONTH)-selectedArrival.get(Calendar.DAY_OF_MONTH)+1;
            Calendar temp = Calendar.getInstance();
            for(int i = selectedArrival.get(Calendar.MONTH)+1;i<selectedDepart.get(Calendar.MONTH);i++)
            {
                temp.set(Calendar.MONTH,i);
                travelingPeriod+=temp.getActualMaximum(Calendar.DAY_OF_MONTH);
            }
            travelingPeriod+=selectedDepart.get(Calendar.DAY_OF_MONTH);
        }
        Log.v("PlanInfo:::","travelingPeriod : "+travelingPeriod);
        ((CreatePlanActivity)getActivity()).setNumOfDays(travelingPeriod);
        Log.v("PlanInfo:::","check saved data\nselectedCountry="+((CreatePlanActivity)getActivity()).getCountry());
    }

    public void settingTextView(ViewGroup view)
    {
        texts[mTxtTravelingCountry] = (TextView)view.findViewById(R.id.plan_info_txt_travel_country);
        texts[mTxtArrival] = (TextView)view.findViewById(R.id.plan_info_txt_arrival_time);
        texts[mTxtDeparture] = (TextView)view.findViewById(R.id.plan_info_txt_departure_time);
        texts[mTxtExplain] = (TextView)view.findViewById(R.id.plan_info_explain);
        texts[mTxtTourStart] =(TextView)view.findViewById(R.id.plan_info_txt_tour_start);
        texts[mTxtTourEnd] = (TextView)view.findViewById(R.id.plan_info_txt_tour_end);
        texts[mSelectedCountry] = (TextView)view.findViewById(R.id.plan_info_txt_selected_country);
        texts[mSelectedCountryShowArrival] = (TextView)view.findViewById(R.id.plan_info_country_name1);
        texts[mSelectedCountryShowDepart] = (TextView)view.findViewById(R.id.plan_info_country_name2);
        texts[mSelectedArrivalDate] = (TextView)view.findViewById(R.id.plan_info_txt_select_arrival_date);
        texts[mSelectedArrivalTime] = (TextView)view.findViewById(R.id.plan_info_txt_select_arrival_time);
        texts[mSelectedDepartDate] = (TextView)view.findViewById(R.id.plan_info_txt_select_departure_date);
        texts[mSelectedDepartTime] = (TextView)view.findViewById(R.id.plan_info_txt_select_departure_time);
        texts[mSelectedTourStart]=(TextView)view.findViewById(R.id.plan_info_selected_tour_start);
        texts[mSelectedTourEnd]= (TextView)view.findViewById(R.id.plan_info_selected_tour_end);

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
                new TimePickerDialog(getContext(),arrivalTimeSetListener,curHour,curMinute,false).show();
            }
        });
        texts[mSelectedDepartDate].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(),departDateSetListener,
                        selectedArrival.get(Calendar.YEAR),
                        selectedArrival.get(Calendar.MONTH),selectedArrival.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        texts[mSelectedDepartTime].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getContext(),departTimeSetListener,
                        selectedArrival.get(Calendar.HOUR_OF_DAY),
                        selectedArrival.get(Calendar.MINUTE),false).show();
            }
        });
        //set onClickListener
        texts[mSelectedTourStart].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getContext(),tourStartSetListener,9,0,false).show();
            }
        });
        texts[mSelectedTourEnd].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getContext(),tourEndSetListener,22,0,false).show();
            }
        });

        //load if there is
        String country = null;
        if((country = ((CreatePlanActivity)getActivity()).getCountry())!=null)
        {
            for(int i = mSelectedCountry;i<mSelectedCountryShowDepart;i++)
            {
                texts[i].setText(country);
            }
            texts[mSelectedCountry].setSelected(true);
        }
        Calendar arrival = null;
        if((arrival = ((CreatePlanActivity)getActivity()).getArrived())!=null)
        {
            texts[mSelectedArrivalDate].setText(arrival.get(Calendar.YEAR)+"년 "
                    +(arrival.get(Calendar.MONTH)+1)+"월 "+arrival.get(Calendar.DAY_OF_MONTH)+"일");
            texts[mSelectedArrivalTime].setSelected(true);
            texts[mSelectedArrivalTime]
                    .setText(arrival.get(Calendar.HOUR_OF_DAY)+"시 "
                            +arrival.get(Calendar.MINUTE)+"분");
        }
        Calendar depart = null;
        if((depart=((CreatePlanActivity)getActivity()).getDepature())!=null)
        {
            texts[mSelectedDepartDate].setText(depart.get(Calendar.YEAR)+"년 "
                    +(depart.get(Calendar.MONTH)+1)+"월 "+depart.get(Calendar.DAY_OF_MONTH)+"일");
            texts[mSelectedDepartTime].setSelected(true);
            texts[mSelectedDepartTime]
                    .setText(depart.get(Calendar.HOUR_OF_DAY)+"시 "
                            +depart.get(Calendar.MINUTE)+"분");
        }
        Calendar tourS = null;
        if((tourS=((CreatePlanActivity)getActivity()).getTourStart())!=null)
        {
            texts[mSelectedTourStart].setSelected(true);
            texts[mSelectedTourStart].setText(
                    tourS.get(Calendar.HOUR_OF_DAY)+"시 "+
                            tourS.get(Calendar.MINUTE)+"분");
        }
        Calendar tourE = null;
        if((tourE=((CreatePlanActivity)getActivity()).getTourEnd())!=null)
        {
            texts[mSelectedTourEnd].setSelected(true);
            texts[mSelectedTourEnd].setText(
                    tourE.get(Calendar.HOUR_OF_DAY)+"시 "+
                            tourE.get(Calendar.MINUTE)+"분");
        }
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
                    texts[mSelectedCountry].setSelected(true);
                    //texts[mSelectedCountry].setBackgroundColor(Color.TRANSPARENT);
                    //texts[mSelectedCountry].setTextColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.v("******server", "send msg failed");
            }
        }
    }
}
