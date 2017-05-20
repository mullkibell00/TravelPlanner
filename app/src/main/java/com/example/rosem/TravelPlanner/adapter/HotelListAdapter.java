package com.example.rosem.TravelPlanner.adapter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.object.Site;
import com.example.rosem.TravelPlanner.view.HotelItemView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by rosem on 2017-03-01.
 */

public class HotelListAdapter extends  RecyclerView.Adapter<HotelListAdapter.ViewHolder> {

    private ArrayList<Site> hotelList;
    private ArrayList<Calendar> checkInList;
    private ArrayList<Calendar> checkOutList;
    private Context mContext;
    private DeleteAlertDialog deleteAlert;

    public HotelListAdapter(Context context, ArrayList<Site> list, final ArrayList<Calendar>checkIn, ArrayList<Calendar>checkOut)
    {
        super();
        mContext = context;
        if(list!=null)
        {
            hotelList = list;
        }
        else
        {
            hotelList = new ArrayList<Site>();
        }
        if(checkIn!=null)
        {
            checkInList = checkIn;
        }
        else
        {
            checkInList = new ArrayList<Calendar>();
        }
        if(checkOut!=null)
        {
            checkOutList = checkOut;
        }
        else
        {
            checkOutList = new ArrayList<Calendar>();
        }
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
                hotelList.remove(deleteAlert.pos);
                checkInList.remove(deleteAlert.pos);
                checkOutList.remove(deleteAlert.pos);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public HotelListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HotelItemView name = new HotelItemView(mContext);

        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        name.setLayoutParams(lp);

        ViewHolder viewHolder = new ViewHolder(name);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HotelListAdapter.ViewHolder holder, int position) {
        try
        {
            Log.v("HotelAdapter::","Size="+hotelList.size()+"\nPosition="+position);
            holder.hotelView.setHotel(hotelList.get(position));
            if(checkInList.size()>position)
            {
                Calendar checkIn = checkInList.get(position);
                if(checkIn!=null)
                {
                    holder.hotelView.setCheckIn(checkIn);
                }
            }
            if(checkOutList.size()>position)
            {
                Calendar checkOut = checkOutList.get(position);
                if(checkOut!=null)
                {
                    holder.hotelView.setCheckOut(checkOut);
                }
            }

        }
        catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public void addHotel(Site hotel)
    {
        hotelList.add(hotel);
        checkInList.add(null);
        checkOutList.add(null);
        notifyDataSetChanged();
    }

    public void setHotelList(ArrayList<Site> list)
    {
        hotelList = list;
    }
    public ArrayList<Site> getHotelList()
    {
        return hotelList;
    }

    public void setCheckInList(ArrayList<Calendar> list)
    {
        checkInList = list;
    }
    public ArrayList<Calendar> getCheckInList()
    {
        return checkInList;
    }
    public void setCheckOutList(ArrayList<Calendar> list)
    {
        checkOutList = list;
    }
    public ArrayList<Calendar> getCheckOutList()
    {
        return checkOutList;
    }

    public int getIndexOf(Site hotel)
    {
        return hotelList.indexOf(hotel);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public HotelItemView hotelView;
        //public TextView hotelName;
        public TextView hotelCheckIn;
        public TextView hotelCheckOut;
        public CheckInSetListener checkInSetListener;
        public CheckOutSetListener checkOutSetListener;

        public ViewHolder(View itemView) {
            super(itemView);

            hotelView = (HotelItemView)itemView;

            hotelCheckIn = (TextView)hotelView.findViewById(R.id.hotel_item_checkin);
            hotelCheckOut = (TextView)hotelView.findViewById(R.id.hotel_item_checkout);

            checkInSetListener = new CheckInSetListener();
            checkOutSetListener = new CheckOutSetListener();

            hotelCheckIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar cal = Calendar.getInstance();
                    new DatePickerDialog(mContext,checkInSetListener,
                            cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
            hotelCheckOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar cal = Calendar.getInstance();
                    new DatePickerDialog(mContext,checkOutSetListener,
                            cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
            hotelView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deleteAlert.showDialog(hotelList.indexOf(hotelView.getHotel()));
                    return false;
                }
            });
        }

        public class CheckInSetListener implements DatePickerDialog.OnDateSetListener {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar checkIn = Calendar.getInstance();
                checkIn.set(Calendar.YEAR,year); checkIn.set(Calendar.MONTH,month);
                checkIn.set(Calendar.DAY_OF_MONTH,day);
                hotelView.setCheckIn(checkIn);

                int position = getIndexOf(hotelView.getHotel());
                Log.v("HotelAdapter::","position="+position+"\nhotelName="+hotelView.getHotelName());
                Log.v("HotelAdapter::","hotelList="+hotelList.size());
                if(checkInList.size()>position)
                {
                    checkInList.set(position,checkIn);
                }
                else if(checkInList.size()==position)
                {
                    checkInList.add(checkIn);
                }
                else
                {

                }

            }
        }
        public class CheckOutSetListener implements DatePickerDialog.OnDateSetListener {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar checkOut = Calendar.getInstance();
                checkOut.set(Calendar.YEAR,year); checkOut.set(Calendar.MONTH,month);
                checkOut.set(Calendar.DAY_OF_MONTH,day);
                hotelView.setCheckOut(checkOut);

                int position = getIndexOf(hotelView.getHotel());
                if(checkOutList.size()>position)
                {
                    checkOutList.set(position,checkOut);
                }
                else if(checkOutList.size()==position)
                {
                    checkOutList.add(checkOut);
                }
                else
                {

                }
            }
        }
    }

    private class DeleteAlertDialog extends AlertDialog
    {
        int pos = 0;
        protected DeleteAlertDialog(@NonNull Context context) {
            super(context);
        }

        public void showDialog(int pos)
        {
            this.pos = pos;
            show();
        }
    }
}
