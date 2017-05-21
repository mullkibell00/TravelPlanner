package com.example.rosem.TravelPlanner.course;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rosem.TravelPlanner.R;

/**
 * Created by rosem on 2017-01-30.
 */

public class CourseView extends RelativeLayout {
    private TextView mName;
    private TextView mAddr;
    private TextView mTime;
    private TextView mSpendTime;
    private TextView mCostTime;
    private TextView mIndex;
    private View mDivider;

    public CourseView(Context context, Course course)
    {
        super(context);
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.course_item,this,true);

        mName = (TextView)findViewById(R.id.name);
        mName.setText(course.getName());

        mAddr = (TextView)findViewById(R.id.addr);
        mAddr.setText(course.getAddr());

        mTime = (TextView)findViewById(R.id.time);
        mTime.setText(course.getTime());

        mSpendTime = (TextView)findViewById(R.id.spend_time);
        mSpendTime.setText(course.getSpendTime());

        mCostTime = (TextView)findViewById(R.id.cost_time);
        mCostTime.setText(course.getCostMoney());

        mIndex = (TextView)findViewById(R.id.index);

        mDivider = (View)findViewById(R.id.cost_time_underline);
    }

    public CourseView(Context context)
    {
        super(context);
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.course_item,this,true);

        mName = (TextView)findViewById(R.id.name);

        mAddr = (TextView)findViewById(R.id.addr);

        mTime = (TextView)findViewById(R.id.time);

        mSpendTime = (TextView)findViewById(R.id.spend_time);

        mCostTime = (TextView)findViewById(R.id.cost_time);

        mIndex = (TextView)findViewById(R.id.index);

        mDivider = (View)findViewById(R.id.cost_time_underline);

    }


    public void setText(String key, String data)
    {
        if(key.equals("name"))
        {
            mName.setText(data);
        }
        else if(key.equals("addr"))
        {
            mAddr.setText(data);
        }
        else if(key.equals("time"))
        {
            mTime.setText(data);
        }
        else if(key.equals("spendTime"))
        {
            mSpendTime.setText(data);
        }
        else
        {
            mCostTime.setText(data);
        }
    }

    public void setCostTimeVisibility(int visibility)
    {
        mCostTime.setVisibility(visibility);
        mDivider.setVisibility(visibility);
    }

    public void setIndex(int position)
    {
        mIndex.setText(Integer.toString(position));
    }

    public void setCourse(Course c)
    {
        mName.setText(c.getName());
        mAddr.setText(c.getAddr());
        mTime.setText(c.getTime());
        mSpendTime.setText(c.getSpendTime());
        mCostTime.setText(c.getCostTime());
    }

    public void setTypeface(Typeface fontType)
    {
        mName.setTypeface(fontType);
        mAddr.setTypeface(fontType);
        mTime.setTypeface(fontType);
        mCostTime.setTypeface(fontType);
        mSpendTime.setTypeface(fontType);
        mIndex.setTypeface(fontType);
    }
}