package com.garrisonthomas.junkapp;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Garrison on 2016-06-11.
 */
public class DialogFragmentHelper extends DialogFragment {

    public String firebaseURL;
    public FirebaseAuth auth = FirebaseAuth.getInstance();
    public Date date = new Date();
    public SimpleDateFormat year = new SimpleDateFormat("yyyy", Locale.CANADA);
    public SimpleDateFormat month = new SimpleDateFormat("MMM", Locale.CANADA);
    public SimpleDateFormat day = new SimpleDateFormat("dd", Locale.CANADA);
    public SimpleDateFormat fullDate = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.CANADA);
    public String currentYear = year.format(date);
    public String currentMonth = month.format(date);
    public String currentDay = day.format(date);
    public String todaysDate = fullDate.format(date);

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // This helps to always show cancel and save button when keyboard is open
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        firebaseURL = "https://junkapp-43226.firebaseio.com/";

    }

    public static void deleteItem(final DialogFragment df, final String firebaseRef) {

        AlertDialog.Builder builder = new AlertDialog.Builder(df.getActivity());
        builder.setMessage("Delete this entry?")
                .setCancelable(false)
                .setIcon(R.drawable.ic_delete_black_24px)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {

                        Firebase ref = new Firebase(firebaseRef);
                        ref.removeValue();
                        Toast.makeText(df.getActivity(), "Entry deleted", Toast.LENGTH_SHORT).show();
                        df.dismiss();

                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

}
