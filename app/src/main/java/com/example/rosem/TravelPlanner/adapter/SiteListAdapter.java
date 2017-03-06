package com.example.rosem.TravelPlanner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.rosem.TravelPlanner.object.Site;

import java.util.ArrayList;

/**
 * Created by rosem on 2017-03-06.
 */

public class SiteListAdapter extends RecyclerView.Adapter<SiteListAdapter.ViewHolder> {

    ArrayList<Site> siteList = null;
    Context context;

    public SiteListAdapter(Context ctx, ArrayList<Site> site)
    {
        super();
        context = ctx;
        if(site==null)
        {
            siteList = new ArrayList<Site>();
        }
        else
        {
            siteList = site;
        }
    }

    @Override
    public SiteListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(SiteListAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return siteList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
