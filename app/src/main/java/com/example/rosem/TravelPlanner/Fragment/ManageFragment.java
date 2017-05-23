package com.example.rosem.TravelPlanner.Fragment;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.rosem.TravelPlanner.Activity.PlanDetailActivity;
import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.Activity.CreatePlanActivity;
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
    ManageListAdapter mAdapter;
    ArrayList<String> planList;
    Realm db;
    ImageView addButton;
    private boolean editMode;

    private final int CREATE_PLAN = 12093;

    public ManageFragment()
    {

    }

    public static ManageFragment newInstance()
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

        ManageListAdapter.PlanClickListener mLongClickListener = new ManageListAdapter.PlanClickListener() {
            @Override
            public boolean planLongClickListener() {
                mAdapter.setVisible();
                Drawable iconOk = ContextCompat.getDrawable(getContext(),R.mipmap.ok);
                iconOk.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorLightButton), PorterDuff.Mode.SRC_IN);
                addButton.setImageDrawable(iconOk);
                editMode=true;
                return false;
            }

            @Override
            public boolean planClickListener(String planName) {
                //start activity
                Intent intent = new Intent(getContext(), PlanDetailActivity.class);
                intent.putExtra("planName",planName);
                startActivity(intent);
                return false;
            }
        };
        mAdapter = new ManageListAdapter(getContext(),planList,mLongClickListener,db);
        list.setAdapter(mAdapter);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        list.setLayoutManager(manager);

        //set divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(),R.drawable.list_line_divider));
        list.addItemDecoration(dividerItemDecoration);

        addButton = (ImageView)view.findViewById(R.id.manage_add_btn);
        Drawable iconAdd = ContextCompat.getDrawable(getContext(),R.mipmap.add);
        iconAdd.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorLightButton), PorterDuff.Mode.SRC_IN);
        addButton.setImageDrawable(iconAdd);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editMode==false)
                {
                    //popup new activity
                    Intent intent= new Intent(getContext(), CreatePlanActivity.class);
                    startActivityForResult(intent,CREATE_PLAN);

                }
                else
                {
                    Drawable iconAdd = ContextCompat.getDrawable(getContext(),R.mipmap.add);
                    iconAdd.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorLightButton), PorterDuff.Mode.SRC_IN);
                    addButton.setImageDrawable(iconAdd);
                    editMode=false;
                    mAdapter.setInvisible();
                }

            }
        });

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(editMode==true&&keyCode == KeyEvent.KEYCODE_BACK)
                {
                    Drawable iconAdd = ContextCompat.getDrawable(getContext(),R.mipmap.add);
                    iconAdd.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorLightButton), PorterDuff.Mode.SRC_IN);
                    addButton.setImageDrawable(iconAdd);
                    editMode=false;
                    mAdapter.setInvisible();
                }
                return false;
            }
        });

        return  view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CREATE_PLAN)
        {
            String name = data.getStringExtra("planName");
            if(name!=null)
            {
                mAdapter.addItem(name);
            }
            //mAdapter.notifyDataSetChanged();
            //list.invalidate();
        }
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
