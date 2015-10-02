package com.garrisonthomas.junkapp.DialogFragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

import com.garrisonthomas.junkapp.CurrentJournal;
import com.garrisonthomas.junkapp.DailyJournal;
import com.garrisonthomas.junkapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DailyJournalDialogFragment extends DialogFragment {

    private static Spinner truckSpinner;
    private static String[] truckNumber;
    private static Button cancel, createJournal;
    private static EditText crew;
    private static String truckSelected;
    private static Date date = new Date();
    private static SimpleDateFormat df2 = new SimpleDateFormat("EEE, dd MMM yyyy");
    private static String todaysDate = df2.format(date);

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(todaysDate);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.daily_journal_dialog_layout, container, false);

        truckSpinner = (Spinner) v.findViewById(R.id.truck_spinner);
        truckNumber = getResources().getStringArray(R.array.truck_number);
        createJournal = (Button) v.findViewById(R.id.btn_create_journal);
        cancel = (Button) v.findViewById(R.id.cancel_fragment);

        crew = (EditText) v.findViewById(R.id.et_crew);

        truckSpinner.setAdapter(new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, truckNumber));
//        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
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

                    DailyJournal newJournal = new DailyJournal();
                    newJournal.setDate(todaysDate);
                    newJournal.setCrew(crew.getText().toString());
                    newJournal.setTruckNumber(truckSelected);
                    Intent intent = new Intent(getActivity(), CurrentJournal.class);
                    intent.putExtra("EXTRA_CREW", crew.getText().toString());
                    intent.putExtra("EXTRA_TRUCK_NUMBER", truckSelected);
                    newJournal.saveInBackground();
                    intent.putExtra("EXTRA_DJ_ID", newJournal.getObjectId());
                    crew.getText().clear();
                    truckSpinner.setSelection(0);
                    dismiss();
                    startActivity(intent);

                } else {
                    Toast.makeText(getActivity(), "Please enter a crew", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setCancelable(false);
        return v;

    }

}
