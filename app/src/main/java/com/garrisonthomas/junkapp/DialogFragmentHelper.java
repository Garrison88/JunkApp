package com.garrisonthomas.junkapp;


import android.app.ProgressDialog;
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
    public SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
    public SimpleDateFormat df2 = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.CANADA);
    public String todaysDateNumerical = df1.format(date);
    public String todaysDate = df2.format(date);

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // This helps to always show cancel and save button when keyboard is open
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        firebaseURL = "https://junkapp-43226.firebaseio.com/";

//        Firebase.setAndroidContext(getActivity());

    }

    public ProgressDialog mProgressDialog;

    public void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(message);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
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
                        Toast.makeText(df.getActivity(), "Item deleted", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

}
