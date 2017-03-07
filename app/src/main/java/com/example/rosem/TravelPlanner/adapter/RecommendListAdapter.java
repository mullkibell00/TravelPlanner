package com.example.rosem.TravelPlanner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.rosem.TravelPlanner.object.Site;

import java.util.ArrayList;

/**
 * Created by rosem on 2017-03-07.
 */

public class RecommendListAdapter extends RecyclerView.Adapter<RecommendListAdapter.ViewHolder> {

    ArrayList<Site> mList;
    Context mContext;

    public RecommendListAdapter(Context context, ArrayList<Site> list)
    {
        mContext = context;
        if(list==null)
        {
            mList = new ArrayList<Site>();
        }
        else
        {
            mList = list;
        }
    }

    @Override
    public RecommendListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecommendListAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
