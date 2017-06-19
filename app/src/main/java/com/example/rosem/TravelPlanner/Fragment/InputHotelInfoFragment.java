package com.example.rosem.TravelPlanner.Fragment;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.Activity.CreatePlanActivity;
import com.example.rosem.TravelPlanner.adapter.HotelListAdapter;
import com.example.rosem.TravelPlanner.object.Schedule;
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

/**
 * Created by rosem on 2017-02-28.
 */

public class InputHotelInfoFragment extends Fragment {


    private final int mTextLodging = 0;
    private final int mTextLodgingPlus = 1;
    private final int mTextLodgingYes = 2;
    private final int mTextLodgingNo = 3;
    private final int mTextLodgingAdd = 4;

    private final int mHotelInfoTextNum = 5;

    TextView [] texts = new TextView[mHotelInfoTextNum];
    Typeface fontType;

    boolean isHotelReserved = false;

    CheckBox lodgingYes;
    CheckBox lodgingNo;

    RecyclerView selectedHotelView;
    HotelListAdapter mAdapter;
    ArrayList<Site> mSelectedHotels;
    ArrayList<Calendar> checkInList;
    ArrayList<Calendar> checkOutList;

    private final int PLACE_PICK_REQUEST = 1213;

    private Schedule schedule = Schedule.getInstance();

    public static InputHotelInfoFragment newInstance()
    {
        InputHotelInfoFragment fragment = new InputHotelInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fontType = ((CreatePlanActivity)getActivity()).getFontType();
        checkInList = new ArrayList<>();
        checkOutList = new ArrayList<>();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.plan_input_hotel_info,container,false);

        settingTextView(view);

        //setting checkbox
        lodgingYes = (CheckBox)view.findViewById(R.id.hotel_info_check_yes);
        lodgingNo = (CheckBox)view.findViewById(R.id.hotel_info_check_no);
        setPlusVisible(View.INVISIBLE);

