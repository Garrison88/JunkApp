package com.garrisonthomas.junkapp;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.garrisonthomas.junkapp.dialogfragments.AddFuelDialogFragment;
import com.garrisonthomas.junkapp.dialogfragments.AddJobDialogFragment;
import com.garrisonthomas.junkapp.dialogfragments.AddQuoteDialogFragment;
import com.garrisonthomas.junkapp.dialogfragments.DumpTabHost;
import com.garrisonthomas.junkapp.dialogfragments.EndOfDayDialogFragment;
import com.garrisonthomas.junkapp.dialogfragments.ViewDumpDialogFragment;
import com.garrisonthomas.junkapp.dialogfragments.ViewFuelDialogFragment;
import com.garrisonthomas.junkapp.dialogfragments.ViewJobDialogFragment;
import com.garrisonthomas.junkapp.dialogfragments.ViewQuoteDialogFragment;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
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
    @Bind(R.id.floating_action_menu)
    FloatingActionMenu FAM;
    @Bind(R.id.btn_add_job)
    FloatingActionButton addJob;
    @Bind(R.id.btn_view_job)
    Button viewJob;
    @Bind(R.id.btn_add_quote)
    FloatingActionButton addQuote;
    @Bind(R.id.btn_view_quote)
    Button viewQuote;
    @Bind(R.id.btn_add_dump)
    FloatingActionButton addDump;
    @Bind(R.id.btn_view_dump)
    Button viewDump;
    @Bind(R.id.btn_view_fuel)
    Button viewFuel;
    @Bind(R.id.btn_add_fuel)
    FloatingActionButton addFuel;
    @Bind(R.id.jobs_spinner)
    Spinner jobsSpinner;
    @Bind(R.id.quotes_spinner)
    Spinner quotesSpinner;
    @Bind(R.id.dumps_spinner)
    Spinner dumpsSpinner;
    @Bind(R.id.fuel_spinner)
    Spinner fuelSpinner;

    String firebaseJournalRef, spDriver, spNavigator, spDate, dumpSpinnerText, fuelReceiptNumber, spTruck;

    int selectedJobSID, selectedQuoteSID, dumpReceiptNumber;

    private ArrayList<Integer> jobsArray;
    private ArrayList<Integer> quotesArray;
    private ArrayList<String> dumpsArray;
    private ArrayList<String> fuelArray;

    private static SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.current_journal_layout);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        firebaseJournalRef = preferences.getString("firebaseRef", "none");
        spDriver = preferences.getString("driver", "noDriver");
        spNavigator = preferences.getString("navigator", "noNavigator");
        spTruck = preferences.getString("truck", "none");
        spDate = preferences.getString("todaysDate", "noDate");

        getSupportActionBar().setTitle(spDate);

        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentJournal.this.finish();
            }
        });

        jobsArray = new ArrayList<>();
        quotesArray = new ArrayList<>();
        dumpsArray = new ArrayList<>();
        fuelArray = new ArrayList<>();

        // Populate the various spinners with the available entries

