package com.example.rosem.TravelPlanner.course;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.rosem.TravelPlanner.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rosem on 2017-01-30.
 */
public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.ViewHolder> {
    private Context mContext;
    private List<Course> mItems = new ArrayList<Course>();
    private Typeface fontType;

    public CourseListAdapter(Context context)
    {
        mContext = context;
        fontType = Typeface.createFromAsset(context.getAssets(),context.getString(R.string.font_name));
    }

    @Override
    public CourseListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CourseView course = new CourseView(mContext);

        ViewHolder viewHolder = new ViewHolder(course);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CourseListAdapter.ViewHolder holder, int position) {
        holder.course.setCourse(mItems.get(position));
        holder.course.setTypeface(fontType);
        holder.course.setIndex(position);
        if(position==0)
        {
            holder.course.setCostTimeVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public CourseView course;

        public ViewHolder(View itemView) {
            super(itemView);
            course = (CourseView)itemView;
            //itemView.setOnClickListener(this);
        }
    }

    public int getPosition(Course contents)
    {
        return mItems.indexOf(contents);
    }

    public void addCourse(Course course)
    {
        mItems.add(course);
    }
}