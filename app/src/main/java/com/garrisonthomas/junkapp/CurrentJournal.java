package com.garrisonthomas.junkapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class CurrentJournal extends BaseActivity {

    Button addJob, addDump, addFuel;
    TextView todaysCrew, todaysTruck, hereJob;
    Toolbar mToolbar;
    String todaysDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.current_journal_layout);

        todaysDate = DateHelper.getCurrentDate();

        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        mToolbar.setTitle(todaysDate);

        addJob = (Button) findViewById(R.id.journal_add_job);
        addDump = (Button) findViewById(R.id.journal_add_dump);
        addFuel = (Button) findViewById(R.id.journal_add_fuel);

        todaysCrew = (TextView) findViewById(R.id.tv_todays_crew);
        todaysTruck = (TextView) findViewById(R.id.tv_todays_truck);
        hereJob = (TextView) findViewById(R.id.tv_heres_a_job);


        addJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.app.FragmentManager manager = getFragmentManager();
                AddJobDialogFragment djFragment = new AddJobDialogFragment();
                djFragment.show(manager, "Dialog");

            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String extraCrew = extras.getString("EXTRA_CREW");
            todaysCrew.setText(extraCrew);
            String extraTruckNumber = extras.getString("EXTRA_TRUCK_NUMBER");
            todaysTruck.setText(extraTruckNumber);
        }

        if (isNetworkAvailable()) {

            ParseQuery<DailyJournal> query = ParseQuery.getQuery("DailyJournal");
            query.whereEqualTo("date", DateHelper.getCurrentDate());
            query.whereExists("jobs");
            query.findInBackground(new FindCallback<DailyJournal>() {
                @Override
                public void done(List<DailyJournal> list, ParseException e) {

                    for (DailyJournal newJournal : list) {

//                        newJournal.setCrew(newJournal.getCrew());
//                        newJournal.setTruckNumber(newJournal.getTruckNumber());

                        todaysCrew.setText(newJournal.getCrew());
                        todaysTruck.setText(newJournal.getTruckNumber());
                        hereJob.setText(newJournal.getJobs().toString());

                        newJournal.unpinInBackground();

                        newJournal.pinInBackground();

                    }
                }
            });

        } else {

            ParseQuery<DailyJournal> query = ParseQuery.getQuery("DailyJournal");
            query.fromLocalDatastore();
            query.whereEqualTo("date", DateHelper.getCurrentDate());
            query.whereExists("jobs");
            query.findInBackground(new FindCallback<DailyJournal>() {
                @Override
                public void done(List<DailyJournal> list, ParseException e) {

                    for (DailyJournal newJournal : list) {

//                        newJournal.setCrew(newJournal.getCrew());
//                        newJournal.setTruckNumber(newJournal.getTruckNumber());

                        todaysCrew.setText(newJournal.getCrew());
                        todaysTruck.setText(newJournal.getTruckNumber());
                        hereJob.setText(newJournal.getJobs().toString());

                        newJournal.unpinInBackground();

                        newJournal.pinInBackground();

                    }
                }
            });

        }

    }

    @Override
    public void onResume() {

        super.onResume();


    }

}