package com.example.rosem.TravelPlanner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.plan.Plan;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by rosem on 2017-01-09.
 */
public class ManageFragment extends android.support.v4.app.Fragment{
    ListView list;
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

        list = (ListView)view.findViewById(R.id.list_plans);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.plan_name_list,planList);
        list.setAdapter(adapter);

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
