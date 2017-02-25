package com.example.rosem.TravelPlanner.activity;

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.rosem.TravelPlanner.R;

import java.util.Calendar;

import static com.example.rosem.TravelPlanner.R.mipmap.next;

/**
 * Created by rosem on 2017-02-25.
 */

public class CreatePlanActivity extends AppCompatActivity {

    Typeface fontType;
    TextView title;
    Button nextButton;
    Button prevButton;
    FrameLayout container;
    int iconColor;
    PorterDuff.Mode iconMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);

        fontType = Typeface.createFromAsset(getAssets(),getString(R.string.font_name));
        Toolbar titleBar = (Toolbar)findViewById(R.id.create_plan_toolbar);
        title = (TextView)titleBar.findViewById(R.id.create_plan_title);
        title.setTypeface(fontType);

        iconColor = ContextCompat.getColor(this,R.color.colorLightButton);
        iconMode = PorterDuff.Mode.SRC_IN;

        container = (FrameLayout)findViewById(R.id.container);

        nextButton = (Button)findViewById(R.id.create_plan_next);
        prevButton = (Button)findViewById(R.id.create_plan_prev);

        //setting icon
        Drawable nextImg = ContextCompat.getDrawable(this, next);
        nextImg.setColorFilter(iconColor,iconMode);
        Drawable prevImg = ContextCompat.getDrawable(this, R.mipmap.prev);
        prevImg.setColorFilter(iconColor,iconMode);
        nextButton.setBackground(nextImg);
        prevButton.setBackground(prevImg);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class Schedule
    {
        private String planName;
        private int numOfDays;
        private Calendar depature;
        private Calendar arrived;
        private boolean isHotelReserved;
        private String hotel;
        private String country;
        private Calendar tourStart;
        private Calendar tourEnd;
    }
}
