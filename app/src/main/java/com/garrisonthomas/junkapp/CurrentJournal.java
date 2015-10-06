package com.garrisonthomas.junkapp;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.garrisonthomas.junkapp.DialogFragments.AddDumpDialogFragment;
import com.garrisonthomas.junkapp.DialogFragments.AddFuelDialogFragment;
import com.garrisonthomas.junkapp.DialogFragments.AddJobDialogFragment;
import com.garrisonthomas.junkapp.DialogFragments.ViewJobDialogFragment;
import com.garrisonthomas.junkapp.ParseObjects.DailyJournal;
import com.garrisonthomas.junkapp.ParseObjects.NewDump;
import com.garrisonthomas.junkapp.ParseObjects.NewFuel;
import com.garrisonthomas.junkapp.ParseObjects.NewJob;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class CurrentJournal extends BaseActivity {

    private TextView todaysCrew, todaysTruck;
    private static ProgressBar toolbarProgressBar;
    private static Spinner jobsSpinner;
    private String currentJournalId, spCrew, spTruck;
    private ArrayList<Integer> jobsArray;
    private int selectedJobSSID;
    private static SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.current_journal_layout);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        currentJournalId = preferences.getString("universalJournalId", "none");
        spCrew = preferences.getString("crew", "noCrew");
        spTruck = preferences.getString("truck", "noTruck");

        toolbarProgressBar = (ProgressBar) findViewById(R.id.toolbar_progress_bar);
        toolbarProgressBar.setVisibility(View.VISIBLE);

        toolbar.setTitle(todaysDate);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(CurrentJournal.this, R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentJournal.this.finish();
            }
        });

        jobsArray = new ArrayList<>();

        final Button addJob = (Button) findViewById(R.id.journal_add_job);
        final Button viewJob = (Button) findViewById(R.id.btn_view_job);
        final Button addDump = (Button) findViewById(R.id.journal_add_dump);
        final Button addFuel = (Button) findViewById(R.id.journal_add_fuel);

        todaysCrew = (TextView) findViewById(R.id.tv_todays_crew);
        todaysTruck = (TextView) findViewById(R.id.tv_todays_truck);
        todaysCrew.setText(spCrew);
        todaysTruck.setText(spTruck);

        jobsSpinner = (Spinner) findViewById(R.id.jobs_spinner);

        // Adds today's jobs (if any) to the spinner
        populateSpinner();

        addJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager = getFragmentManager();
                AddJobDialogFragment djFragment = new AddJobDialogFragment();
                djFragment.show(manager, "Dialog");

            }
        });

        viewJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (jobsArray.size() > 0) {

                    ViewJobDialogFragment vjDialogFragment = new ViewJobDialogFragment();
                    Bundle vjBundle = new Bundle();
                    vjBundle.putInt("spinnerSSID", selectedJobSSID);
                    vjDialogFragment.setArguments(vjBundle);
                    FragmentManager manager = getFragmentManager();
                    vjDialogFragment.show(manager, "Dialog");

                } else {

                    Toast.makeText(CurrentJournal.this, "No jobs to display",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

        addDump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager = getFragmentManager();
                AddDumpDialogFragment djFragment = new AddDumpDialogFragment();
                djFragment.show(manager, "Dialog");

            }
        });

        addFuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager = getFragmentManager();
                AddFuelDialogFragment djFragment = new AddFuelDialogFragment();
                djFragment.show(manager, "Dialog");

            }
        });
    }

    public void populateSpinner() {

        jobsSpinner = (Spinner) findViewById(R.id.jobs_spinner);

        ParseQuery<NewJob> query = ParseQuery.getQuery(NewJob.class);
        query.whereEqualTo("relatedJournal", currentJournalId);
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<NewJob>() {
            @Override
            public void done(List<NewJob> list, com.parse.ParseException e) {

                if (e == null) {

                    for (NewJob job : list) {

                        jobsArray.add(job.getSSID());

                    }

                    ArrayAdapter<Integer> adapter = new ArrayAdapter<>(CurrentJournal.this, android.R.layout.simple_spinner_item, jobsArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    jobsSpinner.setAdapter(adapter);


                } else {
                    Toast.makeText(CurrentJournal.this, "Something went wrong: " + e, Toast.LENGTH_SHORT).show();
                }
                toolbarProgressBar.setVisibility(View.INVISIBLE);
            }

        });

        jobsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedJobSSID = (int) jobsSpinner.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_email_office).setVisible(false);
        menu.findItem(R.id.action_call_office).setVisible(false);
        menu.findItem(R.id.action_take_photo).setVisible(false);
        menu.findItem(R.id.action_delete_journal).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                AlertDialog diaBox = confirmJournalDeletion();
                diaBox.show();

                return false;
            }
        });
        return true;
    }

    private AlertDialog confirmJournalDeletion() {
        return new AlertDialog.Builder(this)

                .setTitle(getString(R.string.confirm_journal_delete_title))
                .setMessage(getString(R.string.confirm_journal_delete_message))
                .setIcon(R.drawable.ic_delete_black_36dp)

                .setPositiveButton("delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        deleteJournal();

                        dialog.dismiss();
                    }

                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        populateSpinner();
//    }

    public void deleteJournal() {

        toolbarProgressBar.setVisibility(View.VISIBLE);

        ParseQuery<DailyJournal> query = ParseQuery.getQuery(DailyJournal.class);
        ParseQuery<NewJob> query1 = ParseQuery.getQuery(NewJob.class);
        ParseQuery<NewDump> query2 = ParseQuery.getQuery(NewDump.class);
        ParseQuery<NewFuel> query3 = ParseQuery.getQuery(NewFuel.class);
        query.whereEqualTo("objectId", currentJournalId);
        query.setLimit(1);
        query1.whereEqualTo("relatedJournal", currentJournalId);
        query2.whereEqualTo("relatedJournal", currentJournalId);
        query3.whereEqualTo("relatedJournal", currentJournalId);
        query.findInBackground(new FindCallback<DailyJournal>() {
            @Override
            public void done(List<DailyJournal> list, ParseException e) {

                if (e == null) {

                    for (DailyJournal dj : list) {

                        dj.deleteInBackground();

                    }
                }
            }
        });

        query1.findInBackground(new FindCallback<NewJob>() {
            @Override
            public void done(List<NewJob> list, ParseException e) {

                if (e == null) {

                    for (NewJob nj : list) {

                        nj.deleteInBackground();

                    }
                }
            }
        });

        query2.findInBackground(new FindCallback<NewDump>() {
            @Override
            public void done(List<NewDump> list, ParseException e) {

                if (e == null) {

                    for (NewDump nd : list) {

                        nd.deleteInBackground();

                    }
                }
            }
        });

        query3.findInBackground(new FindCallback<NewFuel>() {
            @Override
            public void done(List<NewFuel> list, ParseException e) {

                if (e == null) {

                    for (NewFuel nf : list) {

                        nf.deleteInBackground();

                    }
                }
            }
        });

        Toast.makeText(CurrentJournal.this, "Journal successfully deleted",
                Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("universalJournalId", "none");
        editor.putString("crew", "noCrew");
        editor.putString("truck", "noTruck");
        editor.apply();
        toolbarProgressBar.setVisibility(View.GONE);
        CurrentJournal.this.finish();
    }
}