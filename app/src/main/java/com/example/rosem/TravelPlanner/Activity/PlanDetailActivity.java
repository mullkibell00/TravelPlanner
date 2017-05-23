package com.example.rosem.TravelPlanner.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.plan.Plan;
import com.example.rosem.TravelPlanner.plan.PlanAdapter;

import io.realm.Realm;

/**
 * Created by bianca on 2017-05-21.
 */

public class PlanDetailActivity extends AppCompatActivity {

    Realm db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail);

        Typeface fontType = Typeface.createFromAsset(getAssets(),getString(R.string.font_name));
        Toolbar titleBar = (Toolbar)findViewById(R.id.plan_detail_toolbar);
        TextView title = (TextView)titleBar.findViewById(R.id.plan_detail_title);
        ImageView saveButton = (ImageView)titleBar.findViewById(R.id.save);
        saveButton.setVisibility(View.INVISIBLE);
        TextView planInfo = (TextView)findViewById(R.id.plan_info);
        TextView planInfoCostTime = (TextView)findViewById(R.id.plan_info_cost_time);
        TextView planInfoCountry = (TextView)findViewById(R.id.plan_info_country);

        //setting textviews
        title.setTypeface(fontType); planInfo.setTypeface(fontType);
        planInfoCountry.setTypeface(fontType); planInfoCostTime.setTypeface(fontType);

        Intent intent = getIntent();
        String planName = intent.getStringExtra("planName");

        db = Realm.getDefaultInstance();
        Plan plan = db.where(Plan.class).equalTo("planName",planName).findFirst();
        plan.setPlanArrayFromPlan();

        TabLayout tabs=(TabLayout)findViewById(R.id.plan_tabs);
        ViewPager pager = (ViewPager)findViewById(R.id.plan_pager);
        PlanAdapter mAdapter = new PlanAdapter(getSupportFragmentManager());
        mAdapter.setCourse(plan);
        pager.setAdapter(mAdapter);

        tabs.setupWithViewPager(pager);

        //setting textView contents
        planInfo.setText(plan.getPlanName()+getString(R.string.plan_info_title));
        planInfoCostTime.setText(getString(R.string.total_cost_time)+plan.getTotalCostTime());
        planInfoCountry.setText(getString(R.string.plan_info_country)+plan.getCountry());
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
