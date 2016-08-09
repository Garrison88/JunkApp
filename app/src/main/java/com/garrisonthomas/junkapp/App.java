package com.garrisonthomas.junkapp;

import android.app.Application;

import com.firebase.client.Firebase;
import com.garrisonthomas.junkapp.parseobjects.NewDump;
import com.garrisonthomas.junkapp.parseobjects.NewFuel;
import com.garrisonthomas.junkapp.parseobjects.NewQuote;
import com.parse.Parse;
import com.parse.ParseObject;

public class App extends Application {

    @Override
    public void onCreate() {

        super.onCreate();

        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);

        ParseObject.registerSubclass(NewDump.class);
        ParseObject.registerSubclass(NewFuel.class);
        ParseObject.registerSubclass(NewQuote.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("gTmFRethcBdeRGLimnNH5pYCdgmxeeMS9EzEdzj3")
                .clientKey(null)
                .server("http://ridofit.herokuapp.com/parse/") // The trailing slash is important.
                .enableLocalDataStore()
                .build()
        );
    }
}
