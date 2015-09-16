package com.garrisonthomas.junkapp.DialogFragments;

import android.app.DialogFragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.garrisonthomas.junkapp.CurrentJournal;
import com.garrisonthomas.junkapp.DailyJournal;
import com.garrisonthomas.junkapp.DateHelper;
import com.garrisonthomas.junkapp.NewJob;
import com.garrisonthomas.junkapp.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddJobDialogFragment extends DialogFragment {

    EditText etSSID, etGrossSale, etNetSale, etReceiptNumber;
    Button saveJob;
    Spinner payTypeSpinner;
    String[] payTypeArray;
    String payTypeString, todaysDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.add_job_layout, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        etSSID = (EditText) v.findViewById(R.id.et_ssid);
        etGrossSale = (EditText) v.findViewById(R.id.et_gross_sale);
        etNetSale = (EditText) v.findViewById(R.id.et_net_sale);
        etReceiptNumber = (EditText) v.findViewById(R.id.et_receipt_number);

        Date date = new Date();
        SimpleDateFormat df2 = new SimpleDateFormat("EEE, dd MMM yyyy");

        todaysDate = df2.format(date);

        payTypeArray = getResources().getStringArray(R.array.job_pay_type);

        payTypeSpinner = (Spinner) v.findViewById(R.id.spinner_pay_type);

        saveJob = (Button) v.findViewById(R.id.btn_save_job);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.job_pay_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        payTypeSpinner.setAdapter(adapter);
        payTypeSpinner.setSelection(0);

        payTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                payTypeString = payTypeArray[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        saveJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(etSSID.getText())
                        && (!TextUtils.isEmpty(etGrossSale.getText()))
                        && (!TextUtils.isEmpty(etNetSale.getText())
                        && (!TextUtils.isEmpty(etReceiptNumber.getText())))) {

                    ParseQuery<DailyJournal> query = ParseQuery.getQuery("DailyJournal");
                    query.whereEqualTo("date", todaysDate);
                    query.setLimit(1);
                    query.findInBackground(new FindCallback<DailyJournal>() {
                        public void done(List<DailyJournal> list, ParseException e) {
                            if (e == null) {

                                for (DailyJournal newJournal : list) {

                                    NewJob newJob = new NewJob();
                                    newJob.setRelatedJournal(newJournal.getObjectId());
                                    newJob.setSSID(Integer.valueOf(etSSID.getText().toString()));
                                    newJob.setGrossSale(Double.valueOf(etGrossSale.getText().toString()));
                                    newJob.setNetSale(Double.valueOf(etNetSale.getText().toString()));
                                    newJob.setReceiptNumber(Integer.valueOf(etReceiptNumber.getText().toString()));
                                    newJob.setPayType(payTypeString);

                                    newJob.saveInBackground();

                                    newJournal.saveInBackground();

                                }

                            } else {
                                Log.d("score", "Error: " + e.getMessage());
                            }
                        }
                    });

                    Toast.makeText(getActivity(), "Job number " + etSSID.getText().toString() +
                            " saved", Toast.LENGTH_SHORT).show();

                    dismiss();

                } else {

                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_LONG).show();

                }
            }
        });

        return v;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
