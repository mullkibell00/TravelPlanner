package com.example.rosem.TravelPlanner.Activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.rosem.TravelPlanner.R;

import io.realm.Realm;

/**
 * Created by bianca on 2017-05-21.
 */

public class PlanDetailActivity extends AppCompatActivity {

    Realm db;
    Typeface fontType;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);
        db = Realm.getDefaultInstance();
        fontType = Typeface.createFromAsset(getAssets(),getString(R.string.font_name));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(db!=null)
        {
            db.close();
        }
    }
}
