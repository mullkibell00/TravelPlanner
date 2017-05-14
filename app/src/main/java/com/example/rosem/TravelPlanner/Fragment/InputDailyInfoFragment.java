package com.example.rosem.TravelPlanner.Fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rosem.TravelPlanner.Activity.CreatePlanActivity;
import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.object.Time;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.Calendar;

/**
 * Created by bianca on 2017-05-15.
 */

public class InputDailyInfoFragment extends Fragment {


    private final int mTxtExplain = 3;
    private final int mTxtTourStart =4;
    private final int mTxtTourEnd = 5;
    private final int mSelectedTourStart=13;
    private final int mSelectedTourEnd = 14;



    private final int planInfoTextNum=5;
    Typeface fontType;

    TextView[] texts = new TextView[planInfoTextNum];

    //newly added(About tour info)
    private TimePickerDialog.OnTimeSetListener tourStartSetListener;
    private TimePickerDialog.OnTimeSetListener tourEndSetListener;

    //data
    Calendar tourStartTime;
    Calendar tourEndTime;

    public InputDailyInfoFragment newInstance()
    {
        InputDailyInfoFragment fragment = new InputDailyInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void saveData()
    {

    }

    public void settingTextView(ViewGroup view)
    {
        texts[mTxtExplain] = (TextView)view.findViewById(R.id.plan_info_explain);
        texts[mTxtTourStart] =(TextView)view.findViewById(R.id.plan_info_txt_tour_start);
        texts[mTxtTourEnd] = (TextView)view.findViewById(R.id.plan_info_txt_tour_end);
        texts[mSelectedTourStart]=(TextView)view.findViewById(R.id.plan_info_selected_tour_start);
        texts[mSelectedTourEnd]= (TextView)view.findViewById(R.id.plan_info_selected_tour_end);

        for(int i =0; i<planInfoTextNum;i++)
        {
            texts[i].setTypeface(fontType);
        }

        //set onClickListener
        texts[mSelectedTourStart].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getContext(),tourStartSetListener,9,0,false).show();
            }
        });
        texts[mSelectedTourEnd].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getContext(),tourEndSetListener,22,0,false).show();
            }
        });

        Time tourS = null;
        if((tourS=((CreatePlanActivity)getActivity()).getTourStart())!=null)
        {
            Calendar tourSCal = Calendar.getInstance();
            tourSCal.set(Calendar.HOUR_OF_DAY,tourS.hour);
            tourSCal.set(Calendar.MINUTE,tourS.min);
            texts[mSelectedTourStart].setSelected(true);
            //texts[mSelectedTourStart].setText(
            //        tourS.get(Calendar.HOUR_OF_DAY)+"시 "+
            //                tourS.get(Calendar.MINUTE)+"분");
            ((CreatePlanActivity)getActivity()).setTimeText(texts[mSelectedTourStart]
                    ,tourSCal.get(Calendar.HOUR_OF_DAY),tourSCal.get(Calendar.MINUTE));
        }
        Time tourE = null;
        if((tourE=((CreatePlanActivity)getActivity()).getTourEnd())!=null)
        {
            Calendar tourECal = Calendar.getInstance();
            tourECal.set(Calendar.HOUR_OF_DAY,tourS.hour);
            tourECal.set(Calendar.MINUTE,tourS.min);
            texts[mSelectedTourEnd].setSelected(true);
            //texts[mSelectedTourEnd].setText(
            //        tourE.get(Calendar.HOUR_OF_DAY)+"시 "+
            //                tourE.get(Calendar.MINUTE)+"분");
            ((CreatePlanActivity)getActivity()).setTimeText(texts[mSelectedTourEnd]
                    ,tourECal.get(Calendar.HOUR_OF_DAY),tourECal.get(Calendar.MINUTE));
        }
    }
}
