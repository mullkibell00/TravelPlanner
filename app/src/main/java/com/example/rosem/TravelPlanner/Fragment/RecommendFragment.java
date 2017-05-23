package com.example.rosem.TravelPlanner.Fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rosem.TravelPlanner.Activity.RecommendPlanDetailActivity;
import com.example.rosem.TravelPlanner.Interface.GetRecentPlanService;
import com.example.rosem.TravelPlanner.Interface.GetRecommendService;
import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.adapter.PlanCardListAdapter;
import com.example.rosem.TravelPlanner.object.BriefPlan;
import com.example.rosem.TravelPlanner.plan.Plan;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by rosem on 2017-01-09.
 */
public class RecommendFragment extends android.support.v4.app.Fragment {

    RecyclerView planList;
    PlanCardListAdapter mAdapter;
    int page = 0;
    int loadNum = 2;
    String country;
    Typeface fontType;
    Realm db;

    public RecommendFragment()
    {

    }

    public static RecommendFragment newInstance(Typeface fontType) {

        Bundle args = new Bundle();

        RecommendFragment fragment = new RecommendFragment();
        fragment.setFontType(fontType);
        return fragment;
    }

    public void setFontType(Typeface type)
    {
        this.fontType = type;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = Realm.getDefaultInstance();
        Plan favorite = db.where(Plan.class).equalTo("isFavorite",true).findFirst();
        if(favorite!=null)
        {
            GetRecommendAsync getRecommend = new GetRecommendAsync(favorite.getCountry());
            getRecommend.execute();
        }
        else
        {
            GetRecentAsync getRecent = new GetRecentAsync();
            getRecent.execute();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.recommend_fragment,container,false);

        planList = (RecyclerView)view.findViewById(R.id.recommend_plan_list);
        mAdapter = new PlanCardListAdapter(getContext(), fontType, new PlanCardListAdapter.OnCardClickListener() {
            @Override
            public void onCardClick(long id) {
                Intent intent = new Intent(getContext(), RecommendPlanDetailActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
        planList.setAdapter(mAdapter);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        planList.setLayoutManager(manager);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(db!=null)
        {
            db.close();
        }
    }

    private class GetRecentAsync extends AsyncTask<Call<ResponseBody>, Void, String>
    {
        @Override
        protected String doInBackground(Call<ResponseBody>... calls) {
            GetRecentPlanService service  = GetRecentPlanService.retrofit.create(GetRecentPlanService.class);
            Call<ResponseBody> call = service.getRecentPlans(page, loadNum);
            ResponseBody response = null;
            String result = null;
            try
            {
                response = call.execute().body();
                result = response.string();
            }catch(Exception e)
            {
                e.printStackTrace();
                return null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<BriefPlan>>(){}.getType();
            ArrayList<BriefPlan> list = gson.fromJson(s,type);
            mAdapter.addPlanList(list);
            //Toast.makeText(getContext(), "finish", Toast.LENGTH_SHORT).show();
        }
    }

    private class GetRecommendAsync extends AsyncTask<Call<ResponseBody>, Void, String>
    {
        String country;
        public GetRecommendAsync(String country)
        {
            this.country = country;
        }
        @Override
        protected String doInBackground(Call<ResponseBody>... params) {
            GetRecommendService service = GetRecommendService.retrofit.create(GetRecommendService.class);
            Call<ResponseBody> call = service.getRecommend(country,page,loadNum);
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
            Type type = new TypeToken<ArrayList<BriefPlan>>(){}.getType();
            ArrayList<BriefPlan> list = gson.fromJson(s,type);
            mAdapter.addPlanList(list);
            //Toast.makeText(getContext(), "finish", Toast.LENGTH_SHORT).show();
        }
    }
}
