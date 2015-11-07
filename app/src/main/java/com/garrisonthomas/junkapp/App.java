package com.garrisonthomas.junkapp;

import android.app.Application;

import com.garrisonthomas.junkapp.ParseObjects.DailyJournal;
import com.garrisonthomas.junkapp.ParseObjects.NewDump;
import com.garrisonthomas.junkapp.ParseObjects.NewFuel;
import com.garrisonthomas.junkapp.ParseObjects.NewJob;
import com.garrisonthomas.junkapp.ParseObjects.NewQuote;
import com.parse.Parse;
import com.parse.ParseObject;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(NewJob.class);
        ParseObject.registerSubclass(NewDump.class);
        ParseObject.registerSubclass(NewFuel.class);
        ParseObject.registerSubclass(DailyJournal.class);
        ParseObject.registerSubclass(NewQuote.class);

        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "gTmFRethcBdeRGLimnNH5pYCdgmxeeMS9EzEdzj3", "us6N0TVBxNQMDFi75yzpeBRWcQeSNxnjiw3DgyXj");

    }
}
