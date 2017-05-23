package com.example.rosem.TravelPlanner.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.rosem.TravelPlanner.Interface.GetPlanDetailService;
import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.plan.Plan;
import com.example.rosem.TravelPlanner.plan.PlanAdapter;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by bianca on 2017-05-23.
 */

public class RecommendPlanDetailActivity extends AppCompatActivity {

    Plan plan = null;
    ViewPager pager;
    PlanAdapter mAdapter;
    TabLayout tabs;
    TextView planInfo;
    TextView planInfoCostTime;
    TextView planInfoCountry;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail);

        Typeface fontType = Typeface.createFromAsset(getAssets(),getString(R.string.font_name));
        Toolbar titleBar = (Toolbar)findViewById(R.id.create_plan_toolbar);
        TextView title = (TextView)titleBar.findViewById(R.id.create_plan_title);
        planInfo = (TextView)findViewById(R.id.plan_info);
        planInfoCostTime = (TextView)findViewById(R.id.plan_info_cost_time);
        planInfoCountry = (TextView)findViewById(R.id.plan_info_country);

        tabs=(TabLayout)findViewById(R.id.plan_tabs);
        pager = (ViewPager)findViewById(R.id.plan_pager);
        mAdapter = new PlanAdapter(getSupportFragmentManager());

        //setting textviews
        title.setTypeface(fontType); planInfo.setTypeface(fontType);
        planInfoCountry.setTypeface(fontType); planInfoCostTime.setTypeface(fontType);

        Intent intent = getIntent();
        long id = intent.getLongExtra("id", 0);

        GetPlanDetailAsync getDetail = new GetPlanDetailAsync(id);
        getDetail.execute();

    }

    private class GetPlanDetailAsync extends AsyncTask<Call<ResponseBody>, Void, String>
    {
        long key;
        public GetPlanDetailAsync(long k)
        {
            key = k;
        }
        @Override
        protected String doInBackground(Call<ResponseBody>... params) {
            GetPlanDetailService service = GetPlanDetailService.retrofit.create(GetPlanDetailService.class);
            Call<ResponseBody> call = service.getPlanDetail(key);
            ResponseBody response = null;
            String result = null;
            try {
                response = call.execute().body();
                result = response.string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);

            Gson gson = new Gson();
            try
            {
                plan = gson.fromJson(s,Plan.class);
                plan.setPlanArrayFromPlan();
                mAdapter.setCourse(plan);
                pager.setAdapter(mAdapter);

                tabs.setupWithViewPager(pager);

                //setting textView contents
                planInfo.setText(plan.getPlanName()+getString(R.string.plan_info_title));
                planInfoCostTime.setText(getString(R.string.total_cost_time)+plan.getTotalCostTime());
                planInfoCountry.setText(getString(R.string.plan_info_country)+plan.getCountry());
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
