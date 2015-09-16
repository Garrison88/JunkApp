package com.garrisonthomas.junkapp;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.garrisonthomas.junkapp.DialogFragments.AddDumpDialogFragment;
import com.garrisonthomas.junkapp.DialogFragments.AddFuelDialogFragment;
import com.garrisonthomas.junkapp.DialogFragments.AddJobDialogFragment;
import com.garrisonthomas.junkapp.DialogFragments.ViewJobDialogFragment;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.List;

public class CurrentJournal extends BaseActivity {

    Button addJob, viewJob, addDump, addFuel;
    TextView todaysCrew, todaysTruck;
    Toolbar mToolbar;
    Spinner jobsSpinner;
    String currentJournalId;
    ArrayList<Integer> jobsArray;
    int selectedJobSSID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.current_journal_layout);

        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(todaysDate);
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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String extraCrew = extras.getString("EXTRA_CREW");
            todaysCrew.setText(extraCrew);
            String extraTruckNumber = extras.getString("EXTRA_TRUCK_NUMBER");
            todaysTruck.setText(extraTruckNumber);
            currentJournalId = extras.getString("EXTRA_DJ_ID");
        }

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

                    jobsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            int found = (int) jobsSpinner.getItemAtPosition(position);

                            selectedJobSSID = found;

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }

                    });
                }
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_email_office).setVisible(false);
        menu.findItem(R.id.action_call_office).setVisible(false);
        menu.findItem(R.id.action_take_photo).setVisible(false);
        menu.findItem(R.id.action_delete_journal).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                ParseQuery<DailyJournal> query = ParseQuery.getQuery(DailyJournal.class);
                query.whereEqualTo("objectId", currentJournalId);
                query.findInBackground(new FindCallback<DailyJournal>() {
                    @Override
                    public void done(List<DailyJournal> list, com.parse.ParseException e) {

                        if (e == null) {

                            for (DailyJournal dj : list) {

                                dj.deleteInBackground();

                            }

                            CurrentJournal.this.finish();

                        }
                    }
                });

//                ParseQuery<NewJob> query1 = ParseQuery.getQuery(NewJob.class);
//                query1.whereEqualTo("relatedJournal", currentJournalId);
//                query1.findInBackground(new FindCallback<NewJob>() {
//                    @Override
//                    public void done(List<NewJob> list, com.parse.ParseException e) {
//
//                        if (e == null) {
//
//                            for (NewJob nj : list) {
//
//                                nj.deleteInBackground();
//
//                            }
//                        }
//                    }
//                });

                return false;
            }
        });
        return true;
    }

    @Override
    public void onResume() {

        super.onResume();


    }

}