package com.example.rosem.TravelPlanner.DayPlanShow;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.rosem.TravelPlanner.PlanListShow.Plan;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rosem on 2017-01-30.
 */

public class DayPlanAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTitles = new ArrayList<>();
    public DayPlanAdapter(FragmentManager fm)
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
        for(int i = 0; i<course.length();i++)
        {
            int curDay = i+1;
            String title = "Day"+Integer.toString(curDay);
            try
            {
                JSONArray day = course.getJSONArray(title);
                mFragments.add(DayPlanListFragment.newInstance(day));
                mFragmentTitles.add(title);
                Log.v("Adapter",title+"\n"+day);
                Log.v("Adapter::","mFragment size::"+mFragments.size());
            } catch (JSONException e) {
                e.printStackTrace();
            }

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
