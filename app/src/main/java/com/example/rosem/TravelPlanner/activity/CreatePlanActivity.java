package com.example.rosem.TravelPlanner.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.rosem.TravelPlanner.R;

/**
 * Created by rosem on 2017-02-25.
 */

public class CreatePlanActivity extends AppCompatActivity {

    Typeface fontType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);

        fontType = Typeface.createFromAsset(getAssets(),getString(R.string.font_name));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
