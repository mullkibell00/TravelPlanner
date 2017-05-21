package com.example.rosem.TravelPlanner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.plan.Plan;
import com.example.rosem.TravelPlanner.view.PlanNameView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by rosem on 2017-02-25.
 */

public class ManageListAdapter extends RecyclerView.Adapter<ManageListAdapter.ViewHolder> {

    private ArrayList<String> planList;
    private Context mContext;
    private boolean visible;
    private PlanClickListener mListener;
    Realm database;

    public ManageListAdapter(Context context, ArrayList<String>planList, PlanClickListener listener, Realm db) {
        super();
        mContext = context;
        this.planList = planList;
        mListener = listener;
        database = db;
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
      //  holder.nameView.setUpIcon(R.mipmap.up);
      //  holder.nameView.setDownIcon(R.mipmap.down);
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
        public ImageView deleteIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            //textView = (CheckedTextView)itemView.findViewById(R.id.country_list_item);
            nameView = (PlanNameView)itemView;
            deleteIcon = (ImageView)nameView.findViewById(R.id.plan_name_delete);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return mListener.planLongClickListener();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.planClickListener();
                }
            });

            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //transaction
                    PlanNameView view = (PlanNameView)v;
                    RealmResults<Plan> result = database.where(Plan.class).equalTo("planName",view.getPlanName()).findAll();

                    database.beginTransaction();
                    result.deleteAllFromRealm();
                    database.commitTransaction();
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

    public interface PlanClickListener
    {
        public boolean planLongClickListener();
        public boolean planClickListener();
    }

}