        //setting checkbox onCheckListener
        lodgingYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean yes) {
                texts[mTextLodgingYes].setSelected(yes);
                if(yes)
                {
                    isHotelReserved = yes;
                    setAddVisible(View.VISIBLE);
                    setPlusVisible(View.INVISIBLE);
                    lodgingNo.setChecked(false);
                }
                else
                {
                    if(!(lodgingNo.isChecked()))
                    {
                        setPlusVisible(View.INVISIBLE);
                        setAddVisible(View.INVISIBLE);
                    }
                }
            }
        });
        lodgingNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean no) {
                texts[mTextLodgingNo].setSelected(no);
                if(no)
                {
                    isHotelReserved = !(no);
                    setPlusVisible(View.VISIBLE);
                    setAddVisible(View.INVISIBLE);
                    lodgingYes.setChecked(false);
                }
                else
                {
                    if(!(lodgingYes.isChecked()))
                    {
                        setPlusVisible(View.INVISIBLE);
                        setAddVisible(View.INVISIBLE);
                    }
                }
            }
        });

        //set recyclerView
        selectedHotelView = (RecyclerView)view.findViewById(R.id.hotel_info_selected_hotels);
        mSelectedHotels = schedule.getHotel();
        if(mSelectedHotels==null)
        {
            mSelectedHotels = new ArrayList<Site>();
            mAdapter = new HotelListAdapter(getContext(),null,null,null);
        }
        else
        {
            ArrayList<Calendar> checkIn = checkInList;
            ArrayList<Calendar> checkOut = checkOutList;
            mAdapter = new HotelListAdapter(getContext(),mSelectedHotels,checkIn,checkOut);
        }

        selectedHotelView .setAdapter(mAdapter);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        //manager.generateLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        //manager.setAutoMeasureEnabled(false);
        selectedHotelView .setLayoutManager(manager);

        //set divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(),R.drawable.list_line_divider));
        selectedHotelView .addItemDecoration(dividerItemDecoration);

        setAddVisible(View.INVISIBLE);

        Button prevButton = (Button)getActivity().findViewById(R.id.create_plan_prev);
        Button nextButton = (Button)getActivity().findViewById(R.id.create_plan_next);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //saveData();
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

    private void setPlusVisible(int visibility)
    {
        texts[mTextLodgingPlus].setVisibility(visibility);
    }

    private void setAddVisible(int visibility)
    {
        texts[mTextLodgingAdd].setVisibility(visibility);
        selectedHotelView.setVisibility(visibility);
    }

    private void settingTextView(ViewGroup view)
    {

        texts[mTextLodging] = (TextView)view.findViewById(R.id.hotel_info_lodging_explain);
        texts[mTextLodgingPlus] = (TextView)view.findViewById(R.id.hotel_info_txt_lodging_plus);
        texts[mTextLodgingYes] = (TextView)view.findViewById(R.id.hotel_info_txt_lodging_yes);
        texts[mTextLodgingNo] = (TextView)view.findViewById(R.id.hotel_info_txt_lodging_no);
        texts[mTextLodgingAdd] = (TextView)view.findViewById(R.id.hotel_info_add_lodging);

        for(int i =0; i<mHotelInfoTextNum;i++)
        {
            texts[i].setTypeface(fontType);
        }

        texts[mTextLodgingAdd].setOnClickListener(new View.OnClickListener() {
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


        //if there is data -> load
    }

    private boolean checkInput()
    {
        if(isHotelReserved)
        {
           int hotelNum = mAdapter.getHotelList().size();
            if(hotelNum==0)
            {
                Toast.makeText(getContext(), getString(R.string.input_check_hotel_num), Toast.LENGTH_SHORT).show();
                return false;
            }
            if(mAdapter.getCheckInList().size() != hotelNum || mAdapter.getCheckOutList().size()!=hotelNum)
            {
                Toast.makeText(getContext(), getString(R.string.input_check_checkin_checkout), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void saveData()
    {
        if(isHotelReserved)
        {
            int iteration = mAdapter.getHotelList().size();
            ArrayList<Calendar> checkin = mAdapter.getCheckInList();
            ArrayList<Calendar> checkout = mAdapter.getCheckOutList();
            ArrayList<Site> selectedHotels = mAdapter.getHotelList();
            ArrayList<Site> hotels = new ArrayList<>();
            if(checkin.size() == selectedHotels.size() && checkout.size() == selectedHotels.size())
            {
                for(int i = 0; i<iteration; i++)
                {
                    int numOfDay = calendarToNumOfDay(checkin.get(i),checkout.get(i));
                    Site h = selectedHotels.get(i);
                    for(int j = 0; j<numOfDay;j++)
                    {
                        hotels.add(h);
                    }
                }
            }
            checkInList = mAdapter.getCheckInList();
            checkOutList = mAdapter.getCheckOutList();
            schedule.setNumOfHotels(selectedHotels.size());
            schedule.setHotel(hotels);
        }
        schedule.setHotelReserved(isHotelReserved);
    }

    public int calendarToNumOfDay(Calendar start, Calendar end)
    {
        int numOfDay = 0;
        if(start.get(Calendar.MONTH)==end.get(Calendar.MONTH))
        {
            numOfDay = end.get(Calendar.DAY_OF_MONTH) - start.get(Calendar.DAY_OF_MONTH);
        }
        else
        {
            numOfDay = start.getActualMaximum(Calendar.DAY_OF_MONTH)-start.get(Calendar.DAY_OF_MONTH)+end.get(Calendar.DAY_OF_MONTH);
        }
        return numOfDay;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PLACE_PICK_REQUEST)
        {
            if(resultCode == getActivity().RESULT_OK)
            {
                Place selectedPlace = PlacePicker.getPlace(getContext(),data);
                Site hotel = ((CreatePlanActivity)getActivity()).setSiteFromPlace(selectedPlace);
                mSelectedHotels.add(hotel);

                mAdapter.addHotel(hotel);

                //SendRequest request = new SendRequest(selectedPlace.getId());
                //request.postData(null);
            }
        }
    }

    private class SendRequest extends AsyncTask<String,String,String> {
        String urlString = getString(R.string.google_geocoding_base);

        public SendRequest(String placeId) {
            urlString += placeId;
            urlString += "&language=ko&key=";
            urlString += getString(R.string.google_http_api_key);
        }

        @Override
        protected String doInBackground(String... strings) {
            postData(urlString);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            Log.v("Main::", "start send");
        }

        public void postData(String str) {
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
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                }
                Log.v("PlanInfo::bring success", "result:" + response.toString());
                String country = null;
                //parsing
                try {
                    JSONObject responseObj = new JSONObject(response.toString());
                    JSONArray result = responseObj.getJSONArray("results");
                    JSONObject firstObj = result.getJSONObject(0);
                    JSONArray addrComponent = firstObj.getJSONArray("address_components");
                    for (int i = 0; i < addrComponent.length(); i++) {
                        JSONObject addr = addrComponent.getJSONObject(i);
                        JSONArray types = addr.getJSONArray("types");
                        if (types.getString(0).equals("country")) {
                            country = addr.getString("long_name");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //code add to recyclerview

            } catch (IOException e) {
                e.printStackTrace();
                Log.v("******server", "send msg failed");
            }
        }
    }
}
