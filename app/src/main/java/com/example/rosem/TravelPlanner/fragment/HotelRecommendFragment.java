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

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.Activity.CreatePlanActivity;
import com.example.rosem.TravelPlanner.adapter.RecommendListAdapter;
import com.example.rosem.TravelPlanner.object.Site;
import com.google.android.gms.location.places.Place;

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

/**
 * Created by rosem on 2017-03-07.
 */

public class HotelRecommendFragment extends Fragment {

    ProgressDialog progressDialog;
    ArrayList<Site> recommendedList = null;
    RecyclerView hotelListView;
    LatLng midpoint;
    RecommendListAdapter mAdapter;

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
            ArrayList<Site> sites = ((CreatePlanActivity)getActivity()).getSite();
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
                saveData();
                ((CreatePlanActivity)getActivity()).moveNext();
            }
        });
        return view;
    }

    private void saveData()
    {
        int numOfDay=((CreatePlanActivity)getActivity()).getNumOfDay();
        Site hotel = mAdapter.getSelected();
        ArrayList<Site> hotelList = new ArrayList<>();
        for(int j = 0; j<numOfDay;j++)
        {
            hotelList.add(hotel);
        }
        ((CreatePlanActivity)getActivity()).setHotel(hotelList);
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
        String urlString = getString(R.string.google_http_searching_loc);

        public HotelRecommend(LatLng latLng)
        {
            urlString+=latLng.toString()+"&radius=5000&language=ko&types=lodging&key="+getString(R.string.google_http_api_key);
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

            InputStream inputStream = null;
            BufferedReader rd = null;
            StringBuilder response = new StringBuilder();

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(urlString);

            try {
                //서버로 전송 & 받아오기
                HttpResponse httpResponse = httpClient.execute(httpPost);
                Log.v("HotelRecommend", "HotelRecommend=Sending Success");

                inputStream = httpResponse.getEntity().getContent();
                rd = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line=rd.readLine())!=null)
                {
                    response.append(line);
                }
                Log.v("HotelRecommend","result:"+response.toString());
                String country=null;
                //parsing
                try {
                    //bring result array
                    JSONObject responseObj = new JSONObject(response.toString());
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }//end of parsing

            } catch (IOException e) {
                e.printStackTrace();
                Log.v("HotelRecommend", "HotelRecommend=Sending Fail");
            }//end of try/catch of sending & receiving data
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
