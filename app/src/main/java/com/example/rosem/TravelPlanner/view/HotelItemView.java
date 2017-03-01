package com.example.rosem.TravelPlanner.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.object.Site;

import java.util.Calendar;

/**
 * Created by rosem on 2017-03-01.
 */

public class HotelItemView extends LinearLayout {

    TextView mTextHotelName;
    TextView mTextCheckIn;
    TextView mTextCheckOut;
    Site mHotel;
    Calendar mCheckIn;
    Calendar mCheckOut;
    Context mContext;
    private DatePickerDialog.OnDateSetListener checkInSetListener;
    private DatePickerDialog.OnDateSetListener checkOutSetListener;

    public HotelItemView(Context context) {
        super(context);
        mContext = context;
        mCheckIn = Calendar.getInstance();
        mCheckOut = Calendar.getInstance();

        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.hotel_selection_view,this,true);

        Typeface fontType = Typeface.createFromAsset(context.getAssets(),context.getString(R.string.font_name));
        mTextHotelName = (TextView)findViewById(R.id.hotel_item_name); mTextHotelName.setTypeface(fontType);
        mTextCheckIn = (TextView)findViewById(R.id.hotel_item_checkin); mTextCheckIn.setTypeface(fontType);
        mTextCheckOut = (TextView)findViewById(R.id.hotel_item_checkout); mTextCheckOut.setTypeface(fontType);

        checkInSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mTextCheckIn.setSelected(true);
                mTextCheckIn.setText(year+"년 "
                        +(month+1)+"월 "+day+"일");
                mCheckIn.set(Calendar.YEAR,year); mCheckIn.set(Calendar.MONTH,month);
                mCheckIn.set(Calendar.DAY_OF_MONTH,day);
            }
        };
        checkOutSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mTextCheckOut.setSelected(true);
                mTextCheckOut.setText(year+"년 "
                        +(month+1)+"월 "+day+"일");
                mCheckOut.set(Calendar.YEAR,year); mCheckOut.set(Calendar.MONTH,month);
                mCheckOut.set(Calendar.DAY_OF_MONTH,day);
            }
        };

        //set onclick listener
        mTextCheckIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(),checkInSetListener,
                        mCheckIn.get(Calendar.YEAR),mCheckIn.get(Calendar.MONTH),
                        mCheckIn.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        mTextCheckOut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(),checkOutSetListener,
                        mCheckOut.get(Calendar.YEAR),mCheckOut.get(Calendar.MONTH),
                        mCheckOut.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public void setHotel(Site hotel)
    {
        mHotel = hotel;
        mTextHotelName.setText(mHotel.getPlaceName());
    }
    public Site getHotel()
    {
        return mHotel;
    }
    public void setCheckIn(Calendar checkIn)
    {
        mCheckIn = checkIn;
        mTextCheckIn.setSelected(true);
        mTextCheckIn.setText(mCheckIn.get(Calendar.YEAR)+"년 "
                +(mCheckIn.get(Calendar.MONTH)+1)+"월 "+mCheckIn.get(Calendar.DAY_OF_MONTH)+"일");
    }
    public Calendar getCheckIn()
    {
        return mCheckIn;
    }

    public Calendar getCheckOut() {
        return mCheckOut;
    }

    public void setCheckOut(Calendar checkOut) {
        mCheckOut = checkOut;
        mTextCheckOut.setSelected(true);
        mTextCheckOut.setText(mCheckOut.get(Calendar.YEAR)+"년 "
                +(mCheckOut.get(Calendar.MONTH)+1)+"월 "+mCheckOut.get(Calendar.DAY_OF_MONTH)+"일");
    }
}
