package com.garrisonthomas.junkapp;

import android.app.Application;

import com.firebase.client.Firebase;

public class App extends Application {

    @Override
    public void onCreate() {

        super.onCreate();

        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);

//        Parse.initialize(new Parse.Configuration.Builder(this)
//                .applicationId("gTmFRethcBdeRGLimnNH5pYCdgmxeeMS9EzEdzj3")
//                .clientKey(null)
//                .server("http://ridofit.herokuapp.com/parse/") // The trailing slash is important.
//                .enableLocalDataStore()
//                .build()
//        );
    }
}
