package com.garrisonthomas.junkapp;

import android.app.Application;

import com.garrisonthomas.junkapp.parseobjects.DailyJournal;
import com.garrisonthomas.junkapp.parseobjects.NewDump;
import com.garrisonthomas.junkapp.parseobjects.NewFuel;
import com.garrisonthomas.junkapp.parseobjects.NewJob;
import com.garrisonthomas.junkapp.parseobjects.NewQuote;
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

//        Parse.enableLocalDatastore(this);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("gTmFRethcBdeRGLimnNH5pYCdgmxeeMS9EzEdzj3")
                .clientKey(null)
                .server("http://ridofit.herokuapp.com/parse/") // The trailing slash is important.
                .enableLocalDataStore()



        .build()
        );
//        Parse.initialize(this, "vG7mYtAwz0fePvXLkLEh1BsS5P5ZfZMdNhO9sdZR", "z2IzDj8VHgtHGaCsRqYhAAUEq1LzymwnBgBi1U5j");

    }
}
