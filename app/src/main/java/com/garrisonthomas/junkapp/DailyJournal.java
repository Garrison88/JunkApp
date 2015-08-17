package com.garrisonthomas.junkapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Garrison Thomas on 2015-08-17.
 */
public class DailyJournal extends MainActivity {

    Spinner truckSpinner;
    String[] truckNumber;
    TextView todaysDate;
    Button addJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_journal_layout);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        truckSpinner = (Spinner) findViewById(R.id.truck_spinner);
        truckNumber = getResources().getStringArray(R.array.truck_number);
        todaysDate = (TextView) findViewById(R.id.tv_todays_date);
        addJob = (Button) findViewById(R.id.btn_add_job);

        todaysDate.setText(DateHelper.getCurrentDate());

        truckSpinner.setAdapter(new TruckAdapter(this, R.layout.custom_truck_spinner, truckNumber));
        truckSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), AddJobFragment.class);
                startActivity(intent);

            }
        });

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
            LayoutInflater inflater = DailyJournal.this.getLayoutInflater();
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
