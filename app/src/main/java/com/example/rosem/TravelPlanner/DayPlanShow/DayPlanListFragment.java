package com.example.rosem.TravelPlanner.DayPlanShow;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.rosem.TravelPlanner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.rosem.TravelPlanner.course.Course;
import com.example.rosem.TravelPlanner.course.CourseListAdapter;

/**
 * Created by rosem on 2017-01-30.
 */

public class DayPlanListFragment extends Fragment {

    JSONArray contents;
    ListView list;
    CourseListAdapter mAdapter;

    public DayPlanListFragment()
    {

    }

    public static DayPlanListFragment newInstance(JSONArray day) {

        Bundle args = new Bundle();

        DayPlanListFragment fragment = new DayPlanListFragment();
        Log.v("DPLFragment::","newInstance");
        Log.v("DPLFragment::","day\n"+day.toString());

        //argument 가져오기
        args.putString("contents",day.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("DPLFragment::","onCreate");
        if(getArguments()!=null)
        {
            //argument setting
            try
            {
                Log.v("Fragment","here");
                contents = new JSONArray(getArguments().getString("contents"));
                Log.v("Fragment","contents\n"+contents);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.v("DPLFragment","onCreateView");

        ViewGroup temp = (ViewGroup)inflater.inflate(R.layout.course_list,container,false);

        list = (ListView)temp.findViewById(R.id.days);
        mAdapter = new CourseListAdapter(getContext());


        //list에서 저장된 걸 가져오는 부분
        for(int i = 0; i<contents.length();i++)
        {
            try
            {
                JSONObject obj = contents.getJSONObject(i);
                Course c = new Course();
                c.setName(obj.getString("name"));
                c.setAddr(obj.getString("addr"));
                c.setTime(obj.getString("time"));
                c.setCostTime(obj.getString("cost_time"));
                c.setCostMoney(obj.getString("cost_money"));

                mAdapter.addCourse(c);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        list.setAdapter(mAdapter);

        return temp;
    }



}
