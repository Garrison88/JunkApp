package com.garrisonthomas.junkapp.DialogFragments;

import android.app.Dialog;
import android.content.Context;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.garrisonthomas.junkapp.CurrentJournal;
import com.garrisonthomas.junkapp.ParseObjects.DailyJournal;
import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.Utils;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DailyJournalDialogFragment extends DialogFragment {

    private static Spinner truckSpinner;
    private static String[] truckNumber;
    private static Button cancel, createJournal;
    private EditText crew;
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

        crew = (EditText) v.findViewById(R.id.et_crew);

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

        createJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(crew.getText())) {

                    final String crewString = crew.getText().toString();
                    final int truckNumber = Integer.valueOf(truckSelected.substring(6, 7));

                    Utils.hideKeyboard(v, getActivity());

                    pbar.setVisibility(View.VISIBLE);

                    final DailyJournal newJournal = new DailyJournal();

                    newJournal.setDate(todaysDate);
                    newJournal.setCrew(crewString);
                    newJournal.setTruckNumber(truckNumber);
                    newJournal.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("crew", crewString);
                                editor.putString("truck", truckSelected);
                                editor.putString("universalJournalId", newJournal.getObjectId());
                                editor.putString("todaysDate", todaysDate);
                                editor.apply();
                                crew.getText().clear();
                                truckSpinner.setSelection(0);
                                pbar.setVisibility(View.INVISIBLE);
                                dismiss();
                                Intent intent = new Intent(getActivity(), CurrentJournal.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getActivity(), "Something went wrong. Journal not created", Toast.LENGTH_SHORT).show();
                                crew.setText("");
                                truckSpinner.setSelection(0);
                                dismiss();
                            }
                        }
                    });

                } else {
                    Toast.makeText(getActivity(), "Please enter a crew", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crew.setText("");
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
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}