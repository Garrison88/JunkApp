package com.garrisonthomas.junkapp.dialogfragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.garrisonthomas.junkapp.CurrentJournal;
import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.Utils;
import com.garrisonthomas.junkapp.parseobjects.DailyJournal;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DailyJournalDialogFragment extends DialogFragment {

    private static Spinner truckSpinner;
    private static String[] truckNumber;
    private static Button cancel, createJournal, dStartTime, nStartTime;
    private EditText driver, navigator;
    private String truckSelected;
    private Date date = new Date();
    private SimpleDateFormat df2 = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.CANADA);
    private String todaysDate = df2.format(date);
    private SharedPreferences preferences;
    private ProgressBar pbar;

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

        preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());

        truckSpinner = (Spinner) v.findViewById(R.id.truck_spinner);
        truckNumber = getResources().getStringArray(R.array.truck_number);
        createJournal = (Button) v.findViewById(R.id.btn_create_journal);
        cancel = (Button) v.findViewById(R.id.cancel_fragment);
        dStartTime = (Button) v.findViewById(R.id.driver_start_time);
        dStartTime.setTransformationMethod(null);
        nStartTime = (Button) v.findViewById(R.id.nav_start_time);
        nStartTime.setTransformationMethod(null);

        driver = (EditText) v.findViewById(R.id.et_driver);
        navigator = (EditText) v.findViewById(R.id.et_navigator);

        pbar = (ProgressBar) v.findViewById(R.id.pbar_create_journal);

        truckSpinner.setAdapter(new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_dropdown_item_1line, truckNumber));
        truckSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                truckSelected = truckNumber[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.chooseTime(getActivity(), dStartTime);
            }
        });

        nStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.chooseTime(getActivity(), nStartTime);
            }
        });

        createJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(driver.getText()) && !dStartTime.getText().equals("START TIME")) {

                    final String driverString = driver.getText().toString();
                    final String navigatorString = navigator.getText().toString();
                    final int truckNumber = Integer.valueOf(truckSelected.substring(6, 7));
                    final String driverST = dStartTime.getText().toString();
                    final String navST = nStartTime.getText().toString();


                    Utils.hideKeyboard(v, getActivity());

                    createJournal.setVisibility(View.GONE);
                    pbar.setVisibility(View.VISIBLE);

                    final DailyJournal newJournal = new DailyJournal();

                    newJournal.setDate(todaysDate);
                    newJournal.setDriver(driverString);
                    newJournal.setDriverStartTime(driverST);
                    newJournal.setNavigator(navigatorString);
                    newJournal.setNavStartTime(navST);
                    newJournal.setTruckNumber(truckNumber);

                    newJournal.pinInBackground();
                    newJournal.saveEventually(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("driver", driverString);
                                editor.putString("navigator", navigatorString);
                                editor.putString("truck", truckSelected);
                                editor.putString("universalJournalId", newJournal.getObjectId());
                                editor.putString("todaysDate", todaysDate);
                                editor.apply();
                                driver.getText().clear();
                                navigator.getText().clear();
                                truckSpinner.setSelection(0);
                                pbar.setVisibility(View.GONE);
                                createJournal.setVisibility(View.VISIBLE);
                                dismiss();
                                Intent intent = new Intent(getActivity(), CurrentJournal.class);
                                startActivity(intent);
                            } else {
                                pbar.setVisibility(View.GONE);
                                createJournal.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), getString(R.string.parse_exception_text), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();

                            }
                        }
                    });

                } else {
                    Toast.makeText(getActivity(), "Please enter at minimum a driver and start time", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driver.setText("");
                navigator.setText("");
                truckSpinner.setSelection(0);
                Utils.hideKeyboard(v, getActivity());
                dismiss();
            }
        });

        setCancelable(false);
        return v;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
            Utils.showKeyboardInDialog(getDialog());
    }
}