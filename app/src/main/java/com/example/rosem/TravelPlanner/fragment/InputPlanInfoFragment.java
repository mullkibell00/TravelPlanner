package com.example.rosem.TravelPlanner.Fragment;

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
import android.widget.Toast;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.Activity.CreatePlanActivity;
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
import java.util.Calendar;

/**
 * Created by rosem on 2017-02-26.
 */

public class InputPlanInfoFragment extends Fragment {

    private final int mTxtArrival = 0;
    private final int mTxtDeparture=1;
    private final int mTxtTravelingCountry=2;
    private final int mTxtArrivalPlace = 3;
    private final int mTxtDeparturePlace= 4;
    private final int mSelectedCountry=5;
    private final int mSelectedCountryShowArrival = 6;
    private final int mSelectedCountryShowDepart = 7;
    private final int mSelectedArrivalDate=8;
    private final int mSelectedArrivalTime=9;
    private final int mSelectedArrivalPlace=10;
    private final int mSelectedDepartDate=11;
    private final int mSelectedDepartTime=12;
    private final int mSelectedDepartPlace = 13;



    private final int planInfoTextNum=14;
    Typeface fontType;

    private final int COUNTRY_PICK_REQUEST = 1213;
    private final int ARRIVAL_PICK_REQUEST = 1214;
    private final int DEPART_PICK_REQUEST = 1215;

    TextView[] texts = new TextView[planInfoTextNum];
    private DatePickerDialog.OnDateSetListener arrivalDateSetListener;
    private DatePickerDialog.OnDateSetListener departDateSetListener;
    private TimePickerDialog.OnTimeSetListener arrivalTimeSetListener;
    private TimePickerDialog.OnTimeSetListener departTimeSetListener;

    //current
    int curYear, curMonth, curDay;
    Calendar selectedArrival;
    Calendar selectedDepart;
    int travelingPeriod;
    Site arrivalPlace = null;
    Site departPlace = null;

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

        arrivalDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                ((CreatePlanActivity)getActivity()).setDateText(texts[mSelectedArrivalDate],year,month,day);
                //texts[mSelectedArrivalDate].setText(year+"년 "+(month+1)+"월 "+day+"일");
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
                //texts[mSelectedArrivalTime].setText(hour+"시 "+minute+"분");
                ((CreatePlanActivity)getActivity()).setTimeText(texts[mSelectedArrivalTime],hour,minute);
                selectedArrival.set(Calendar.HOUR_OF_DAY,hour);
                selectedArrival.set(Calendar.MINUTE,minute);
            }
        };
        departDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //texts[mSelectedDepartDate].setText(year+"년 "+(month+1)+"월 "+day+"일");
                ((CreatePlanActivity)getActivity()).setDateText(texts[mSelectedDepartDate],year,month,day);
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
                //texts[mSelectedDepartTime].setText(hour+"시 "+minute+"분");
                ((CreatePlanActivity)getActivity()).setTimeText(texts[mSelectedDepartTime],hour,minute);
                selectedDepart.set(Calendar.HOUR_OF_DAY,hour);
                selectedDepart.set(Calendar.MINUTE,minute);
            }
        };

        Calendar curCalendar = Calendar.getInstance();
        curYear = curCalendar.get(Calendar.YEAR);
        curMonth = curCalendar.get(Calendar.MONTH);
        curDay = curCalendar.get(Calendar.DATE);
        int curHour = curCalendar.get(Calendar.HOUR_OF_DAY);
        int curMinute = curCalendar.get(Calendar.MINUTE);
        selectedArrival.set(Calendar.YEAR,curYear); selectedArrival.set(Calendar.MONTH,curMonth);
        selectedArrival.set(Calendar.DAY_OF_MONTH,curDay); selectedArrival.set(Calendar.HOUR_OF_DAY,curHour);
        selectedArrival.set(Calendar.MINUTE,curMinute);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.plan_input_plan_info,container,false);

        if(((CreatePlanActivity)getActivity()).getStartPoint()!=null)
        {
            arrivalPlace = ((CreatePlanActivity)getActivity()).getStartPoint();
        }
        if(((CreatePlanActivity)getActivity()).getEndPoint()!=null)
        {
            departPlace = ((CreatePlanActivity)getActivity()).getEndPoint();
        }

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
                if(checkInput())
                {
                    saveData();
                    ((CreatePlanActivity)getActivity()).moveNext();
                }
            }
        });


        return view;
    }

    public boolean checkInput()
    {
        //나라 입력은 했는지
        if(texts[mSelectedCountry].getText().toString().equals(getString(R.string.txt_select_traveling_country)))
        {
            Toast.makeText(getContext(), getString(R.string.input_check_country), Toast.LENGTH_SHORT).show();
            return false;
        }
        //arrivalPlace와 departPlace가 입력되었는지
        if(arrivalPlace==null)
        {
            Toast.makeText(getContext(), getString(R.string.input_check_arrival), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(departPlace==null)
        {
            Toast.makeText(getContext(), getString(R.string.input_check_departure), Toast.LENGTH_SHORT).show();
            return false;
        }
        //도착날과 출발날이 같지는 않은지
        if(selectedArrival.get(Calendar.MONTH)==selectedDepart.get(Calendar.MONTH)
                && selectedArrival.get(Calendar.DAY_OF_MONTH)==selectedDepart.get(Calendar.DAY_OF_MONTH))
        {
            Toast.makeText(getContext(), getString(R.string.input_check_date), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    public void saveData()
    {
        //setting arrivalPlace, departPlace
        //numofDays, arrival,departure,country
        ((CreatePlanActivity)getActivity()).setFirstDayStart(new Time(selectedArrival.get(Calendar.HOUR_OF_DAY),selectedArrival.get(Calendar.MINUTE)));
        ((CreatePlanActivity)getActivity()).setLastDayEnd(new Time(selectedDepart.get(Calendar.HOUR_OF_DAY),selectedDepart.get(Calendar.MINUTE)));
        ((CreatePlanActivity)getActivity()).setCountry(texts[mSelectedCountry].getText().toString());
        ((CreatePlanActivity)getActivity()).setStartPoint(arrivalPlace);
        ((CreatePlanActivity)getActivity()).setEndPoint(departPlace);

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
        texts[mTxtArrivalPlace] = (TextView)view.findViewById(R.id.plan_info_txt_arrival_place);
        texts[mTxtDeparturePlace] = (TextView)view.findViewById(R.id.plan_info_txt_departure_place);
        texts[mSelectedCountry] = (TextView)view.findViewById(R.id.plan_info_txt_selected_country);
        texts[mSelectedCountryShowArrival] = (TextView)view.findViewById(R.id.plan_info_country_name1);
        texts[mSelectedCountryShowDepart] = (TextView)view.findViewById(R.id.plan_info_country_name2);
        texts[mSelectedArrivalDate] = (TextView)view.findViewById(R.id.plan_info_txt_select_arrival_date);
        texts[mSelectedArrivalTime] = (TextView)view.findViewById(R.id.plan_info_txt_select_arrival_time);
        texts[mSelectedArrivalPlace] = (TextView)view.findViewById(R.id.plan_info_arrival_place);
        texts[mSelectedDepartPlace] = (TextView)view.findViewById(R.id.plan_info_departure_place);
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
                    startActivityForResult(intent,COUNTRY_PICK_REQUEST);
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
        texts[mSelectedArrivalPlace].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                    Intent intent = builder.build(getActivity());
                    startActivityForResult(intent,ARRIVAL_PICK_REQUEST);
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

        texts[mSelectedDepartPlace].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                    Intent intent = builder.build(getActivity());
                    startActivityForResult(intent,DEPART_PICK_REQUEST);
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
                new TimePickerDialog(getContext(),arrivalTimeSetListener,0,0,false).show();
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
                new TimePickerDialog(getContext(),departTimeSetListener, 0, 0,false).show();
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
        if(selectedArrival!=null)
        {
            //texts[mSelectedArrivalDate].setText(arrival.get(Calendar.YEAR)+"년 "
            //        +(arrival.get(Calendar.MONTH)+1)+"월 "+arrival.get(Calendar.DAY_OF_MONTH)+"일");
            ((CreatePlanActivity)getActivity()).
                    setDateText(texts[mSelectedArrivalDate],selectedArrival.get(Calendar.YEAR),
                            selectedArrival.get(Calendar.MONTH),selectedArrival.get(Calendar.DAY_OF_MONTH));
            texts[mSelectedArrivalTime].setSelected(true);
            ((CreatePlanActivity)getActivity())
                    .setTimeText(texts[mSelectedArrivalTime],selectedArrival.get(Calendar.HOUR_OF_DAY),selectedArrival.get(Calendar.MINUTE));
            //texts[mSelectedArrivalTime]
            //        .setText(arrival.get(Calendar.HOUR_OF_DAY)+"시 "
            //                +arrival.get(Calendar.MINUTE)+"분");
        }
        if(selectedDepart!=null)
        {
           // texts[mSelectedDepartDate].setText(depart.get(Calendar.YEAR)+"년 "
           //         +(depart.get(Calendar.MONTH)+1)+"월 "+depart.get(Calendar.DAY_OF_MONTH)+"일");
            ((CreatePlanActivity)getActivity()).
                    setDateText(texts[mSelectedDepartDate],selectedDepart.get(Calendar.YEAR),
                            selectedDepart.get(Calendar.MONTH),selectedDepart.get(Calendar.DAY_OF_MONTH));
            texts[mSelectedDepartTime].setSelected(true);
            ((CreatePlanActivity)getActivity()).setTimeText(texts[mSelectedDepartTime]
                    ,selectedDepart.get(Calendar.HOUR_OF_DAY),selectedDepart.get(Calendar.MINUTE));
            //texts[mSelectedDepartTime]
            //        .setText(depart.get(Calendar.HOUR_OF_DAY)+"시 "
            //                +depart.get(Calendar.MINUTE)+"분");
        }
        if(arrivalPlace!=null)
        {
            texts[mSelectedArrivalPlace].setText(arrivalPlace.getPlaceName());
        }
        if(departPlace!=null)
        {
            texts[mSelectedDepartPlace].setText(departPlace.getPlaceName());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==COUNTRY_PICK_REQUEST)
        {
            if(resultCode == getActivity().RESULT_OK)
            {
                Place selectedPlace = PlacePicker.getPlace(getContext(),data);
                SendRequest request = new SendRequest(selectedPlace.getId());
                request.postData(null);
            }
        }
        else if(requestCode==ARRIVAL_PICK_REQUEST)
        {
            if(resultCode == getActivity().RESULT_OK)
            {
                Place selectedPlace = PlacePicker.getPlace(getContext(),data);
                arrivalPlace = ((CreatePlanActivity)getActivity()).setSiteFromPlace(selectedPlace);
                texts[mSelectedArrivalPlace].setText(arrivalPlace.getPlaceName());

            }
        }
        else if(requestCode==DEPART_PICK_REQUEST)
        {
            Place selectedPlace = PlacePicker.getPlace(getContext(),data);
            departPlace = ((CreatePlanActivity)getActivity()).setSiteFromPlace(selectedPlace);
            texts[mSelectedDepartPlace].setText(departPlace.getPlaceName());
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
