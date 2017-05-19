package com.example.rosem.TravelPlanner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.object.Site;
import com.example.rosem.TravelPlanner.view.RecommendView;

import java.util.ArrayList;

/**
 * Created by rosem on 2017-03-07.
 */

public class RecommendListAdapter extends RecyclerView.Adapter<RecommendListAdapter.ViewHolder> {

    ArrayList<Site> mList;
    Site mSelected;
    Context mContext;
    RecommendView.NotifyMapReady notifyMapReady = null;

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
        mSelected = null;

        notifyMapReady = new RecommendView.NotifyMapReady() {
            @Override
            public void notifyMapReady() {
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public RecommendListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecommendView itemView = new RecommendView(mContext);
        itemView.setNotifyMapReady(notifyMapReady);
        itemView.mapViewOnCreate(null);

        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemView.setLayoutParams(lp);

        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecommendListAdapter.ViewHolder holder, int position) {
        holder.hotel.setSite(mList.get(position));
        if(mList.get(position).equals(mSelected))
        {
            holder.hotel.setSelectionVisibility(View.VISIBLE);
        }
        else
        {
            holder.hotel.setSelectionVisibility(View.INVISIBLE);
        }
        holder.hotel.mapViewOnResume();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public Site getSelected()
    {
        return mSelected;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        RecommendView hotel;
        RelativeLayout viewLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            hotel = (RecommendView)itemView;

            viewLayout = (RelativeLayout)itemView.findViewById(R.id.recommend_view_layout);
            viewLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(hotel.toggleVisibility()) // check표시 한 경우
                    {
                        //mSelected가 이미 있는 경우, 토글해주고 세팅하기
                        if(mSelected!=null &&!(hotel.getSite().equals(mSelected)))
                        {
                            notifyItemChanged(mList.indexOf(mSelected));
                        }
                        mSelected= hotel.getSite();
                    }
                    else
                    {
                        if(hotel.getSite().equals(mSelected))
                        {
                            mSelected= null;
                        }
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }
}
