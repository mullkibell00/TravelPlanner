package com.example.rosem.TravelPlanner.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.object.BriefPlan;
import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by bianca on 2017-05-22.
 */

public class PlanCardListAdapter extends RecyclerView.Adapter{

    ArrayList<BriefPlan> planList = new ArrayList<>();
    Context context;
    Typeface fontType;
    OnCardClickListener listener;
    private int PROGRESS = 1;
    private int PLAN = 0;

    public PlanCardListAdapter(Context context, Typeface fontType, OnCardClickListener listener)
    {
        this.context = context;
        this.fontType = fontType;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==PLAN)
        {
            View cardview = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_card_item,null);
            return new ViewHolder(cardview);
        }
        else
        {
            View cardview = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_progress,null);
            return new ProgressViewHolder(cardview);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder)
        {
            final BriefPlan plan = planList.get(position);
            if(plan!=null)
            {
                ((ViewHolder)holder).planName.setText(plan.getPlanName());
                ((ViewHolder)holder).country.setText(plan.getCountry());
                ((ViewHolder)holder).numOfDay.setText("Day "+Integer.toString(plan.getNumOfDay()));
                ((ViewHolder)holder).item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onCardClick(plan.getId());
                    }
                });
            }
        }
        else
        {
            ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);
        }

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

    public void showLoading()
    {
        planList.add(null);
        notifyItemInserted(planList.size()-1);
    }

    public void hideLoading()
    {
        planList.remove(planList.size()-1);
        notifyItemRemoved(planList.size());
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

            TypedArray imgArray = context.getResources().obtainTypedArray(R.array.random_card_img);
            Random random = new Random();
            int imgNum = random.nextInt(19-1)+1;
            String url = context.getString(imgArray.getResourceId(imgNum,R.string.i1));
            try {
                Glide.with(context).load(url).override(350,160).error(R.mipmap.loading_fail)
                        .placeholder(R.mipmap.loading).thumbnail(0.1f).into(background);
            }catch (Exception e)
            {
                FirebaseCrash.report(e);
            }
            Glide.with(context).load(url).override(350,160).centerCrop().error(R.mipmap.loading_fail)
                    .placeholder(R.mipmap.loading).thumbnail(0.1f).into(background);
            imgArray.recycle();
        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder
    {
        public ProgressBar progressBar;
        public ProgressViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar)itemView.findViewById(R.id.list_progress);
        }
    }

    public interface OnCardClickListener
    {
        public void onCardClick(long id);
    }
}
