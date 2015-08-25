package com.garrisonthomas.junkapp;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
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

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AddJobDialogFragment extends DialogFragment {

    EditText etSSID, etGrossSale, etNetSale, etReceiptNumber;
    Button saveJob;
    Spinner payTypeSpinner;
    String[] payTypeArray;
    String payTypeString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.add_job_layout, container, false);

        etSSID = (EditText) v.findViewById(R.id.et_ssid);
        etGrossSale = (EditText) v.findViewById(R.id.et_gross_sale);
        etNetSale = (EditText) v.findViewById(R.id.et_net_sale);
        etReceiptNumber = (EditText) v.findViewById(R.id.et_receipt_number);

        payTypeArray = getResources().getStringArray(R.array.job_pay_type);

        payTypeSpinner = (Spinner) v.findViewById(R.id.spinner_pay_type);

        saveJob = (Button) v.findViewById(R.id.btn_save_job);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.job_pay_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
                    query.whereEqualTo("date", DateHelper.getCurrentDate());
                    query.findInBackground(new FindCallback<DailyJournal>() {
                        public void done(List<DailyJournal> list, ParseException e) {
                            if (e == null) {

                                for (DailyJournal newJournal : list) {

                                    final ParseObject newJob = new NewJob();
                                    int ssid = Integer.valueOf(etSSID.getText().toString());
                                    double grossSale = Double.valueOf(etGrossSale.getText().toString());
                                    double netSale = Double.valueOf(etNetSale.getText().toString());
                                    int receiptNumber = Integer.valueOf(etReceiptNumber.getText().toString());
                                    newJob.put("ssid", ssid);
                                    newJob.put("grossSale", grossSale);
                                    newJob.put("netSale", netSale);
                                    newJob.put("receiptNumber", receiptNumber);
                                    newJob.put("payType", payTypeString);

                                    newJournal.add("jobs", newJob);

                                    newJob.saveEventually();
                                    newJob.unpinInBackground();
                                    newJob.pinInBackground();
                                    newJournal.saveEventually();

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

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

}
