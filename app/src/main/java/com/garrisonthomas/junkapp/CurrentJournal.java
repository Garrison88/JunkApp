package com.garrisonthomas.junkapp;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.garrisonthomas.junkapp.DialogFragments.AddJobDialogFragment;
import com.parse.FindCallback;
import com.parse.ParseQuery;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CurrentJournal extends BaseActivity {

    Button addJob, addDump, addFuel;
    TextView todaysCrew, todaysTruck;
    Toolbar mToolbar;
    String todaysDate;
    Spinner jobsSpinner;
    String currentJournalId;
    ArrayList<String> jobsArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.current_journal_layout);

        jobsSpinner = (Spinner) findViewById(R.id.jobs_spinner);

        Date date = new Date();
        SimpleDateFormat df2 = new SimpleDateFormat("EEE, dd MMM yyyy");

        todaysDate = df2.format(date);

        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        mToolbar.setTitle(todaysDate);
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(CurrentJournal.this, R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentJournal.this.finish();
            }
        });

        addJob = (Button) findViewById(R.id.journal_add_job);
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

                        addDump.setText(String.valueOf(job.getSSID()));

                    }


                }

            }

        });




        jobsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

//                payTypeString = payTypeArray[position];

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

    }

    @Override
    public void onResume() {

        super.onResume();


    }

}