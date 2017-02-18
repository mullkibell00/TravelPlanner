package com.example.rosem.TravelPlanner.course;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rosem on 2017-01-30.
 */
public class CourseListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Course> mItems = new ArrayList<Course>();

    public CourseListAdapter(Context context)
    {
        mContext = context;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CourseView courseView;
        if(view ==null)
        {
            courseView = new CourseView(mContext, mItems.get(i));
        }
        else
        {
            courseView = (CourseView) view;
        }

        courseView.setText("name",mItems.get(i).getName());
        courseView.setText("addr",mItems.get(i).getAddr());
        courseView.setText("time",mItems.get(i).getTime());
        courseView.setText("costTime",mItems.get(i).getCostTime());
        courseView.setText("costMoney",mItems.get(i).getCostMoney());

        return courseView;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Course getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void addCourse(Course course)
    {
        mItems.add(course);
    }
}