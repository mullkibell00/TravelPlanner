package com.example.rosem.TravelPlanner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.adapter.ManageListAdapter;
import com.example.rosem.TravelPlanner.plan.Plan;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by rosem on 2017-01-09.
 */
public class ManageFragment extends android.support.v4.app.Fragment{
    RecyclerView list;
    ArrayList<String> planList;
    Realm db;
    ImageView addButton;

    public ManageFragment()
    {

    }

    public ManageFragment newInstance()
    {
        ManageFragment fragment = new ManageFragment();

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = Realm.getDefaultInstance();

        planList = new ArrayList<>();

        RealmResults<Plan> results = db.where(Plan.class).findAll();
        for(int i =0; i<results.size();i++)
        {
            Plan p = results.get(i);
            planList.add(p.getPlanName());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.manage_fragment,container,false);

        list = (RecyclerView) view.findViewById(R.id.list_plans);
        ManageListAdapter.PlanLongClickListener planClickListener = new ManageListAdapter.PlanLongClickListener() {
            @Override
            public void planLongClicked(View view, int position) {

            }
        };
        ManageListAdapter adapter = new ManageListAdapter(getContext(),planList,planClickListener);
        list.setAdapter(adapter);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        list.setLayoutManager(manager);

        //set divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(),R.drawable.list_line_divider));
        list.addItemDecoration(dividerItemDecoration);

        addButton = (ImageView)view.findViewById(R.id.manage_add_btn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //popup new activity
            }
        });

        return  view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(db!=null)
        {
            db.close();
        }
    }
}
