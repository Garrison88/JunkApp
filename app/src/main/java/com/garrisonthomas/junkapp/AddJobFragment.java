package com.garrisonthomas.junkapp;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class AddJobFragment extends DialogFragment {

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

                    ParseObject newJob = new NewJob();
                    int ssid = Integer.valueOf(etSSID.getText().toString());
                    double grossSale = Double.valueOf(etGrossSale.getText().toString());
                    double netSale = Double.valueOf(etNetSale.getText().toString());
                    int receiptNumber = Integer.valueOf(etReceiptNumber.getText().toString());
                    newJob.put("ssid", ssid);
                    newJob.put("grossSale", grossSale);
                    newJob.put("netSale", netSale);
                    newJob.put("receiptNumber", receiptNumber);
                    newJob.put("payType", payTypeString);
                    newJob.saveEventually();

                    Toast.makeText(getActivity(), "Job number " + ssid + " saved", Toast.LENGTH_LONG).show();

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
