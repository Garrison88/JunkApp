//package com.garrisonthomas.junkapp;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.support.v7.app.AlertDialog;
//import android.view.View;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import com.garrisonthomas.junkapp.ParseObjects.NewJob;
//import com.parse.FindCallback;
//import com.parse.ParseException;
//import com.parse.ParseObject;
//import com.parse.ParseQuery;
//
//import java.util.List;
//
//public class DeleteEntry {
//
//    private String currentJournalId;
//    private ParseObject pObject;
//
////    public String getCurrentJournalId() {
////        return currentJournalId;
////    }
////
////    public ParseObject getParseObject() {
////        return pObject;
////    }
////
////    public void setCurrentJournalId(String currentJournalId) {
////        this.currentJournalId = currentJournalId;
////    }
////
////    public void setParseObject(ParseObject pObject) {
////        this.pObject = pObject;
////    }
//
//    public void deleteSingleEntry(final Context context, final ProgressBar pbar, final String currentJournalId) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setMessage("Delete this entry?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//
//                        pbar.setVisibility(View.VISIBLE);
//
//                        ParseQuery<pObject> query = ParseQuery.getQuery(pObject);
//                        query.setLimit(1);
//                        query.whereEqualTo("relatedJournal", currentJournalId);
//                        query.whereEqualTo("ssid", vjSSID);
//
//                        query.findInBackground(new FindCallback<NewJob>() {
//                            @Override
//                            public void done(List<NewJob> list, ParseException e) {
//
//                                if (e == null) {
//
//                                    for (NewJob nj : list) {
//
//                                        try {
//                                            nj.delete();
//                                        } catch (ParseException e1) {
//                                            e1.printStackTrace();
//                                        }
//                                    }
//
//                                }
//                                Toast.makeText(context, "Job number " + vjSSID + " successfully deleted",
//                                        Toast.LENGTH_SHORT).show();
//                                ViewJobDialogFragment.this.dismiss();
//                            }
//                        });
//
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.show();
//
//    }
//}