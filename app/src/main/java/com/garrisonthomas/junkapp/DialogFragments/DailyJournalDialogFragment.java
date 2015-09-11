package com.garrisonthomas.junkapp.DialogFragments;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.garrisonthomas.junkapp.CurrentJournal;
import com.garrisonthomas.junkapp.DailyJournal;
import com.garrisonthomas.junkapp.DateHelper;
import com.garrisonthomas.junkapp.R;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Garrison Thomas on 2015-08-17.
 */
public class DailyJournalDialogFragment extends DialogFragment {

    Spinner truckSpinner;
    String[] truckNumber;
    TextView tvTodaysDate;
    Button cancel, createJournal;
    EditText crew;
    String truckSelected, todaysDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.daily_journal_dialog_layout, container, false);

        Date date = new Date();
        SimpleDateFormat df2 = new SimpleDateFormat("EEE, dd MMM yyyy");

        todaysDate = df2.format(date);
        truckSpinner = (Spinner) v.findViewById(R.id.truck_spinner);
        truckNumber = getResources().getStringArray(R.array.truck_number);
        tvTodaysDate = (TextView) v.findViewById(R.id.tv_todays_date);
        tvTodaysDate.setText(todaysDate);
        createJournal = (Button) v.findViewById(R.id.btn_create_journal);
        cancel = (Button) v.findViewById(R.id.cancel_fragment);

        crew = (EditText) v.findViewById(R.id.et_crew);

        truckSpinner.setAdapter(new TruckAdapter(this.getActivity(), R.layout.custom_truck_spinner, truckNumber));
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
                    dismiss();
                    startActivity(intent);

                } else {
                    Toast.makeText(getActivity(), "Must enter crew", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public class TruckAdapter extends ArrayAdapter<String> {

        public TruckAdapter(Context ctx, int txtViewResourceId, String[] objects) {
            super(ctx, txtViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
            return getCustomView(position, cnvtView, prnt);
        }

        @Override
        public View getView(int pos, View cnvtView, ViewGroup prnt) {
            return getCustomView(pos, cnvtView, prnt);
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.custom_truck_spinner, parent,
                    false);

            TextView main_text = (TextView) mySpinner.findViewById(R.id.spinner_text_truck_number);

            main_text.setText(truckNumber[position]);

            return mySpinner;
        }

    }

}
