package com.garrisonthomas.junkapp;

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

import com.parse.ParseObject;

/**
 * Created by Garrison Thomas on 2015-08-17.
 */
public class DailyJournalDialogFragment extends DialogFragment {

    Spinner truckSpinner;
    String[] truckNumber;
    TextView todaysDate;
    Button cancel, createJournal;
    EditText crew;
    String truckSelected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.daily_journal_dialog_layout, container, false);

        truckSpinner = (Spinner) v.findViewById(R.id.truck_spinner);
        truckNumber = getResources().getStringArray(R.array.truck_number);
        todaysDate = (TextView) v.findViewById(R.id.tv_todays_date);
        createJournal = (Button) v.findViewById(R.id.btn_create_journal);
        cancel = (Button) v.findViewById(R.id.cancel_fragment);

        crew = (EditText) v.findViewById(R.id.et_crew);

        todaysDate.setText(DateHelper.getCurrentDate());

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

                    ParseObject newJournal = new NewJournal();
                    newJournal.put("date", todaysDate.getText());
                    newJournal.put("crew", crew.getText().toString());
                    newJournal.put("truckNumber", truckSelected);
                    newJournal.saveEventually();
                    dismiss();

                } else {
                    Toast.makeText(getActivity(), "Enter crew", Toast.LENGTH_SHORT).show();
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

//            ImageView left_icon = (ImageView) mySpinner.findViewById(R.id.left_pic);
//            left_icon.setImageResource(R.drawable.dump_truck);

            return mySpinner;
        }

    }

}
