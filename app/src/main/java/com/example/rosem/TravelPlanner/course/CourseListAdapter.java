package com.example.rosem.TravelPlanner.course;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.rosem.TravelPlanner.R;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.type;

/**
 * Created by rosem on 2017-01-30.
 */
public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.ViewHolder> {
    private Context mContext;
    private List<Course> mItems = new ArrayList<Course>();
    private Typeface fontType;

    public CourseListAdapter(Context context)
    {
        mContext = context;
        fontType = Typeface.createFromAsset(context.getAssets(),context.getString(R.string.font_name));
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