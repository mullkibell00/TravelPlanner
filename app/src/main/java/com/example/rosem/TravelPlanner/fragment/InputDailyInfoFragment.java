package com.example.rosem.TravelPlanner.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.activity.CreatePlanActivity;

/**
 * Created by rosem on 2017-02-28.
 */

public class InputDailyInfoFragment extends Fragment {


    private final int mTextLodging = 0;
    private final int mTextLodgingPlus = 1;
    private final int mTextLodgingYes = 2;
    private final int mTextLodgingNo = 3;

    private final int mDailyInfoTextNum = 4;

    TextView [] texts = new TextView[mDailyInfoTextNum];
    Typeface fontType;

    boolean isHotelReserved = false;
    boolean additionalQuestion = false;//value of addition question

    View selectedContainer;
    ViewStub lodgingStub;
    CheckBox lodgingPlus;
    CheckBox lodgingYes;
    CheckBox lodgingNo;

    public static InputDailyInfoFragment newInstance()
    {
        InputDailyInfoFragment fragment = new InputDailyInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fontType = ((CreatePlanActivity)getActivity()).getFontType();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.plan_input_daily_info,container,false);

        settingTextView(view);

        //stub
        lodgingStub = (ViewStub)view.findViewById(R.id.daily_info_lodging_stub);

        //setting checkbox
        lodgingYes = (CheckBox)view.findViewById(R.id.daily_info_check_yes);
        lodgingNo = (CheckBox)view.findViewById(R.id.daily_info_check_no);
        lodgingPlus = (CheckBox)view.findViewById(R.id.daily_info_check_lodging_plus);
        setPlusVisible(View.INVISIBLE);

        //setting checkbox onCheckListener
        lodgingYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean yes) {
                texts[mTextLodgingYes].setSelected(yes);
                if(yes)
                {
                    isHotelReserved = yes;
                    setPlusVisible(View.VISIBLE);
                    texts[mTextLodgingPlus].setText(getString(R.string.txt_lodging_multiple_selection));
                    lodgingNo.setChecked(false);
                }
                else
                {
                    if(!(lodgingNo.isChecked()))
                    {
                        setPlusVisible(View.INVISIBLE);
                    }
                }
            }
        });
        lodgingNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean no) {
                texts[mTextLodgingNo].setSelected(no);
                if(no)
                {
                    isHotelReserved = !(no);
                    setPlusVisible(View.VISIBLE);
                    texts[mTextLodgingPlus].setText(getString(R.string.txt_lodging_recommendation));
                    lodgingYes.setChecked(false);
                }
                else
                {
                    if(!(lodgingYes.isChecked()))
                    {
                        setPlusVisible(View.INVISIBLE);
                    }
                }
            }
        });

        lodgingPlus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked)
                {
                    if(isHotelReserved)
                    {
                        //inflate view stub
                    }
                    else
                    {
                        //inflate view stub
                    }
                }
                else
                {
                    //view stub invisible
                }
            }
        });

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

    private void setPlusVisible(int visibility)
    {
        texts[mTextLodgingPlus].setVisibility(visibility);
        lodgingPlus.setVisibility(visibility);
    }

    private void settingTextView(ViewGroup view)
    {

        texts[mTextLodging] = (TextView)view.findViewById(R.id.daily_info_lodging_explain);
        texts[mTextLodgingPlus] = (TextView)view.findViewById(R.id.daily_info_txt_lodging_plus);
        texts[mTextLodgingYes] = (TextView)view.findViewById(R.id.daily_info_txt_lodging_yes);
        texts[mTextLodgingNo] = (TextView)view.findViewById(R.id.daily_info_txt_lodging_no);

        for(int i =0; i<mDailyInfoTextNum;i++)
        {
            texts[i].setTypeface(fontType);
        }


        //if there is data -> load
    }

    private void saveData()
    {

    }
}
