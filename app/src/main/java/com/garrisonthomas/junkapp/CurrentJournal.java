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
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class CurrentJournal extends BaseActivity {

    private TextView todaysCrew, todaysTruck;
    private static ProgressBar toolbarProgressBar;
    private static Spinner jobsSpinner;
    private String currentJournalId, spCrew, spTruck, spDate;
    private ArrayList<Integer> jobsArray;
    private int selectedJobSSID;
    private static SharedPreferences preferences;
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.current_journal_layout);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        currentJournalId = preferences.getString("universalJournalId", "none");
        spCrew = preferences.getString("crew", "noCrew");
        spTruck = preferences.getString("truck", "noTruck");
        spDate = preferences.getString("todaysDate", "noDate");

        getSupportActionBar().setTitle(spDate);

        toolbarProgressBar = (ProgressBar) findViewById(R.id.toolbar_progress_bar);

        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha));
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

        toolbarProgressBar.setVisibility(View.VISIBLE);

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
                toolbarProgressBar.setVisibility(View.GONE);
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

                AlertDialog diaBox = confirmJournalDelete();
                diaBox.show();

                return false;
            }
        });
        menu.findItem(R.id.action_publish_journal).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                AlertDialog diaBox = confirmJournalPublish();
                diaBox.show();

                return false;
            }
        });
        return true;
    }

    private AlertDialog confirmJournalDelete() {

        return new AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirm_journal_delete_title))
                .setMessage(getString(R.string.confirm_journal_delete_message))
                .setIcon(R.drawable.ic_delete_black_24px)

                .setPositiveButton("delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        dialog.dismiss();
                        deleteJournal();

                    }

                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
    }

    private AlertDialog confirmJournalPublish() {

        return new AlertDialog.Builder(this)
                .setTitle("Finalize Journal?")
                .setMessage("You will no longer be able to view or edit this journal")
                .setIcon(R.drawable.ic_publish_black_24px)

                .setPositiveButton("publish", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        publishJournal();

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

    private void deleteJournal() {

        toolbarProgressBar.setVisibility(View.VISIBLE);

        ParseQuery<DailyJournal> djQuery = ParseQuery.getQuery(DailyJournal.class);
        djQuery.whereEqualTo("objectId", currentJournalId);
        djQuery.setLimit(1);
        final ParseQuery<NewJob> njQuery = ParseQuery.getQuery(NewJob.class);
        njQuery.whereEqualTo("relatedJournal", currentJournalId);
        final ParseQuery<NewDump> ndQuery = ParseQuery.getQuery(NewDump.class);
        ndQuery.whereEqualTo("relatedJournal", currentJournalId);
        final ParseQuery<NewFuel> nfQuery = ParseQuery.getQuery(NewFuel.class);
        nfQuery.whereEqualTo("relatedJournal", currentJournalId);

        djQuery.findInBackground(new FindCallback<DailyJournal>() {
            @Override
            public void done(List<DailyJournal> list, ParseException e) {

                if (e == null) {

                    for (DailyJournal dj : list) {

                        try {
                            dj.delete();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    }
                }

                njQuery.findInBackground(new FindCallback<NewJob>() {
                    @Override
                    public void done(List<NewJob> list, ParseException e) {

                        if (e == null) {

                            for (NewJob nj : list) {

                                try {
                                    nj.delete();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    }
                });

                ndQuery.findInBackground(new FindCallback<NewDump>() {
                    @Override
                    public void done(List<NewDump> list, ParseException e) {

                        if (e == null) {

                            for (NewDump nd : list) {
                                try {
                                    nd.delete();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    }
                });

                nfQuery.findInBackground(new FindCallback<NewFuel>() {
                    @Override
                    public void done(List<NewFuel> list, ParseException e) {

                        if (e == null) {

                            for (NewFuel nf : list) {

                                try {
                                    nf.delete();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    }
                });

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("universalJournalId", "none");
                editor.putString("crew", "noCrew");
                editor.putString("truck", "noTruck");
                editor.putString("date", "noDate");
                editor.commit();
                toolbarProgressBar.setVisibility(View.GONE);
                Toast.makeText(CurrentJournal.this, "Journal successfully deleted",
                        Toast.LENGTH_SHORT).show();
                CurrentJournal.this.finish();

            }
        });
    }

    public void publishJournal() {

        toolbarProgressBar.setVisibility(View.VISIBLE);

        final ParseQuery<DailyJournal> djQuery = ParseQuery.getQuery(DailyJournal.class);
        djQuery.whereEqualTo("objectId", currentJournalId);
        djQuery.setLimit(1);
        djQuery.findInBackground(new FindCallback<DailyJournal>() {
            @Override
            public void done(List<DailyJournal> list, ParseException e) {

                if (e == null) {

                    for (final DailyJournal dj : list) {

                        ParseQuery<NewJob> njQuery = ParseQuery.getQuery(NewJob.class);
                        njQuery.whereEqualTo("relatedJournal", currentJournalId);
                        njQuery.findInBackground(new FindCallback<NewJob>() {
                            @Override
                            public void done(List<NewJob> list, ParseException e) {

                                if (e == null) {

                                    double total = 0.0;

                                    for (NewJob nj : list) {

                                        total = total + nj.getGrossSale();

                                    }

                                    int intTotal = (int) ((total / 1400) * 100);
                                    dj.setPercentOfGoal(intTotal);

                                    dj.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {

                                            if (e == null) {
                                                SharedPreferences.Editor editor = preferences.edit();
                                                editor.putString("universalJournalId", "none");
                                                editor.putString("crew", "noCrew");
                                                editor.putString("truck", "noTruck");
                                                editor.putString("date", "noDate");
                                                editor.apply();
                                                toolbarProgressBar.setVisibility(View.GONE);
                                                Toast.makeText(CurrentJournal.this, "Journal successfully published",
                                                        Toast.LENGTH_SHORT).show();
                                                CurrentJournal.this.finish();
                                            }
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
}