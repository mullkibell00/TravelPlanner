package com.example.rosem.TravelPlanner.course;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
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
    private TextView mCostMoney;

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

        mSpendTime = (TextView)findViewById(R.id.cost_time);
        mSpendTime.setText(course.getSpendTime());

        mCostMoney = (TextView)findViewById(R.id.cost_money);
        mCostMoney.setText(course.getCostMoney());

    }

    public CourseView(Context context)
    {
        super(context);
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.course_item,this,true);

        mName = (TextView)findViewById(R.id.name);

        mAddr = (TextView)findViewById(R.id.addr);

        mTime = (TextView)findViewById(R.id.time);

        mSpendTime = (TextView)findViewById(R.id.cost_time);

        mCostMoney = (TextView)findViewById(R.id.cost_money);

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
            mCostMoney.setText(data);
        }
    }

    public void setCourse(Course c)
    {
        mName.setText(c.getName());
        mAddr.setText(c.getAddr());
        mTime.setText(c.getTime());
        mSpendTime.setText(c.getSpendTime());
        mCostMoney.setText(c.getCostMoney());
    }

    public void setTypeface(Typeface fontType)
    {
        mName.setTypeface(fontType);
        mAddr.setTypeface(fontType);
        mTime.setTypeface(fontType);
        mCostMoney.setTypeface(fontType);
        mSpendTime.setTypeface(fontType);
    }
}