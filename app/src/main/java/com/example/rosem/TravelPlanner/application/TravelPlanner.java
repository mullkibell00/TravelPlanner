package com.example.rosem.TravelPlanner.application;

import android.app.Application;
import android.support.multidex.MultiDexApplication;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by rosem on 2017-02-18.
 */

public class TravelPlanner extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
    }

    public void setTimeText(TextView view, int hour, int min)
    {
        view.setText(hour+"시 "+min+"분");
    }

    public void setTimerText(TextView view, int hour, int min)
    {
        if(hour==0)
        {
            view.setText(min+"분");
        }
        else
        {
            view.setText(hour+"시간 "+min+"분");;
        }
    }

    public void setDateText(TextView view, int year, int month, int day)
    {
        view.setText(year+"년 "+(month+1)+"월 "+day+"일");
    }
}
