package com.example.rosem.TravelPlanner.Fragment;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.example.rosem.TravelPlanner.Interface.GoogleMapService;
import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.Activity.CreatePlanActivity;
import com.example.rosem.TravelPlanner.adapter.RecommendListAdapter;
import com.example.rosem.TravelPlanner.object.Schedule;
import com.example.rosem.TravelPlanner.object.Site;
import com.google.android.gms.location.places.Place;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by rosem on 2017-03-07.
 */

public class HotelRecommendFragment extends Fragment {

    ProgressDialog progressDialog;
    ArrayList<Site> recommendedList = null;
    RecyclerView hotelListView;
    LatLng midpoint;
    RecommendListAdapter mAdapter;
    private Schedule schedule = Schedule.getInstance();

    public static HotelRecommendFragment newInstance()
    {
        HotelRecommendFragment fragment = new HotelRecommendFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.recommend_dialog_message));

        midpoint = new LatLng();

        if(recommendedList==null)
        {
            ArrayList<Site> sites = schedule.getSite();
            for(int i = 0; i < sites.size();i++)
            {
                midpoint.lat += sites.get(i).getLat();
                midpoint.lng += sites.get(i).getLng();
            }
            midpoint.lat = midpoint.lat/(sites.size());
            midpoint.lng = midpoint.lng/(sites.size());
            HotelRecommend recommendation = new HotelRecommend(midpoint);

            recommendedList = recommendation.getRecommendation();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.plan_hotel_recommend,container,false);

        hotelListView = (RecyclerView)view.findViewById(R.id.plan_recommended_hotel_list);
        mAdapter = new RecommendListAdapter(getContext(),recommendedList);
        hotelListView.setAdapter(mAdapter);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        hotelListView.setLayoutManager(manager);

        //set divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(),R.drawable.list_line_divider));
        hotelListView.addItemDecoration(dividerItemDecoration);


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
                if(checkInput())
                {
                    saveData();
                    ((CreatePlanActivity)getActivity()).moveNext();
                }
            }
        });
        return view;
    }

    private boolean checkInput()
    {
        if(mAdapter.getSelected()==null)
        {
            Toast.makeText(getContext(), getString(R.string.input_check_select_hotel), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveData()
    {
        int numOfDay=schedule.getNumOfDays()-1;
        Site hotel = mAdapter.getSelected();
        ArrayList<Site> hotelList = new ArrayList<>();
        for(int j = 0; j<numOfDay;j++)
        {
            hotelList.add(hotel);
        }
        schedule.setNumOfHotels(1);
        schedule.setHotel(hotelList);
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

    private class HotelRecommend extends AsyncTask<LatLng,LatLng,LatLng>
    {
        int radius = getResources().getInteger(R.integer.google_api_radius);
        String lang = getString(R.string.google_api_language);
        String type = getString(R.string.google_api_type);
        String key = getString(R.string.google_http_api_key);
        String urlString = getString(R.string.google_http_searching_loc);
        String location = null;

        public HotelRecommend(LatLng latLng)
        {
            location =latLng.toString();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
            Log.v("HotelRecommend::","bring recommendation list");
        }

        @Override
        protected LatLng doInBackground(LatLng... latLngs) {
            return null;
        }

        @Override
        protected void onPostExecute(LatLng latLng) {
            super.onPostExecute(latLng);
            if(progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }
            Log.v("HotelRecommend::","Recommend success");
        }

        public ArrayList<Site> getRecommendation()
        {
            ArrayList<Site> hotelList = new ArrayList<Site>();

            GoogleMapService service = GoogleMapService.retrofit.create(GoogleMapService.class);
            Call<ResponseBody> call = service.getRecommendHotelInfo(location,radius,lang,type,key);
            ResponseBody response = null;
            try {
                response = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
                FirebaseCrash.report(e);
            }
            if(response!=null)
            {
                try {
                    String contents = response.string();
                    String country=null;
                    //parsing
                    try {
                        //bring result array
                        JSONObject responseObj = new JSONObject(contents);
                        JSONArray result = responseObj.getJSONArray("results");
                        //get member of array
                        for(int i=0; i< result.length();i++)
                        {
                            try
                            {
                                Site hotel = new Site();
                                JSONObject hotelObj = result.getJSONObject(i);
                                JSONObject geometry = null;
                                JSONObject location = null;
                                //bring location of lodging
                                if(hotelObj.has("geometry"))
                                {
                                    geometry = hotelObj.getJSONObject("geometry");
                                    if(geometry.has("location"))
                                    {
                                        location = geometry.getJSONObject("location");
                                        if(location.has("lat"))
                                        {
                                            hotel.setLat(location.getDouble("lat"));
                                        }
                                        if(location.has("lng"))
                                        {
                                            hotel.setLng(location.getDouble("lng"));
                                        }
                                    }
                                }
                                //bring name
                                if(hotelObj.has("name"))
                                {
                                    hotel.setPlaceName(hotelObj.getString("name"));
                                }
                                //bring place id
                                if(hotelObj.has("place_id"))
                                {
                                    hotel.setPlaceId(hotelObj.getString("place_id"));
                                }
                                //get temp addr
                                if(hotelObj.has("vicinity"))
                                {
                                    hotel.setAddress(hotelObj.getString("vicinity"));
                                }
                                //set type
                                List<Integer> type = new ArrayList<>();
                                type.add(Place.TYPE_LODGING);
                                hotel.setPlaceType(type);

                                //add to hotelList
                                hotelList.add(hotel);
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }//end of for( result )
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }//end of parsing
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return hotelList;
        }
    }

    private class LatLng
    {
        double lat = 0;
        double lng = 0;

        public String toString()
        {
            String ret = Double.toString(lat)+","+Double.toString(lng);
            return ret;
        }
    }

}
