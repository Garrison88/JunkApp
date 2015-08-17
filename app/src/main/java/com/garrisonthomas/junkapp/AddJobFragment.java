package com.garrisonthomas.junkapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseObject;

import org.w3c.dom.Text;

public class AddJobFragment extends FragmentActivity {

    EditText etSSID, etGrossSale, etNetSale, etReceiptNumber;
    Button saveJob;
    Spinner payType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_form_layout);

        etSSID = (EditText) findViewById(R.id.et_ssid);
        etGrossSale = (EditText) findViewById(R.id.et_gross_sale);
        etNetSale = (EditText) findViewById(R.id.et_net_sale);
        etReceiptNumber = (EditText) findViewById(R.id.et_receipt_number);

        payType = (Spinner) findViewById(R.id.spinner_pay_type);

        saveJob = (Button) findViewById(R.id.btn_save_job);

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
                    newJob.saveInBackground();

                    Toast.makeText(getApplicationContext(), "Job number " + ssid + " saved", Toast.LENGTH_LONG).show();

                    InputMethodManager imm = (InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    Intent intent = new Intent(getApplicationContext(), DailyJournal.class);
                    startActivity(intent);

                } else {

                    Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();

                }
            }
        });

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
