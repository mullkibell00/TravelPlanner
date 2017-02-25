package com.example.rosem.TravelPlanner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rosem.TravelPlanner.R;

import java.util.ArrayList;

/**
 * Created by rosem on 2017-02-25.
 */

public class ManageListAdapter extends RecyclerView.Adapter<ManageListAdapter.ViewHolder> {

    private ArrayList<String> planList;
    private Context mContext;
    private static PlanLongClickListener mListener;

    public ManageListAdapter(Context context, ArrayList<String>planList, PlanLongClickListener listener) {
        super();
        mContext = context;
        this.planList = planList;
        this.mListener = listener;
    }

    @Override
    public ManageListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_name_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ManageListAdapter.ViewHolder holder, int position) {
        holder.planName.setText(planList.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener
    {
        public TextView planName;
        public ImageView upIcon;
        public ImageView downIcon;
        public ImageView deleteIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            //textView = (CheckedTextView)itemView.findViewById(R.id.country_list_item);
            planName = (TextView)itemView.findViewById(R.id.plan_name);
            upIcon = (ImageView)itemView.findViewById(R.id.plan_name_list_up);
            downIcon = (ImageView)itemView.findViewById(R.id.plan_name_list_down);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            mListener.planLongClicked(view, getLayoutPosition());
            return true;
        }

    }

    public interface PlanLongClickListener{
        public void planLongClicked(View view, int position);
    }


}
