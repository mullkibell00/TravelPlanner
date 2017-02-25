package com.example.rosem.TravelPlanner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.view.PlanNameView;

import java.util.ArrayList;

/**
 * Created by rosem on 2017-02-25.
 */

public class ManageListAdapter extends RecyclerView.Adapter<ManageListAdapter.ViewHolder> {

    private ArrayList<String> planList;
    private Context mContext;
    private boolean visible;
    private PlanLongClickListener mListener;

    public ManageListAdapter(Context context, ArrayList<String>planList, PlanLongClickListener listener) {
        super();
        mContext = context;
        this.planList = planList;
        mListener = listener;
    }

    @Override
    public ManageListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        PlanNameView name = new PlanNameView(mContext);

        ViewHolder viewHolder = new ViewHolder(name);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ManageListAdapter.ViewHolder holder, int position) {
        holder.nameView.setPlanName(planList.get(position));
        holder.nameView.setUpIcon(R.mipmap.up);
        holder.nameView.setDownIcon(R.mipmap.down);
        holder.nameView.setDeleteIcon(R.mipmap.delete);
        if(visible)
        {
            holder.nameView.setVisible();
        }
        else
        {
            holder.nameView.setInvisible();
        }
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public PlanNameView nameView;

        public ViewHolder(View itemView) {
            super(itemView);
            //textView = (CheckedTextView)itemView.findViewById(R.id.country_list_item);
            nameView = (PlanNameView)itemView;

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return mListener.planLongClickListener();
                }
            });
        }


    }

    public void toggleVisibility()
    {
        visible = !visible;
        notifyDataSetChanged();
    }

    public void setVisible()
    {
        visible=true;
        notifyDataSetChanged();
    }

    public void setInvisible()
    {
        visible = false;
        notifyDataSetChanged();
    }

    public boolean getVisibility()
    {
        return visible;
    }

    public interface PlanLongClickListener
    {
        public boolean planLongClickListener();
    }

}
