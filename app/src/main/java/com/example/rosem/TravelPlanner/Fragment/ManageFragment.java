package com.example.rosem.TravelPlanner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.rosem.TravelPlanner.R;

/**
 * Created by rosem on 2017-01-09.
 */
public class ManageFragment extends android.support.v4.app.Fragment{
    ListView list;
    public ManageFragment()
    {

    }

    public ManageFragment newInstance()
    {
        ManageFragment fragment = new ManageFragment();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.manage_fragment,container,false);

        list = (ListView)view.findViewById(R.id.list_plans);

        return  view;
    }
}
