package com.garrisonthomas.junkapp;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.garrisonthomas.junkapp.dialogfragments.AddDumpDialogFragment;
import com.garrisonthomas.junkapp.dialogfragments.AddFuelDialogFragment;
import com.garrisonthomas.junkapp.dialogfragments.AddJobDialogFragment;
import com.garrisonthomas.junkapp.dialogfragments.AddQuoteDialogFragment;
import com.garrisonthomas.junkapp.dialogfragments.EndOfDayDialogFragment;
import com.garrisonthomas.junkapp.dialogfragments.ViewDumpDialogFragment;
import com.garrisonthomas.junkapp.dialogfragments.ViewJobDialogFragment;
import com.garrisonthomas.junkapp.parseobjects.DailyJournal;
import com.garrisonthomas.junkapp.parseobjects.NewDump;
import com.garrisonthomas.junkapp.parseobjects.NewFuel;
import com.garrisonthomas.junkapp.parseobjects.NewJob;
import com.garrisonthomas.junkapp.parseobjects.NewQuote;
import com.github.clans.fab.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    FloatingActionButton addJob;
    @Bind(R.id.btn_add_quote)
    FloatingActionButton addQuote;
    @Bind(R.id.btn_view_job)
    Button viewJob;
    @Bind(R.id.btn_add_dump)
    FloatingActionButton addDump;
    @Bind(R.id.btn_view_dump)
    Button viewDump;
    @Bind(R.id.btn_add_fuel)
    FloatingActionButton addFuel;
    @Bind(R.id.toolbar_progress_bar)
    ProgressBar toolbarProgressBar;
    @Bind(R.id.jobs_spinner)
    Spinner jobsSpinner;
    @Bind(R.id.dumps_spinner)
    Spinner dumpsSpinner;

    String currentJournalId, spDriver, spNavigator, spTruck, spDate, dumpSpinnerText;
    int selectedJobSID, dumpReceiptNumber;
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
        spDriver = preferences.getString("driver", "noDriver");
        spNavigator = preferences.getString("navigator", "noNavigator");
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

        todaysCrew.setText("Driver: " + spDriver + "\n" + "Nav: " + spNavigator);
        todaysTruck.setText(spTruck);

        jobsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedJobSID = (int) jobsSpinner.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        dumpsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                dumpSpinnerText = (String) dumpsSpinner.getItemAtPosition(position);

                Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(dumpSpinnerText);
                while(m.find()) {
                    dumpReceiptNumber = Integer.parseInt(m.group(1));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Utils.populateJobSpinner(this, currentJournalId, jobsArray, jobsSpinner);
        Utils.populateDumpSpinner(this, currentJournalId, dumpsArray, dumpsSpinner);

        addJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager = getFragmentManager();
                AddJobDialogFragment djFragment = new AddJobDialogFragment();
                djFragment.show(manager, "Dialog");

            }
        });

        addQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager = getFragmentManager();
                AddQuoteDialogFragment djFragment = new AddQuoteDialogFragment();
                djFragment.show(manager, "Dialog");

            }
        });

        viewJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (jobsArray.size() > 0) {

                    ViewJobDialogFragment vjDialogFragment = new ViewJobDialogFragment();
                    Bundle vjBundle = new Bundle();
                    vjBundle.putInt("spinnerSID", selectedJobSID);
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

        viewDump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dumpsArray.size() > 0) {

                    ViewDumpDialogFragment vdDialogFragment = new ViewDumpDialogFragment();
                    Bundle vdBundle = new Bundle();
                    vdBundle.putString("dumpName", dumpSpinnerText);
                    vdBundle.putInt("dumpReceiptNumber", dumpReceiptNumber);
                    vdBundle.putString("relatedJournalId", currentJournalId);
                    vdDialogFragment.setArguments(vdBundle);
                    FragmentManager manager = getFragmentManager();
                    vdDialogFragment.show(manager, "Dialog");

                } else {

                    Toast.makeText(CurrentJournal.this, "No dumps to display",
                            Toast.LENGTH_SHORT).show();

                }
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

                FragmentManager manager = getFragmentManager();
                EndOfDayDialogFragment djFragment = new EndOfDayDialogFragment();
                djFragment.show(manager, "Dialog");

                return false;
            }
        });
        return true;
    }

    private AlertDialog confirmJournalDelete() {

        return new AlertDialog.Builder(CurrentJournal.this)
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


    private void deleteJournal() {

        toolbarProgressBar.setVisibility(View.VISIBLE);

        ParseQuery<DailyJournal> djQuery = ParseQuery.getQuery(DailyJournal.class);
        djQuery.whereEqualTo("objectId", currentJournalId);
        djQuery.setLimit(1);
        final ParseQuery<NewJob> njQuery = ParseQuery.getQuery(NewJob.class);
        njQuery.whereEqualTo("relatedJournal", currentJournalId);
        final ParseQuery<NewQuote> nqQuery = ParseQuery.getQuery(NewQuote.class);
        nqQuery.whereEqualTo("relatedJournal", currentJournalId);
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

                nqQuery.findInBackground(new FindCallback<NewQuote>() {
                    @Override
                    public void done(List<NewQuote> list, ParseException e) {

                        if (e == null) {

                            for (NewQuote nq : list) {

                                try {
                                    nq.delete();
                                    nq.unpin();
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
                editor.putString("driver", "noDriver");
                editor.putString("navigator", "noNavigator");
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

}