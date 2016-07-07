package com.garrisonthomas.junkapp;

import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Garrison on 2016-06-11.
 */
public abstract class ViewItemHelper extends DialogFragment {

    public static void deleteItem(final DialogFragment df, final String currentJournalId, final String itemID, final Context context, final String parseClass) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Delete this entry?")
                .setCancelable(false)
                .setIcon(R.drawable.ic_delete_black_24px)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {

                        final ParseQuery<ParseObject> query = ParseQuery.getQuery(parseClass);
                        query.setLimit(1);
                        query.fromPin();
                        query.whereEqualTo("relatedJournal", currentJournalId);
                        query.whereEqualTo("objectId", itemID);

                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {

                                if (e == null) {

                                    for (ParseObject PO : list) {

                                        PO.deleteEventually();
                                    }
                                    Toast.makeText(context, "Item successfully deleted",
                                            Toast.LENGTH_SHORT).show();
                                    df.dismiss();
                                }
                            }
                        });
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

    public void showItemInfo() {


    }

}
