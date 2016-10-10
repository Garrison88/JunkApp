package com.garrisonthomas.junkapp.dialogfragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.garrisonthomas.junkapp.CurrentJournal;
import com.garrisonthomas.junkapp.DialogFragmentHelper;
import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.entryobjects.DailyJournalObject;

public class DailyJournalDialogFragment extends DialogFragmentHelper {

    private Spinner truckSpinner;
    private String[] truckArray;
    private static Button cancel, createJournal, dStartTime, nStartTime;
    private EditText etDriver, etNavigator;
    private String truckSelected;
    private SharedPreferences preferences;
    private ProgressDialog pDialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(todaysDate);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.add_daily_journal_layout, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        truckSpinner = (Spinner) v.findViewById(R.id.truck_spinner);
        truckArray = v.getResources().getStringArray(R.array.truck_number);
        createJournal = (Button) v.findViewById(R.id.btn_create_journal);
        cancel = (Button) v.findViewById(R.id.btn_cancel_journal);
        dStartTime = (Button) v.findViewById(R.id.driver_start_time);
        dStartTime.setTransformationMethod(null);
        nStartTime = (Button) v.findViewById(R.id.nav_start_time);
        nStartTime.setTransformationMethod(null);

        etDriver = (EditText) v.findViewById(R.id.et_driver);
        etNavigator = (EditText) v.findViewById(R.id.et_navigator);

        truckSpinner.setAdapter(new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_spinner_item, truckArray));
        truckSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                truckSelected = truckArray[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                truckSelected = truckArray[0];
            }
        });


        dStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePickerDialog(getActivity(), dStartTime);
            }
        });

        nStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePickerDialog(getActivity(), nStartTime);
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etDriver.setText("");
                etNavigator.setText("");
                truckSpinner.setSelection(0);
                dismiss();
            }
        });

        createJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (truckSpinner.getSelectedItemPosition() != 0) {

                    pDialog = ProgressDialog.show(getActivity(), null,
                            "Creating journal...", true);

                    final String firebaseJournalRef = firebaseURL + "journals/" +
                            currentYear + "/" + currentMonth + "/" + currentDay + "/T" + truckSelected + "/";

                    final Firebase fbrJournal = new Firebase(firebaseJournalRef);

                    fbrJournal.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.exists()) {

                                pDialog.dismiss();
                                Toast.makeText(getActivity(), "A journal for this truck already exists",
                                        Toast.LENGTH_SHORT).show();

                            } else if (!snapshot.exists()) {

                                createJournal(fbrJournal, firebaseJournalRef);

                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });

                } else {
                    Toast.makeText(getActivity(), "Please select a truck", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setCancelable(false);
        return v;

    }

    private void createJournal(Firebase firebaseRef, final String firebaseJournalRef) {

        if (!TextUtils.isEmpty(etDriver.getText()) &&
                !dStartTime.getText().equals("Start")) {

            final String driverString = etDriver.getText().toString();
            final String navigatorString = etNavigator.getText().toString();
            final String driverST = dStartTime.getText().toString();
            final String navST = nStartTime.getText().toString();

//                    fbrJournal.child("journalAuthor").setValue(auth.getCurrentUser().getEmail());

            DailyJournalObject journal = new DailyJournalObject();

            journal.setDate(todaysDate);
            journal.setDriver(driverString);
            journal.setDriverStartTime(driverST);
            journal.setNavigator(navigatorString);
            journal.setNavStartTime(navST);
            journal.setTruckNumber(truckSelected);

            firebaseRef.setValue(journal, new Firebase.CompletionListener() {

                @Override
                public void onComplete(FirebaseError firebaseError, Firebase firebase) {

                    if (firebaseError != null) {

                        System.out.println("Data could not be saved. " + firebaseError.getMessage());

                    } else {

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("firebaseRef", firebaseJournalRef);
                        editor.apply();

                        // hamfisted way of clearing info from dailyJournalDialogFragment...
                        etDriver.setText("");
                        etNavigator.setText("");
                        truckSpinner.setSelection(0);

                        pDialog.dismiss();
                        dismiss();
                        Intent intent = new Intent(getActivity(), CurrentJournal.class);
                        startActivity(intent);

                    }
                }
            });

        } else {
            Toast.makeText(getActivity(), "Please enter at minimum a driver and start time", Toast.LENGTH_SHORT).show();
        }

    }
}