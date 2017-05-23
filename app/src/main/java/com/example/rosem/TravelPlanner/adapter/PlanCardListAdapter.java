package com.example.rosem.TravelPlanner.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.object.BriefPlan;

import java.util.ArrayList;

/**
 * Created by bianca on 2017-05-22.
 */

public class PlanCardListAdapter extends RecyclerView.Adapter<PlanCardListAdapter.ViewHolder> {

    ArrayList<BriefPlan> planList = new ArrayList<>();
    Context context;
    Typeface fontType;
    OnCardClickListener listener;

    public PlanCardListAdapter(Context context, Typeface fontType, OnCardClickListener listener)
    {
        this.context = context;
        this.fontType = fontType;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardview = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_card_item,null);
        return new ViewHolder(cardview);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BriefPlan plan = planList.get(position);
        holder.planName.setText(plan.getPlanName());
        holder.country.setText(plan.getCountry());
        holder.numOfDay.setText("Day "+Integer.toString(plan.getNumOfDay()));
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCardClick(plan.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public BriefPlan getPlan(int position)
    {
        return planList.get(position);
    }

    public void addPlan(BriefPlan plan)
    {
        planList.add(plan);
        notifyDataSetChanged();
    }

    public void addPlanList(ArrayList<BriefPlan> list)
    {
        planList.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        CardView item;
        TextView planName;
        TextView country;
        TextView numOfDay;
        ImageView background;

        public ViewHolder(View itemView) {
            super(itemView);
            item = (CardView)itemView.findViewById(R.id.card);
            planName = (TextView)itemView.findViewById(R.id.card_plan_name);
            country = (TextView)itemView.findViewById(R.id.card_country);
            numOfDay = (TextView)itemView.findViewById(R.id.card_num_of_day);
            background = (ImageView)itemView.findViewById(R.id.card_background_img);

            planName.setTypeface(fontType); country.setTypeface(fontType); numOfDay.setTypeface(fontType);
        }
    }

    public interface OnCardClickListener
    {
        public void onCardClick(long id);
    }
}
