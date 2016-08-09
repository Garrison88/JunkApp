package com.garrisonthomas.junkapp.dialogfragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.garrisonthomas.junkapp.DialogFragmentHelper;
import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.Utils;

public class EndOfDayDialogFragment extends DialogFragmentHelper {

    SharedPreferences preferences;
    EditText endOfDayNotes;
    Button cancel, archive, dEndTime, nEndTime;
    TextView tvPercentOfGoal;
    String currentJournalId, todaysDate, firebaseJournalURL;
    Firebase fbrJournal;
    int percentOfGoal;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Archive Journal");
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.end_of_day_layout, container, false);


        preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());

        endOfDayNotes = (EditText) v.findViewById(R.id.end_day_notes);

        cancel = (Button) v.findViewById(R.id.cancel_publish_dialog);
        archive = (Button) v.findViewById(R.id.archive_journal);
        dEndTime = (Button) v.findViewById(R.id.driver_end_time);
        dEndTime.setTransformationMethod(null);
        nEndTime = (Button) v.findViewById(R.id.nav_end_time);
        nEndTime.setTransformationMethod(null);

        tvPercentOfGoal = (TextView) v.findViewById(R.id.tv_end_day_percent_of_goal);

        todaysDate = preferences.getString("todaysDate", "noDate");
        currentJournalId = preferences.getString("universalJournalId", "none");
        firebaseJournalURL = preferences.getString("firebaseRef", "none");

        //calculates total profit for day divided by 1400, and sets tvPercentOfGoal to display it
//        calculatePercentOfGoal();

        dEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.chooseTime(getActivity(), dEndTime);
            }
        });

        nEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.chooseTime(getActivity(), nEndTime);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String DET = dEndTime.getText().toString();
                final String NET = nEndTime.getText().toString();

                if (!DET.equals("Driver Out") && !NET.equals("Nav Out")) {
                    AlertDialog diaBox = confirmJournalArchive();
                    diaBox.show();
                } else {
                    Toast.makeText(getActivity(), "Please clock out first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setCancelable(false);
        return v;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private AlertDialog confirmJournalArchive() {

        return new AlertDialog.Builder(getActivity())
                .setTitle("Archive Journal?")
                .setMessage("You will no longer be able to view or edit this journal")
                .setIcon(R.drawable.ic_warning_white_24dp)

                .setPositiveButton("archive", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        dialog.dismiss();
                        archiveJournal();

                    }

                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
    }

    public void archiveJournal() {

        final String DET = dEndTime.getText().toString();
        final String NET = nEndTime.getText().toString();

        showProgressDialog("Archiving Journal...");

        fbrJournal = new Firebase(firebaseJournalURL);

        fbrJournal.child("driverEndTime").setValue(DET);
        fbrJournal.child("navEndTime").setValue(NET);
        fbrJournal.child("percentOfGoal").setValue(percentOfGoal);
        fbrJournal.child("endOfDayNotes").setValue(endOfDayNotes.getText().toString());
        fbrJournal.child("archived").setValue(true, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("firebaseRef", "none");
                editor.putString("driver", "noDriver");
                editor.putString("navigator", "noNavigator");
                editor.putString("truck", "none");
                editor.putString("date", "noDate");
                editor.apply();
                Toast.makeText(getActivity(), "Journal successfully archived",
                        Toast.LENGTH_SHORT).show();

                hideProgressDialog();
                getActivity().finish();
            }
        });

//        DailyJournalObject journal = new DailyJournalObject();
//
//        journal.setDriverEndTime(DET);
//        journal.setNavEndTime(NET);
//        journal.setPercentOfGoal(percentOfGoal);
//        journal.setEndOfDayNotes(endOfDayNotes.getText().toString());
//        journal.setArchived(true);
//
//        fbrJournal.setValue(journal, new Firebase.CompletionListener() {
//            @Override
//            public void onComplete(FirebaseError firebaseError, Firebase firebase) {

            }
//        });


//
//        final ParseQuery<DailyJournal> djQuery = ParseQuery.getQuery(DailyJournal.class);
//        djQuery.whereEqualTo("objectId", currentJournalId);
//        djQuery.setLimit(1);
//        djQuery.findInBackground(new FindCallback<DailyJournal>() {
//            @Override
//            public void done(List<DailyJournal> list, ParseException e) {
//
//                if (e == null) {
//
//                    for (final DailyJournal dj : list) {
//
//                        dj.setEndOfDayNotes(endOfDayNotes.getText().toString());
//                        dj.setDriverEndTime(DET);
//                        dj.setNavEndTime(NET);
//                        dj.setPercentOfGoal(percentOfGoal);
//                        dj.setArchived(true);
//
//                        dj.saveInBackground(new SaveCallback() {
//                            @Override
//                            public void done(ParseException e) {
//
//                                if (e == null) {
//                                    SharedPreferences.Editor editor = preferences.edit();
//                                    editor.putString("firebaseURL", "none");
//                                    editor.putString("universalJournalId", "none");
//                                    editor.putString("driver", "noDriver");
//                                    editor.putString("navigator", "noNavigator");
//                                    editor.putString("truck", "none");
//                                    editor.putString("date", "noDate");
//                                    editor.apply();
//                                    Toast.makeText(getActivity(), "Journal successfully archived",
//                                            Toast.LENGTH_SHORT).show();
//                                    dj.unpinInBackground(new DeleteCallback() {
//                                        @Override
//                                        public void done(ParseException e) {
//
//                                        }
//
//
//                                });
//                            }
//                        }
//                    });
//                }
//                }
//            }
//        });
//    }

//    public void calculatePercentOfGoal() {
//        ParseQuery<NewJob> njQuery = ParseQuery.getQuery(NewJob.class);
//        njQuery.whereEqualTo("relatedJournal", currentJournalId);
//        njQuery.fromPin();
//        njQuery.findInBackground(new FindCallback<NewJob>() {
//            @Override
//            public void done(List<NewJob> list, ParseException e) {
//
//                if (e == null) {
//
//                    double total = 0.0;
//
//                    for (NewJob nj : list) {
//
//                        total += nj.getGrossSale();
//
//                    }
//
//                    String totalString = String.valueOf(total);
//                    percentOfGoal = (int) ((total / 1400) * 100);
//                    String percentOfGoalString = "$" + totalString + " profit (" + String.valueOf(percentOfGoal) + "% of goal)";
//                    tvPercentOfGoal.setText(percentOfGoalString);
//                }
//            }
//        });
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // This helps to always show cancel and save button when keyboard is open
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
}