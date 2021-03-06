package com.example.rosem.TravelPlanner.plan;

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

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.course.Course;
import com.example.rosem.TravelPlanner.course.CourseListAdapter;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by rosem on 2017-01-30.
 */

public class PlanListFragment extends Fragment {

    JSONArray contents;
    RecyclerView list;
    CourseListAdapter mAdapter;

    public PlanListFragment()
    {

    }

    public static PlanListFragment newInstance(JSONArray day) {

        Bundle args = new Bundle();

        PlanListFragment fragment = new PlanListFragment();
        Log.v("DPLFragment::","newInstance");
        if(day!=null)
        {
            Log.v("DPLFragment::","day\n"+day.toString());

            //argument 가져오기
            args.putString("contents",day.toString());
        }

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
                if(getArguments().getString("contents")!=null)
                {
                    contents = new JSONArray(getArguments().getString("contents"));
                }
                else
                {
                    contents = new JSONArray();
                }
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

        list = (RecyclerView)temp.findViewById(R.id.days);
        mAdapter = new CourseListAdapter(getContext());


        //list에서 저장된 걸 가져오는 부분
        for(int i = 0; i<contents.length();i++)
        {
            Course c = Course.getCourseFromDay(i,contents);

            mAdapter.addCourse(c);
        }

        list.setAdapter(mAdapter);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
       list.setLayoutManager(manager);

        //set divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(),R.drawable.list_line_divider));
        list.addItemDecoration(dividerItemDecoration);

        return temp;
    }



}
