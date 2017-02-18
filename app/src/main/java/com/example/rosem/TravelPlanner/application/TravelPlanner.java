package com.example.rosem.TravelPlanner.application;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by rosem on 2017-02-18.
 */

public class TravelPlanner extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
