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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.garrisonthomas.junkapp.dialogfragments.AddFuelDialogFragment;
import com.garrisonthomas.junkapp.dialogfragments.AddJobDialogFragment;
import com.garrisonthomas.junkapp.dialogfragments.AddQuoteDialogFragment;
import com.garrisonthomas.junkapp.dialogfragments.DumpTabHost;
import com.garrisonthomas.junkapp.dialogfragments.EndOfDayDialogFragment;
import com.garrisonthomas.junkapp.dialogfragments.ViewDumpDialogFragment;
import com.garrisonthomas.junkapp.dialogfragments.ViewFuelDialogFragment;
import com.garrisonthomas.junkapp.dialogfragments.ViewJobDialogFragment;
import com.garrisonthomas.junkapp.dialogfragments.ViewQuoteDialogFragment;
import com.garrisonthomas.junkapp.entryobjects.JobObject;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CurrentJournal extends BaseActivity implements View.OnClickListener {

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
    @Bind(R.id.tv_total_income)
    TextView tvTotalIncome;
    @Bind(R.id.tv_percent_of_goal)
    TextView tvPercentOfGoal;

    private String firebaseJournalRef, spDriver, spNavigator, spDate,
            dumpSpinnerText, fuelReceiptNumber, spTruck;

    private int selectedJobSID, selectedQuoteSID, dumpReceiptNumber,
            percentOfGoal, totalGrossProfit;

    private ArrayList<Integer> jobsArray = new ArrayList<>();
    private ArrayList<Integer> quotesArray = new ArrayList<>();
    private ArrayList<String> dumpsArray = new ArrayList<>();
    private ArrayList<String> fuelArray = new ArrayList<>();

    private static SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.current_journal_layout);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        firebaseJournalRef = preferences.getString("firebaseRef", null);
        spDriver = preferences.getString("driver", null);
        spNavigator = preferences.getString("navigator", "noNavigator");
        spTruck = preferences.getString("truck", null);
        spDate = preferences.getString("todaysDate", null);

        viewJob.setOnClickListener(this);
        viewQuote.setOnClickListener(this);
        viewDump.setOnClickListener(this);
        viewFuel.setOnClickListener(this);
        addJob.setOnClickListener(this);
        addQuote.setOnClickListener(this);
        addDump.setOnClickListener(this);
        addFuel.setOnClickListener(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(spDate);
        }
        if (user != null) {
            getSupportActionBar().setSubtitle(user.getEmail());
        }

        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentJournal.this.finish();
            }
        });

        String crewString = "Driver: " + spDriver + "\n" + "Nav: " + spNavigator;
        todaysCrew.setText(crewString);
        String truckString = "Truck #:" + "\n" + spTruck;
        todaysTruck.setText(truckString);

        // populate spinners
        Firebase jobs = new Firebase(firebaseJournalRef + "jobs");
        Utils.populateIntegerSpinner(this, jobs, jobsArray, jobsSpinner);
        Firebase quotes = new Firebase(firebaseJournalRef + "quotes");
        Utils.populateIntegerSpinner(this, quotes, quotesArray, quotesSpinner);
        Firebase dumps = new Firebase(firebaseJournalRef + "dumps");
        Utils.populateStringSpinner(this, dumps, dumpsArray, dumpsSpinner);
        Firebase fuel = new Firebase(firebaseJournalRef + "fuel");
        Utils.populateStringSpinner(this, fuel, fuelArray, fuelSpinner);

        //query grossSale children of SID nodes to find real-time daily profit
        Query queryRef = jobs.orderByChild("sid");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                JobObject jobObject = dataSnapshot.getValue(JobObject.class);
                totalGrossProfit += jobObject.getGrossSale();
                tvTotalIncome.setText("Income: $" + String.valueOf(totalGrossProfit));
                percentOfGoal = (int) (100 * (totalGrossProfit / 1400f));
                tvPercentOfGoal.setText(String.valueOf(percentOfGoal) + "% of goal");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                JobObject jobObject = dataSnapshot.getValue(JobObject.class);
                totalGrossProfit -= jobObject.getGrossSale();
                tvTotalIncome.setText("Income: $" + String.valueOf(totalGrossProfit));
                percentOfGoal = (int) (100 * (totalGrossProfit / 1400f));
                tvPercentOfGoal.setText(String.valueOf(percentOfGoal) + "% of goal");
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

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_email_office).setVisible(false);
        menu.findItem(R.id.action_call_office).setVisible(false);
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
                Bundle eodBundle = new Bundle();
                eodBundle.putString("driver", spDriver);
                eodBundle.putString("navigator", spNavigator);
                eodBundle.putInt("percentOfGoal", percentOfGoal);
                eodBundle.putInt("totalGrossProfit", totalGrossProfit);
                djFragment.setArguments(eodBundle);
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

        Utils.showProgressDialog(this, "Deleting journal...");

        Firebase firebaseRef = new Firebase(firebaseJournalRef);
        firebaseRef.removeValue(new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("firebaseRef", null);
                editor.putString("driver", null);
                editor.putString("navigator", "noNavigator");
                editor.putString("truck", null);
                editor.putString("date", null);
                editor.apply();

                Utils.hideProgressDialog();
                Toast.makeText(CurrentJournal.this, "Journal successfully deleted",
                        Toast.LENGTH_SHORT).show();
                CurrentJournal.this.finish();

            }
        });
    }

    @Override
    public void onClick(View view) {

        FragmentManager manager = getSupportFragmentManager();

        switch (view.getId()) {

            case R.id.btn_view_job:

                if (jobsArray.size() > 0) {

                    ViewJobDialogFragment vjDialogFragment = new ViewJobDialogFragment();
                    Bundle vjBundle = new Bundle();
                    vjBundle.putInt("jobSpinnerSID", selectedJobSID);
                    vjBundle.putString("firebaseJournalRef", firebaseJournalRef);
                    vjDialogFragment.setArguments(vjBundle);
                    vjDialogFragment.show(manager, "Dialog");

                } else {

                    Toast.makeText(CurrentJournal.this, "No jobs to display",
                            Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.btn_view_quote:

                if (quotesArray.size() > 0) {

                    ViewQuoteDialogFragment vqDialogFragment = new ViewQuoteDialogFragment();
                    Bundle vqBundle = new Bundle();
                    vqBundle.putInt("quoteSpinnerSID", selectedQuoteSID);
                    vqBundle.putString("firebaseJournalRef", firebaseJournalRef);
                    vqDialogFragment.setArguments(vqBundle);
                    vqDialogFragment.show(manager, "Dialog");

                } else {

                    Toast.makeText(CurrentJournal.this, "No quotes to display",
                            Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.btn_view_dump:

                if (dumpsArray.size() > 0) {

                    ViewDumpDialogFragment vdDialogFragment = new ViewDumpDialogFragment();
                    Bundle vdBundle = new Bundle();
                    vdBundle.putString("dumpName", dumpSpinnerText);
                    vdBundle.putInt("dumpReceiptNumber", dumpReceiptNumber);
                    vdBundle.putString("firebaseJournalRef", firebaseJournalRef);
                    vdDialogFragment.setArguments(vdBundle);
                    vdDialogFragment.show(manager, "Dialog");

                } else {

                    Toast.makeText(CurrentJournal.this, "No dumps to display",
                            Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.btn_view_fuel:

                if (fuelArray.size() > 0) {

                    ViewFuelDialogFragment vfDialogFragment = new ViewFuelDialogFragment();
                    Bundle vfBundle = new Bundle();
                    vfBundle.putString("fuelReceiptNumber", fuelReceiptNumber);
                    vfBundle.putString("firebaseJournalRef", firebaseJournalRef);
                    vfDialogFragment.setArguments(vfBundle);

                    vfDialogFragment.show(manager, "Dialog");

                } else {

                    Toast.makeText(CurrentJournal.this, "No fuel entries to display",
                            Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.btn_add_job:

                AddJobDialogFragment ajFragment = new AddJobDialogFragment();
                ajFragment.show(manager, "Dialog");
                FAM.close(false);

                break;
            case R.id.btn_add_quote:

                AddQuoteDialogFragment aqFragment = new AddQuoteDialogFragment();
                aqFragment.show(manager, "Dialog");
                FAM.close(false);

                break;
            case R.id.btn_add_dump:

                DumpTabHost dthFragment = new DumpTabHost();
                dthFragment.show(manager, "Dialog");
                FAM.close(false);

                break;
            case R.id.btn_add_fuel:

                AddFuelDialogFragment afFragment = new AddFuelDialogFragment();
                afFragment.show(manager, "Dialog");
                FAM.close(false);

                break;
        }
    }
}