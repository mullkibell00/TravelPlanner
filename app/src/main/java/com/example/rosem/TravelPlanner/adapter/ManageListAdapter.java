package com.example.rosem.TravelPlanner.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.plan.Plan;
import com.example.rosem.TravelPlanner.view.PlanNameView;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by rosem on 2017-02-25.
 */

public class ManageListAdapter extends RecyclerView.Adapter<ManageListAdapter.ViewHolder> {

    private ArrayList<String> planList;
    private Context mContext;
    private boolean visible;
    private PlanClickListener mListener;
    private DeleteAlertDialog deleteAlert;
    private FavoriteAlertDialog favoriteAlert;
    Realm database;

    public ManageListAdapter(final Context context, final ArrayList<String>planList, PlanClickListener listener, Realm db) {
        super();
        mContext = context;
        this.planList = planList;
        mListener = listener;
        database = db;

        deleteAlert  = new DeleteAlertDialog(context);
        deleteAlert.setMessage(context.getString(R.string.alert_delete));
        deleteAlert.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.txt_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        deleteAlert.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.txt_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //transaction
                Plan result = database.where(Plan.class).equalTo("planName", deleteAlert.planName).findFirst();

                if(result.isFavorite())
                {
                    Toast.makeText(context,context.getString(R.string.cannot_delete_favorite), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    database.beginTransaction();
                    result.deleteFromRealm();
                    database.commitTransaction();
                    Toast.makeText(context, context.getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                    planList.remove(deleteAlert.planName);
                    notifyDataSetChanged();
                }
            }
        });

        favoriteAlert = new FavoriteAlertDialog(context);
        favoriteAlert.setMessage(context.getString(R.string.alert_favorite));
        favoriteAlert.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.txt_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        favoriteAlert.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.txt_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Plan prevPlan = database.where(Plan.class).equalTo("isFavorite",true).findFirst();
                Plan curPlan = database.where(Plan.class).equalTo("planName",favoriteAlert.planName).findFirst();
                if(prevPlan!=null)
                {
                    database.beginTransaction();
                    prevPlan.setFavorite(false);
                    database.copyToRealmOrUpdate(prevPlan);
                    database.commitTransaction();
                }
                database.beginTransaction();
                curPlan.setFavorite(true);
                database.copyToRealmOrUpdate(curPlan);
                database.commitTransaction();
                Toast.makeText(context, context.getString(R.string.alert_favorite_success), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void addItem(String name)
    {
        planList.add(name);
        notifyDataSetChanged();
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
        public ImageView favoriteIcon;
        public ImageView okIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            //textView = (CheckedTextView)itemView.findViewById(R.id.country_list_item);
            nameView = (PlanNameView)itemView;
            deleteIcon = (ImageView)nameView.findViewById(R.id.plan_name_delete);
            okIcon = (ImageView)nameView.findViewById(R.id.plan_name_ok);
            favoriteIcon = (ImageView)nameView.findViewById(R.id.plan_name_favorite);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return mListener.planLongClickListener();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.planClickListener(nameView.getPlanName());
                }
            });

            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteAlert.showDialog(nameView.getPlanName());
                }
            });

            okIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setInvisible();
                }
            });

            favoriteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favoriteAlert.showDialog(nameView.getPlanName());
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
        public boolean planClickListener(String planName);
    }

    private class DeleteAlertDialog extends AlertDialog
    {
        String planName;
        protected DeleteAlertDialog(@NonNull Context context) {
            super(context);
        }

        public void showDialog(String planName)
        {
            this.planName = planName;
            show();
        }
    }

    private class FavoriteAlertDialog extends AlertDialog
    {
        String planName;

        protected FavoriteAlertDialog(@NonNull Context context) {
            super(context);
        }

        public void showDialog(String planName)
        {
            this.planName = planName;
            show();
        }
    }

}
