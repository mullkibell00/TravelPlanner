package com.example.rosem.TravelPlanner.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.plan.Plan;
import com.example.rosem.TravelPlanner.plan.PlanAdapter;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by rosem on 2017-01-04.
 */
public class FavoriteFragment extends Fragment {
    TabLayout tabs;
    ViewPager pager;
    PlanAdapter mAdapter;
    //sharedPref에서 JSON을 가져올 JSON
    Plan plan;
    FragmentManager fm;
    Realm db;

    public FavoriteFragment()
    {

    }

    public static FavoriteFragment newInstance(FragmentManager fm) {

        Bundle args = new Bundle();

        FavoriteFragment fragment = new FavoriteFragment();

        fragment.setFragmentManager(fm);
        return fragment;
    }
    private void setFragmentManager(FragmentManager fm)
    {
        this.fm = fm;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = Realm.getDefaultInstance();
        RealmResults <Plan> results = db.where(Plan.class).equalTo("isFavorite",true).findAll();
        Log.v("FavoriteFrag:::","resultSize="+results.size());
        plan = results.first();
        if(plan.setPlanArrayFromPlan())
        {
            Log.v("FavoriteFrag:::","plan\n"+plan.toString());
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.favorite_fragment,container,false);

        tabs=(TabLayout)view.findViewById(R.id.tabs);
        pager = (ViewPager)view.findViewById(R.id.pager);
        mAdapter = new PlanAdapter(getChildFragmentManager());
        mAdapter.setCourse(plan);
        pager.setAdapter(mAdapter);

        tabs.setupWithViewPager(pager);

        //end

        return view;
    }

    @Override
    public void onDestroyView() {
        Log.v("Favorite::","destroy view");
       // mAdapter.destroyAll();
        super.onDestroyView();
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
