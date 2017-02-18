package com.example.rosem.TravelPlanner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rosem.TravelPlanner.DayPlanShow.DayPlanAdapter;
import com.example.rosem.TravelPlanner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rosem on 2017-01-04.
 */
public class FavoriteFragment extends Fragment {
    TabLayout tabs;
    ViewPager pager;
    DayPlanAdapter mAdapter;
    //sharedPref에서 JSON을 가져올 JSON
    JSONObject course;
    FragmentManager fm;

    public FavoriteFragment()
    {

    }

    public static FavoriteFragment newInstance(FragmentManager fm) {

        Bundle args = new Bundle();

        FavoriteFragment fragment = new FavoriteFragment();

        fragment.setFragmentManager(fm);
        return fragment;
    }
    private void setFragmentManager(FragmentManager fm)
    {
        this.fm = fm;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.favorite_fragment,container,false);

        //start

        course = new JSONObject();

        try
        {

            JSONArray jArr1 = new JSONArray();
            JSONObject temp = new JSONObject();
            temp.put("name","London Eye");
            temp.put("time","10:00~10:15");
            temp.put("addr","London");
            temp.put("cost_time","15min");
            temp.put("cost_money","10pounds");

            jArr1.put(temp);

            JSONObject temp1 = new JSONObject();
            temp1.put("name","London Bridge");
            temp1.put("time","10:35~11:00");
            temp1.put("addr","London");
            temp1.put("cost_time","20min");
            temp1.put("cost_money","5pounds");

            jArr1.put(temp1);

            course.put("Day1",jArr1);

            JSONArray jArr2 = new JSONArray();
            JSONObject temp2 = new JSONObject();
            temp2.put("name","London Tower");
            temp2.put("time","10:10~10:15");
            temp2.put("addr","London");
            temp2.put("cost_time","10min");
            temp2.put("cost_money","15pounds");

            jArr2.put(temp2);

            JSONObject temp3 = new JSONObject();
            temp3.put("name","Great Britain Museum");
            temp3.put("time","10:45~13:00");
            temp3.put("addr","London");
            temp3.put("cost_time","30min");
            temp3.put("cost_money","20pounds");

            jArr2.put(temp3);

            course.put("Day2",jArr2);

            Log.v("Main","result\n"+course);


        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        //end



        tabs=(TabLayout)view.findViewById(R.id.tabs);
        pager = (ViewPager)view.findViewById(R.id.pager);
        mAdapter = new DayPlanAdapter(getChildFragmentManager());
        mAdapter.setCourse(course);
        pager.setAdapter(mAdapter);

        tabs.setupWithViewPager(pager);

        //end

        return view;
    }

    @Override
    public void onDestroyView() {
        Log.v("Favorite::","destroy view");
       // mAdapter.destroyAll();
        super.onDestroyView();
    }
}
