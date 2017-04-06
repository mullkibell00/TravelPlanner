package com.example.rosem.TravelPlanner.plan;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rosem on 2017-01-30.
 */

public class PlanAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTitles = new ArrayList<>();
    public PlanAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    public void setCourse(Plan plan)
    {
        if(plan!=null)
        {
            for(int i = 0; i<plan.getNumOfDays();i++)
            {
                int curDay = i+1;
                String title = "Day"+Integer.toString(curDay);

                JSONArray day = plan.getDay(i);
                mFragments.add(PlanListFragment.newInstance(day));
                mFragmentTitles.add(title);
                Log.v("Adapter",title+"\n"+day);
                Log.v("Adapter::","mFragment size::"+mFragments.size());
            }
            notifyDataSetChanged();
        }
    }

    /*
    public void addFragment(String string)
    {
        mFragments.add(MyFragment.newInstance(string));
        mFragmentTitles.add(string);
    }
    */
}
