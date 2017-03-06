package com.example.rosem.TravelPlanner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.object.Site;
import com.example.rosem.TravelPlanner.view.SiteItemView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by rosem on 2017-03-06.
 */

public class SiteListAdapter extends RecyclerView.Adapter<SiteListAdapter.ViewHolder> {

    ArrayList<Site> siteList = null;
    Context context;
    ShowDialog showDialog = null;

    public SiteListAdapter(Context ctx, ArrayList<Site> site, ShowDialog showDialog)
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
        this.showDialog = showDialog;
    }

    @Override
    public SiteListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SiteItemView itemView = new SiteItemView(context);

        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemView.setLayoutParams(lp);

        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SiteListAdapter.ViewHolder holder, int position) {
        holder.siteView.setSite(siteList.get(position));
    }

    public ArrayList<Site> getSiteList()
    {
        return siteList;
    }

    public void setSiteList(ArrayList<Site> list)
    {
        siteList = list;
    }
    @Override
    public int getItemCount() {
        return siteList.size();
    }

    public int getIndexOf(Site site)
    {
        return siteList.indexOf(site);
    }

    public void addSite(Site site)
    {
        siteList.add(site);
        notifyDataSetChanged();
    }

    public void setVisitStart(int idx, Calendar start)
    {
        siteList.get(idx).setVisitStart(start);
        Log.v("SiteAdapter::","siteList visitStart="+siteList.get(idx).getVisitStart().get(Calendar.HOUR_OF_DAY));
    }

    public void setVisitEnd(int idx, Calendar end)
    {
        siteList.get(idx).setVisitEnd(end);
        Log.v("SiteAdapter::","siteList visitStart="+siteList.get(idx).getVisitEnd().get(Calendar.HOUR_OF_DAY));
    }

    public Site getSite(int idx)
    {
        return siteList.get(idx);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public SiteItemView siteView;
        public RelativeLayout viewLayout;
        public ImageView editButton;
        public ImageView deleteButton;
        public ImageView confirmButton;

        public ViewHolder(View itemView) {
            super(itemView);


            siteView = (SiteItemView)itemView;
            viewLayout = (RelativeLayout)itemView.findViewById(R.id.site_item_layout);
            editButton = (ImageView)itemView.findViewById(R.id.site_item_edit);
            deleteButton = (ImageView)itemView.findViewById(R.id.site_item_delete);
            confirmButton = (ImageView)itemView.findViewById(R.id.site_item_ok);

            viewLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    siteView.setButtonVisibility(View.VISIBLE);
                    return false;
                }
            });

            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(confirmButton.getVisibility()==View.VISIBLE)
                    {
                        siteView.setButtonVisibility(View.INVISIBLE);
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(deleteButton.getVisibility()==View.VISIBLE)
                    {
                        int pos = siteList.indexOf(siteView.getSite());
                        siteView.setButtonVisibility(View.INVISIBLE);
                        siteList.remove(pos);
                        notifyDataSetChanged();
                    }
                }
            });

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(editButton.getVisibility()==View.VISIBLE && showDialog!=null)
                    {
                        showDialog.showDialog(siteList.indexOf(siteView.getSite()));
                    }
                }
            });
        }
    }

    public interface ShowDialog
    {
        public Calendar visitStart = Calendar.getInstance();
        public Calendar visitEnd = Calendar.getInstance();

        public void showDialog(int idx);
    }
}