//        Utils.populateQuoteSpinner(this, currentJournalId, quotesArray, quotesSpinner);
//        Utils.populateDumpSpinner(this, currentJournalId, dumpsArray, dumpsSpinner);
//        Utils.populateFuelSpinner(this, currentJournalId, fuelArray, fuelSpinner);

        String crewString = "Driver: " + spDriver + "\n" + "Nav: " + spNavigator;
        todaysCrew.setText(crewString);
        todaysTruck.setText("Truck #: " + spTruck);


        Firebase jobs = new Firebase(firebaseJournalRef + "jobs");
        jobs.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {

                jobsArray.add(Integer.valueOf(snapshot.getKey()));

                ArrayAdapter<Integer> adapter = new ArrayAdapter<>(CurrentJournal.this,
                        android.R.layout.simple_spinner_item, jobsArray);
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                jobsSpinner.setAdapter(adapter);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                jobsArray.remove(Integer.valueOf(dataSnapshot.getKey()));

                ArrayAdapter<Integer> adapter = new ArrayAdapter<>(CurrentJournal.this,
                        android.R.layout.simple_spinner_item, jobsArray);
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                jobsSpinner.setAdapter(adapter);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase fuel = new Firebase(firebaseJournalRef + "fuel");
        fuel.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {

                fuelArray.add(snapshot.getKey());

                ArrayAdapter<String> adapter = new ArrayAdapter<>(CurrentJournal.this,
                        android.R.layout.simple_spinner_item, fuelArray);
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                fuelSpinner.setAdapter(adapter);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                fuelArray.remove(dataSnapshot.getKey());

                ArrayAdapter<String> adapter = new ArrayAdapter<>(CurrentJournal.this,
                        android.R.layout.simple_spinner_item, fuelArray);
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                fuelSpinner.setAdapter(adapter);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase dumps = new Firebase(firebaseJournalRef + "dumps");
        dumps.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {

                dumpsArray.add(snapshot.getKey());

                ArrayAdapter<String> adapter = new ArrayAdapter<>(CurrentJournal.this,
                        android.R.layout.simple_spinner_item, dumpsArray);
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                dumpsSpinner.setAdapter(adapter);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                dumpsArray.remove(dataSnapshot.getKey());

                ArrayAdapter<String> adapter = new ArrayAdapter<>(CurrentJournal.this,
                        android.R.layout.simple_spinner_item, dumpsArray);
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                dumpsSpinner.setAdapter(adapter);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        jobsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedJobSID = (int) jobsSpinner.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        quotesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedQuoteSID = (int) quotesSpinner.getItemAtPosition(position);

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
                while (m.find()) {
                    dumpReceiptNumber = Integer.parseInt(m.group(1));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fuelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                fuelReceiptNumber = (String) fuelSpinner.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager = getSupportFragmentManager();
                AddJobDialogFragment djFragment = new AddJobDialogFragment();
                djFragment.show(manager, "Dialog");
                FAM.close(false);

            }
        });

        viewJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (jobsArray.size() > 0) {

                    ViewJobDialogFragment vjDialogFragment = new ViewJobDialogFragment();
                    Bundle vjBundle = new Bundle();
                    vjBundle.putInt("jobSpinnerSID", selectedJobSID);
                    vjBundle.putString("firebaseJournalRef", firebaseJournalRef);
                    vjDialogFragment.setArguments(vjBundle);
                    FragmentManager manager = getSupportFragmentManager();
                    vjDialogFragment.show(manager, "Dialog");

                } else {

                    Toast.makeText(CurrentJournal.this, "No jobs to display",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

        addQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager = getSupportFragmentManager();
                AddQuoteDialogFragment djFragment = new AddQuoteDialogFragment();
                djFragment.show(manager, "Dialog");
                FAM.close(false);

            }
        });

        viewQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (quotesArray.size() > 0) {

                    ViewQuoteDialogFragment vqDialogFragment = new ViewQuoteDialogFragment();
                    Bundle vqBundle = new Bundle();
                    vqBundle.putInt("quoteSpinnerSID", selectedQuoteSID);
                    vqBundle.putString("firebaseJournalRef", firebaseJournalRef);
                    vqDialogFragment.setArguments(vqBundle);
                    FragmentManager manager = getSupportFragmentManager();
                    vqDialogFragment.show(manager, "Dialog");

                } else {

                    Toast.makeText(CurrentJournal.this, "No quotes to display",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

        addDump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager = getSupportFragmentManager();
                DumpTabHost djFragment = new DumpTabHost();
                djFragment.show(manager, "Dialog");
                FAM.close(false);

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
                    vdBundle.putString("firebaseJournalRef", firebaseJournalRef);
                    vdDialogFragment.setArguments(vdBundle);
                    FragmentManager manager = getSupportFragmentManager();
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

                FragmentManager manager = getSupportFragmentManager();
                AddFuelDialogFragment djFragment = new AddFuelDialogFragment();
                djFragment.show(manager, "Dialog");
                FAM.close(false);

            }
        });

        viewFuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fuelArray.size() > 0) {

                    ViewFuelDialogFragment vfDialogFragment = new ViewFuelDialogFragment();
                    Bundle vfBundle = new Bundle();
                    vfBundle.putString("fuelReceiptNumber", fuelReceiptNumber);
                    vfBundle.putString("firebaseJournalRef", firebaseJournalRef);
                    vfDialogFragment.setArguments(vfBundle);
                    FragmentManager manager = getSupportFragmentManager();
                    vfDialogFragment.show(manager, "Dialog");

                } else {

                    Toast.makeText(CurrentJournal.this, "No fuel entries to display",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_email_office).setVisible(false);
        menu.findItem(R.id.action_call_office).setVisible(false);
//        menu.findItem(R.id.action_take_photo).setVisible(false);
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

                FragmentManager manager = getSupportFragmentManager();
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
                .setIcon(R.drawable.ic_warning_white_24dp)

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

        showProgressDialog("Deleting journal...");

        Firebase firebaseRef = new Firebase(firebaseJournalRef);
        firebaseRef.removeValue(new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("universalJournalId", "none");
                editor.putString("firebaseRef", "none");
                editor.putString("driver", "noDriver");
                editor.putString("navigator", "noNavigator");
                editor.putString("truck", "noTruck");
                editor.putString("date", "noDate");
                editor.apply();

                hideProgressDialog();
                Toast.makeText(CurrentJournal.this, "Journal successfully deleted",
                        Toast.LENGTH_SHORT).show();
                CurrentJournal.this.finish();

            }
        });
    }

}