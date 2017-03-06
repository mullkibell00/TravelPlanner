package com.example.rosem.TravelPlanner.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.activity.CreatePlanActivity;

/**
 * Created by rosem on 2017-03-06.
 */

public class InputSiteFragment extends Fragment {

    Typeface fontType;
    RecyclerView siteListView;
    TextView addSiteButton;

    static public InputSiteFragment newInstance()
    {
        InputSiteFragment fragment = new InputSiteFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fontType = ((CreatePlanActivity)getActivity()).getFontType();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.plan_input_site,container,false);

        siteListView = (RecyclerView)view.findViewById(R.id.plan_site_list);
        addSiteButton = (TextView)view.findViewById(R.id.plan_site_add);
        addSiteButton.setTypeface(fontType);

        return view;
    }
}
