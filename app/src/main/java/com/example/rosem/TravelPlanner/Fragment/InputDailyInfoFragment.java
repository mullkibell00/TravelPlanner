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
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

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


    private final int mTxtExplain = 0;
    private final int mTxtTourStart =1;
    private final int mTxtTourEnd = 2;
    private final int mSelectedTourStart=3;
    private final int mSelectedTourEnd = 4;



    private final int planInfoTextNum=5;
    Typeface fontType;

    TextView[] texts = new TextView[planInfoTextNum];

    //newly added(About tour info)
    private TimePickerDialog.OnTimeSetListener tourStartSetListener;
    private TimePickerDialog.OnTimeSetListener tourEndSetListener;

    //data
    Calendar tourStartTime;
    Calendar tourEndTime;

    static public InputDailyInfoFragment newInstance()
    {
        InputDailyInfoFragment fragment = new InputDailyInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fontType = ((CreatePlanActivity)getActivity()).getFontType();

        tourStartSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                texts[mSelectedTourStart].setSelected(true);
                ((CreatePlanActivity)getActivity()).setTimeText(texts[mSelectedTourStart],hour,minute);
                //texts[mSelectedTourStart].setText(hour+"시 "+minute+"분");
                tourStartTime.set(Calendar.HOUR_OF_DAY,hour);
                tourStartTime.set(Calendar.MINUTE,minute);
            }
        };
        tourEndSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                texts[mSelectedTourEnd].setSelected(true);
                ((CreatePlanActivity)getActivity()).setTimeText(texts[mSelectedTourEnd],hour,minute);
                //texts[mSelectedTourEnd].setText(hour+"시 "+minute+"분");
                tourEndTime.set(Calendar.HOUR_OF_DAY,hour);
                tourEndTime.set(Calendar.MINUTE,minute);
            }
        };

        tourStartTime = Calendar.getInstance();
        tourEndTime = Calendar.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.plan_input_daily_info,container,false);

        settingTextView(view);

        Time tourS = ((CreatePlanActivity)getActivity()).getTourStart();
        Time tourE = ((CreatePlanActivity)getActivity()).getTourEnd();

        if(tourS!=null)
        {
            tourStartTime.set(Calendar.HOUR_OF_DAY,tourS.hour);
            tourStartTime.set(Calendar.MINUTE,tourS.min);
        }
        if(tourE!=null)
        {
            tourEndTime.set(Calendar.HOUR_OF_DAY,tourE.hour);
            tourEndTime.set(Calendar.MINUTE, tourE.min);
        }

        Button prevButton = (Button)getActivity().findViewById(R.id.create_plan_prev);
        Button nextButton = (Button)getActivity().findViewById(R.id.create_plan_next);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                ((CreatePlanActivity)getActivity()).movePrev();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                ((CreatePlanActivity)getActivity()).moveNext();
            }
        });


        return view;
    }

    public void saveData()
    {
        Time startTime = new Time(tourStartTime.get(Calendar.HOUR_OF_DAY),tourStartTime.get(Calendar.MINUTE));
        ((CreatePlanActivity)getActivity()).setTourStart(startTime);
        Time endTime = new Time(tourEndTime.get(Calendar.HOUR_OF_DAY),tourEndTime.get(Calendar.MINUTE));
        ((CreatePlanActivity)getActivity()).setTourEnd(endTime);
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
