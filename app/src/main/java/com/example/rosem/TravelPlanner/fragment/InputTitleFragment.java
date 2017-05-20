package com.example.rosem.TravelPlanner.Fragment;

import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.Activity.CreatePlanActivity;

/**
 * Created by rosem on 2017-02-26.
 */

public class InputTitleFragment extends Fragment {

    Typeface fonType;
    EditText inputPlanName;


    public static InputTitleFragment newInstance()
    {
        InputTitleFragment fragment = new InputTitleFragment();

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fonType = ((CreatePlanActivity)getActivity()).getFontType();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.plan_input_title,container,false);

        TextView planNameView = (TextView)view.findViewById(R.id.plan_txt_plan_name);
        inputPlanName = (EditText) view.findViewById(R.id.plan_txt_input_plan_name);
        TextView inputNameEx = (TextView)view.findViewById(R.id.plan_txt_plan_name_explain);
        planNameView.setTypeface(fonType); inputNameEx.setTypeface(fonType); inputPlanName.setTypeface(fonType);

        String prevPlanName = ((CreatePlanActivity)getActivity()).getPlanName();
        if(prevPlanName!=null)
        {
            inputPlanName.setText(prevPlanName);
        }

        LinearLayout screen = (LinearLayout)view.findViewById(R.id.plan_screen_input_title);
        screen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                {
                    if(inputPlanName.isFocused())
                    {
                        Rect region = new Rect();
                        inputPlanName.getGlobalVisibleRect(region);
                        if(!region.contains((int)motionEvent.getX(),(int)motionEvent.getY()))
                        {
                            inputPlanName.clearFocus();
                        }
                    }
                }
                return false;
            }
        });

        Button nextButton = (Button)getActivity().findViewById(R.id.create_plan_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String planName = inputPlanName.getText().toString();

                if(planName.equals(""))
                {
                    Toast.makeText(getContext(), getString(R.string.txt_input_plan_name), Toast.LENGTH_SHORT).show();
                }
                else if(((CreatePlanActivity)getActivity()).checkPlanName(planName))
                {
                    ((CreatePlanActivity)getActivity()).setPlanName(planName);
                    ((CreatePlanActivity)getActivity()).moveNext();
                }
                else
                {
                    Toast.makeText(getContext(),getString(R.string.txt_duplicate_plan_name),Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
