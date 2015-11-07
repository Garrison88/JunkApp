package com.garrisonthomas.junkapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.garrisonthomas.junkapp.ParseObjects.NewDump;
import com.garrisonthomas.junkapp.ParseObjects.NewJob;
import com.parse.FindCallback;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GarrisonThomas on 2015-10-08.
 */
public class Utils {

    public static void hideKeyboard(View v, Activity a) {

        InputMethodManager imm = (InputMethodManager) a.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

    }

    public static void showKeyboardInDialog(Dialog d) {

        d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    public static Boolean isInternetAvailable(Activity a) {
        ConnectivityManager cm =
                (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void populateJobSpinner(final Context context, String currentJournalId,
                                          final ArrayList<Integer> jobsArray, final Spinner jobsSpinner) {

        ParseQuery<NewJob> query = ParseQuery.getQuery(NewJob.class);
        query.whereEqualTo("relatedJournal", currentJournalId);
        query.orderByAscending("createdAt");
        query.fromPin();
        query.findInBackground(new FindCallback<NewJob>() {
            @Override
            public void done(List<NewJob> list, com.parse.ParseException e) {

                if (e == null) {

                    for (NewJob job : list) {

                        jobsArray.add(job.getSSID());

                    }

                    ArrayAdapter<Integer> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, jobsArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    jobsSpinner.setAdapter(adapter);


                } else {
                    Toast.makeText(context, "Something went wrong: " + e, Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    public static void populateDumpSpinner(final Context context, String currentJournalId,
                                          final ArrayList<String> dumpsArray, final Spinner dumpsSpinner) {

        ParseQuery<NewDump> query = ParseQuery.getQuery(NewDump.class);
        query.whereEqualTo("relatedJournal", currentJournalId);
        query.orderByAscending("createdAt");
        query.fromPin();
        query.findInBackground(new FindCallback<NewDump>() {
            @Override
            public void done(List<NewDump> list, com.parse.ParseException e) {

                if (e == null) {

                    for (NewDump dump : list) {

                        dumpsArray.add(dump.getDumpName());

                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, dumpsArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    dumpsSpinner.setAdapter(adapter);


                } else {
                    Toast.makeText(context, "Something went wrong: " + e, Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

}
