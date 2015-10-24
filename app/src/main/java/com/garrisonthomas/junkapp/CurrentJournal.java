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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.garrisonthomas.junkapp.DialogFragments.AddDumpDialogFragment;
import com.garrisonthomas.junkapp.DialogFragments.AddFuelDialogFragment;
import com.garrisonthomas.junkapp.DialogFragments.AddJobDialogFragment;
//import com.garrisonthomas.junkapp.DialogFragments.ViewDumpDialogFragment;
import com.garrisonthomas.junkapp.DialogFragments.ViewJobDialogFragment;
import com.garrisonthomas.junkapp.ParseObjects.DailyJournal;
import com.garrisonthomas.junkapp.ParseObjects.NewDump;
import com.garrisonthomas.junkapp.ParseObjects.NewFuel;
import com.garrisonthomas.junkapp.ParseObjects.NewJob;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CurrentJournal extends BaseActivity {

    @Bind(R.id.app_bar)
    Toolbar toolbar;
    @Bind(R.id.tv_todays_crew)
    TextView todaysCrew;
    @Bind(R.id.tv_todays_truck)
    TextView todaysTruck;
    @Bind(R.id.btn_add_job)
    Button addJob;
    @Bind(R.id.btn_view_job)
    Button viewJob;
    @Bind(R.id.btn_add_dump)
    Button addDump;
    @Bind(R.id.btn_view_dump)
    Button viewDump;
    @Bind(R.id.btn_add_fuel)
    Button addFuel;
    @Bind(R.id.toolbar_progress_bar)
    ProgressBar toolbarProgressBar;
    @Bind(R.id.jobs_spinner)
    Spinner jobsSpinner;
    @Bind(R.id.dumps_spinner)
    Spinner dumpsSpinner;
    private String currentJournalId, spCrew, spTruck, spDate;
    private int selectedJobSSID;
    private ArrayList<Integer> jobsArray;
    private ArrayList<String> dumpsArray;
    private static SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.current_journal_layout);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        currentJournalId = preferences.getString("universalJournalId", "none");
        spCrew = preferences.getString("crew", "noCrew");
        spTruck = preferences.getString("truck", "noTruck");
        spDate = preferences.getString("todaysDate", "noDate");

        getSupportActionBar().setTitle(spDate);

        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentJournal.this.finish();
            }
        });

        jobsArray = new ArrayList<>();
        dumpsArray = new ArrayList<>();

        todaysCrew.setText(spCrew);
        todaysTruck.setText(spTruck);

        // Adds today's jobs (if any) to the spinner
        Utils.populateSpinner(this, toolbarProgressBar, currentJournalId, jobsArray, jobsSpinner);

        jobsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedJobSSID = (int) jobsSpinner.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        dumpsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                    vjBundle.putString("relatedJournalId", currentJournalId);
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

//        viewDump.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (dumpsArray.size() > 0) {
//
//                    ViewDumpDialogFragment vdDialogFragment = new ViewDumpDialogFragment();
//                    Bundle vdBundle = new Bundle();
//                    vdBundle.putInt("spinnerSSID", selectedJobSSID);
//                    vdBundle.putString("relatedJournalId", currentJournalId);
//                    vdDialogFragment.setArguments(vdBundle);
//                    FragmentManager manager = getFragmentManager();
//                    vdDialogFragment.show(manager, "Dialog");
//
//                } else {
//
//                    Toast.makeText(CurrentJournal.this, "No dumps to display",
//                            Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });

        addFuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager = getFragmentManager();
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
                                    nj.unpin();
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
                                    nd.unpin();
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
                                    nf.unpin();
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
                editor.apply();
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
                        njQuery.fromPin();
                        njQuery.findInBackground(new FindCallback<NewJob>() {
                            @Override
                            public void done(List<NewJob> list, ParseException e) {

                                if (e == null) {

                                    double total = 0.0;

                                    for (NewJob nj : list) {

                                        total = total + nj.getGrossSale();
                                        try {
                                            nj.unpin();
                                        } catch (ParseException e1) {
                                            e1.printStackTrace();
                                        }

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