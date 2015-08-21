package com.garrisonthomas.junkapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

/**
 * Created by Garrison Thomas on 2015-08-17.
 */
public class CurrentJournal extends BaseActivity {

    Button addJob, addDump, addFuel;
    TextView todaysCrew;
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

        addJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.app.FragmentManager manager = getFragmentManager();
                AddJobFragment djFragment = new AddJobFragment();
                djFragment.show(manager, "Dialog");

            }
        });

    }

}