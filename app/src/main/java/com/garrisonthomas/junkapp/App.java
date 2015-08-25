package com.garrisonthomas.junkapp;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(NewJob.class);
        ParseObject.registerSubclass(DailyJournal.class);

        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "gTmFRethcBdeRGLimnNH5pYCdgmxeeMS9EzEdzj3", "us6N0TVBxNQMDFi75yzpeBRWcQeSNxnjiw3DgyXj");

    }
}
