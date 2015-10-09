package com.garrisonthomas.junkapp;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by GarrisonThomas on 2015-10-08.
 */
public class Utils {

    public static void hideKeyboard(View v, Activity a) {

        InputMethodManager imm = (InputMethodManager) a.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

    }
}
