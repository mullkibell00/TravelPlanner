package com.example.rosem.TravelPlanner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.activity.CreatePlanActivity;

/**
 * Created by rosem on 2017-02-26.
 */

public class InputPlanInfoFragment extends Fragment {

    TextView txtArrival;
    TextView txtDeparture;
    TextView txtTravelingCountry;
    TextView selectedCountry;
    TextView selectedArrivalDate;
    TextView selectedArrivalTime;
    TextView selectedDepartDate;
    TextView selectedDepartTime;

    public static InputPlanInfoFragment newInstance()
    {
        InputPlanInfoFragment fragment = new InputPlanInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.plan_input_plan_info,container,false);

        settingTextView(view);


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

    }

    public void settingTextView(ViewGroup view)
    {

    }
}
