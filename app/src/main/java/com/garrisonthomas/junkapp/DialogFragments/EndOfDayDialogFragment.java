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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.Utils;
import com.garrisonthomas.junkapp.ViewItemHelper;
import com.garrisonthomas.junkapp.parseobjects.DailyJournal;
import com.garrisonthomas.junkapp.parseobjects.NewJob;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class EndOfDayDialogFragment extends ViewItemHelper {

    SharedPreferences preferences;
    ProgressBar publishPbar;
    EditText endOfDayNotes;
    Button cancel, archive, dEndTime, nEndTime;
    TextView tvPercentOfGoal;
    String currentJournalId, todaysDate, todaysTruck;
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

        publishPbar = (ProgressBar) v.findViewById(R.id.publish_pbar);

        endOfDayNotes = (EditText) v.findViewById(R.id.end_day_notes);

        cancel = (Button) v.findViewById(R.id.cancel_publish_dialog);
        archive = (Button) v.findViewById(R.id.archive_journal);
        dEndTime = (Button) v.findViewById(R.id.driver_end_time);
        dEndTime.setTransformationMethod(null);
        nEndTime = (Button) v.findViewById(R.id.nav_end_time);
        nEndTime.setTransformationMethod(null);

        tvPercentOfGoal = (TextView) v.findViewById(R.id.tv_end_day_percent_of_goal);

        todaysDate = preferences.getString("todaysDate", "noDate");
        todaysTruck = preferences.getString("truck", "noTruck");

        currentJournalId = preferences.getString("universalJournalId", "none");

        //calculates total profit for day divided by 1400, and sets tvPercentOfGoal to display it
        calculatePercentOfGoal();

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

                if (!DET.equals("DRIVER OUT") && !NET.equals("NAVIGATOR OUT")) {
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

        publishPbar.setVisibility(View.VISIBLE);
        archive.setVisibility(View.GONE);

        final ParseQuery<DailyJournal> djQuery = ParseQuery.getQuery(DailyJournal.class);
        djQuery.whereEqualTo("objectId", currentJournalId);
        djQuery.setLimit(1);
        djQuery.findInBackground(new FindCallback<DailyJournal>() {
            @Override
            public void done(List<DailyJournal> list, ParseException e) {

                if (e == null) {

                    for (final DailyJournal dj : list) {

                        dj.setEndOfDayNotes(endOfDayNotes.getText().toString());
                        dj.setDriverEndTime(DET);
                        dj.setNavEndTime(NET);
                        dj.setPercentOfGoal(percentOfGoal);

                        dj.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                if (e == null) {
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("universalJournalId", "none");
                                    editor.putString("driver", "noDriver");
                                    editor.putString("navigator", "noNavigator");
                                    editor.putString("truck", "noTruck");
                                    editor.putString("date", "noDate");
                                    editor.apply();
                                    Toast.makeText(getActivity(), "Journal successfully archived",
                                            Toast.LENGTH_SHORT).show();
                                    dj.unpinInBackground(new DeleteCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            getActivity().finish();
                                        }


                                });
                            }
                        }
                    });
                }
                }
            }
        });
    }

    public void calculatePercentOfGoal() {
        ParseQuery<NewJob> njQuery = ParseQuery.getQuery(NewJob.class);
        njQuery.whereEqualTo("relatedJournal", currentJournalId);
        njQuery.fromPin();
        njQuery.findInBackground(new FindCallback<NewJob>() {
            @Override
            public void done(List<NewJob> list, ParseException e) {

                if (e == null) {

                    double total = 0.0;

                    for (NewJob nj : list) {

                        total += nj.getGrossSale();

                    }

                    percentOfGoal = (int) ((total / 1400) * 100);
                    tvPercentOfGoal.setText(String.valueOf(percentOfGoal) + "% of goal");
                }
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // This helps to always show cancel and save button when keyboard is open
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
}