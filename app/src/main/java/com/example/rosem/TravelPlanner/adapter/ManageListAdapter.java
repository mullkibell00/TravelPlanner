package com.example.rosem.TravelPlanner.adapter;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import java.util.ArrayList;

/**
 * Created by rosem on 2017-02-25.
 */

public class ManageListAdapter extends RecyclerView.Adapter<ManageListAdapter.ViewHolder> {

    private Typeface fontType;
    private ArrayList<String> planList;
    public ManageListAdapter() {
        super();
    }

    @Override
    public ManageListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ManageListAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener
    {
        public CheckedTextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            //textView = (CheckedTextView)itemView.findViewById(R.id.country_list_item);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            //listner.
            return true;
        }
    }




}
