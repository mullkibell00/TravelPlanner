package com.example.rosem.TravelPlanner.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.activity.CreatePlanActivity;
import com.example.rosem.TravelPlanner.object.Site;

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

/**
 * Created by rosem on 2017-03-07.
 */

public class HotelRecommendFragment extends Fragment {

    ProgressDialog progressDialog;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.plan_hotel_recommend,container,false);




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

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.v("HotelRecommend", "HotelRecommend=Sending Fail");
            }
            return null;
        }
    }

    private class LatLng
    {
        double lat;
        double lng;

        public String toString()
        {
            String ret = Double.toString(lat)+","+Double.toString(lng);
            return ret;
        }
    }

}
