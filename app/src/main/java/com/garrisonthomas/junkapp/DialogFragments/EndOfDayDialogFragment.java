package com.garrisonthomas.junkapp.dialogfragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.garrisonthomas.junkapp.DialogFragmentHelper;
import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.Utils;

public class EndOfDayDialogFragment extends DialogFragmentHelper {

    SharedPreferences preferences;
    EditText endOfDayNotes;
    Button cancel, archive, dEndTime, nEndTime;
    private String firebaseJournalRef, driver, navigator, loadString, fuelString;
    private String[] endLoadArray, endFuelArray;
    private int percentOfGoal, totalGrossProfit, percentOnDumps, totalDumpCost;
    private ProgressDialog pDialog;
    private Spinner endLoad, endFuel;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Archive Journal");

        Bundle eodBundle = getArguments();
        percentOfGoal = eodBundle.getInt("percentOfGoal");
        totalGrossProfit = eodBundle.getInt("totalGrossProfit");
        percentOnDumps = eodBundle.getInt("percentOnDumps");
        totalDumpCost = eodBundle.getInt("totalDumpCost");

        driver = eodBundle.getString("driver");
        navigator = eodBundle.getString("navigator");
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.end_of_day_layout, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());

        firebaseJournalRef = preferences.getString("firebaseRef", null);

        endOfDayNotes = (EditText) v.findViewById(R.id.end_day_notes);

        cancel = (Button) v.findViewById(R.id.cancel_publish_dialog);
        archive = (Button) v.findViewById(R.id.archive_journal);
        dEndTime = (Button) v.findViewById(R.id.driver_end_time);
        dEndTime.setText(driver);
        dEndTime.setTransformationMethod(null);
        nEndTime = (Button) v.findViewById(R.id.nav_end_time);
        nEndTime.setText(navigator);
        nEndTime.setTransformationMethod(null);

        endLoadArray = v.getResources().getStringArray(R.array.end_day_load);
        endFuelArray = v.getResources().getStringArray(R.array.end_day_fuel);

        endLoad = (Spinner) v.findViewById(R.id.end_day_load);
        endFuel = (Spinner) v.findViewById(R.id.end_day_fuel);

        endLoad.setAdapter(new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_spinner_item, endLoadArray));
        endLoad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                loadString = endLoadArray[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        endFuel.setAdapter(new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_spinner_item, endFuelArray));
        endFuel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                fuelString = endFuelArray[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.chooseTime(getActivity(), dEndTime);
            }
        });

        nEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.chooseTime(getActivity(), nEndTime);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String DET = dEndTime.getText().toString();
                final String NET = nEndTime.getText().toString();

                if (!DET.equals(driver) && !NET.equals(navigator)) {
                    AlertDialog diaBox = confirmJournalArchive();
                    diaBox.show();
                } else {
                    Toast.makeText(getActivity(), "Please clock out first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setCancelable(false);
        return v;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private AlertDialog confirmJournalArchive() {

        return new AlertDialog.Builder(getActivity())
                .setTitle("Archive Journal?")
                .setMessage("You will no longer be able to view or edit this journal")
                .setIcon(R.drawable.ic_warning_white_24dp)

                .setPositiveButton("archive", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        dialog.dismiss();
                        archiveJournal();

                    }

                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
    }

    public void archiveJournal() {

        final String DET = dEndTime.getText().toString();
        final String NET = nEndTime.getText().toString();

        pDialog = ProgressDialog.show(getActivity(), null,
                "Archiving journal...", true);

        Firebase fbrJournal = new Firebase(firebaseJournalRef);

        fbrJournal.child("driverEndTime").setValue(DET);
        fbrJournal.child("navEndTime").setValue(NET);
        fbrJournal.child("percentOfGoal").setValue(percentOfGoal);
        fbrJournal.child("totalGrossProfit").setValue(totalGrossProfit);
        fbrJournal.child("percentOnDumps").setValue(percentOnDumps);
        fbrJournal.child("totalDumpCost").setValue(totalDumpCost);
        fbrJournal.child("endOfDayNotes").setValue("Load: " + loadString + ". Fuel: " + fuelString +
                ". Notes: " + endOfDayNotes.getText().toString());
        fbrJournal.child("archived").setValue(true, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("firebaseRef", null);
                editor.apply();
                Toast.makeText(getActivity(), "Journal successfully archived",
                        Toast.LENGTH_SHORT).show();

                pDialog.dismiss();

                getActivity().finish();
            }
        });
    }
}