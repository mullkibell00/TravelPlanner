package com.example.rosem.TravelPlanner.Fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rosem.TravelPlanner.Activity.RecommendPlanDetailActivity;
import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.adapter.PlanCardListAdapter;
import com.example.rosem.TravelPlanner.object.BriefPlan;

import java.util.ArrayList;

/**
 * Created by rosem on 2017-01-09.
 */
public class RecommendFragment extends android.support.v4.app.Fragment {

    RecyclerView planList;
    PlanCardListAdapter mAdapter;
    int page = 0;
    ArrayList<BriefPlan> initList;
    Typeface fontType;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.recommend_fragment,container,false);

        planList = (RecyclerView)view.findViewById(R.id.recommend_plan_list);
        mAdapter = new PlanCardListAdapter(getContext(), initList, fontType, new PlanCardListAdapter.OnCardClickListener() {
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
}
