package com.garrisonthomas.junkapp.dialogfragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
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

import com.firebase.client.Firebase;
import com.garrisonthomas.junkapp.DialogFragmentHelper;
import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.Utils;
import com.garrisonthomas.junkapp.entryobjects.JobObject;

public class AddJobDialogFragment extends DialogFragmentHelper {

    private EditText etSID, etGrossSale, etNetSale, etReceiptNumber, etJobNotes;
    private static Button startTime, endTime, saveJob, cancelJob;
    private static Spinner payTypeSpinner;
    private static String[] payTypeArray;
    private String payTypeString, firebaseJournalRef;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.add_job_layout, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        firebaseJournalRef = preferences.getString("firebaseRef", "none");

        etSID = (EditText) v.findViewById(R.id.et_sid);
        etGrossSale = (EditText) v.findViewById(R.id.et_gross_sale);
        etNetSale = (EditText) v.findViewById(R.id.et_net_sale);
        etReceiptNumber = (EditText) v.findViewById(R.id.et_receipt_number);
        etJobNotes = (EditText) v.findViewById(R.id.et_job_notes);

        payTypeArray = getResources().getStringArray(R.array.job_pay_type);

        payTypeSpinner = (Spinner) v.findViewById(R.id.spinner_pay_type);

        startTime = (Button) v.findViewById(R.id.job_start_time);
        endTime = (Button) v.findViewById(R.id.job_end_time);
        saveJob = (Button) v.findViewById(R.id.btn_save_job);
        cancelJob = (Button) v.findViewById(R.id.btn_cancel_job);

        payTypeSpinner.setAdapter(new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_dropdown_item_1line, payTypeArray));

        payTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                payTypeString = payTypeArray[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        etGrossSale.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {

                if ((!hasFocus) && (!TextUtils.isEmpty(etGrossSale.getText()))) {

                    final double doubleValue = Double.valueOf(etGrossSale.getText().toString());

                    final String withTax = String.valueOf(Utils.calculateTax(doubleValue));
                    etNetSale.setText(withTax);
                }
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.chooseTime(getActivity(), startTime);
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.chooseTime(getActivity(), endTime);
            }
        });

        saveJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(etSID.getText())
                        && (!TextUtils.isEmpty(etGrossSale.getText()))
                        && (!TextUtils.isEmpty(etNetSale.getText())
                        && (!TextUtils.isEmpty(etReceiptNumber.getText())))) {

                    Firebase fbrJob = new Firebase(firebaseJournalRef + "jobs/" + String.valueOf(etSID.getText()));

                    JobObject job = new JobObject();

                    job.setSID(Integer.valueOf(etSID.getText().toString()));
                    job.setGrossSale(Double.valueOf(etGrossSale.getText().toString()));
                    job.setNetSale(Double.valueOf(etNetSale.getText().toString()));
                    job.setStartTime(String.valueOf(startTime.getText()));
                    job.setEndTime(String.valueOf(endTime.getText()));
                    job.setPayType(payTypeString);
                    job.setReceiptNumber(Integer.valueOf(etReceiptNumber.getText().toString()));
                    job.setJobNotes(String.valueOf(etJobNotes.getText()));

                    fbrJob.setValue(job);

                    Toast.makeText(getActivity(), "Job number " + etSID.getText().toString() + " saved", Toast.LENGTH_SHORT).show();

                    dismiss();


                } else {

                    Toast.makeText(getActivity(), "Please fill all required fields", Toast.LENGTH_SHORT).show();

                }
            }
        });

        cancelJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setCancelable(false);
        return v;

    }

}