package com.garrisonthomas.junkapp;

import android.content.DialogInterface;
import android.os.Bundle;
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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class CurrentJournal extends BaseActivity {

    private static Button addJob, viewJob, addDump, addFuel;
    private static TextView todaysCrew, todaysTruck;
    private static Toolbar mToolbar;
    private static ProgressBar toolbarProgressSpinner;
    private static Spinner jobsSpinner;
    private static String currentJournalId;
    private static ArrayList<Integer> jobsArray;
    private static int selectedJobSSID;
    private static Bundle extras;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.current_journal_layout);

        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);

        toolbarProgressSpinner = (ProgressBar) findViewById(R.id.toolbar_progress_spinner);
        toolbarProgressSpinner.setVisibility(View.VISIBLE);

        mToolbar.setTitle(todaysDate);
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(CurrentJournal.this, R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentJournal.this.finish();
            }
        });

        jobsArray = new ArrayList<>();

        addJob = (Button) findViewById(R.id.journal_add_job);
        viewJob = (Button) findViewById(R.id.btn_view_job);
        addDump = (Button) findViewById(R.id.journal_add_dump);
        addFuel = (Button) findViewById(R.id.journal_add_fuel);

        todaysCrew = (TextView) findViewById(R.id.tv_todays_crew);
        todaysTruck = (TextView) findViewById(R.id.tv_todays_truck);

        jobsSpinner = (Spinner) findViewById(R.id.jobs_spinner);

        extras = getIntent().getExtras();
        if (extras != null) {
            String extraCrew = extras.getString("EXTRA_CREW");
            todaysCrew.setText(extraCrew);
            String extraTruckNumber = extras.getString("EXTRA_TRUCK_NUMBER");
            todaysTruck.setText(extraTruckNumber);
            currentJournalId = extras.getString("EXTRA_DJ_ID");
        }

        // Adds today's jobs (if any) to the spinner
        populateSpinner();

        jobsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedJobSSID = (int) jobsSpinner.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        addJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.app.FragmentManager manager = getFragmentManager();
                AddJobDialogFragment djFragment = new AddJobDialogFragment();
                djFragment.show(manager, "Dialog");

            }
        });

        viewJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewJobDialogFragment vjDialogFragment = new ViewJobDialogFragment();
                Bundle vjBundle = new Bundle();
                vjBundle.putInt("spinnerSSID", selectedJobSSID);
                vjDialogFragment.setArguments(vjBundle);
                android.app.FragmentManager manager = getFragmentManager();
                vjDialogFragment.show(manager, "Dialog");

            }
        });

        addDump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.app.FragmentManager manager = getFragmentManager();
                AddDumpDialogFragment djFragment = new AddDumpDialogFragment();
                djFragment.show(manager, "Dialog");

            }
        });

        addFuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.app.FragmentManager manager = getFragmentManager();
                AddFuelDialogFragment djFragment = new AddFuelDialogFragment();
                djFragment.show(manager, "Dialog");

            }
        });

    }

    public void populateSpinner() {

        ParseQuery<NewJob> query = ParseQuery.getQuery(NewJob.class);
        query.whereEqualTo("relatedJournal", currentJournalId);
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


                }
                toolbarProgressSpinner.setVisibility(View.INVISIBLE);
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

    @Override
    public void onResume() {

        super.onResume();

    }

    public void deleteJournal() {

        toolbarProgressSpinner.setVisibility(View.VISIBLE);

        ParseQuery<DailyJournal> query = ParseQuery.getQuery(DailyJournal.class);
        ParseQuery<NewJob> query1 = ParseQuery.getQuery(NewJob.class);
        query.whereEqualTo("objectId", currentJournalId);
        query.setLimit(1);
        query1.whereEqualTo("relatedJournal", currentJournalId);
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

                    Toast.makeText(CurrentJournal.this, "Journal successfully deleted",
                            Toast.LENGTH_SHORT).show();
                    extras.clear();
                    toolbarProgressSpinner.setVisibility(View.GONE);
                    CurrentJournal.this.finish();

                }
            }
        });

    }
}