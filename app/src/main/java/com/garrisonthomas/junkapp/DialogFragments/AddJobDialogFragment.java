package com.garrisonthomas.junkapp.dialogfragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.garrisonthomas.junkapp.AddItemHelper;
import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.Utils;
import com.garrisonthomas.junkapp.parseobjects.NewJob;

public class AddJobDialogFragment extends AddItemHelper {

    private EditText etSID, etGrossSale, etNetSale, etReceiptNumber, etJobNotes;
    private static Button startTime, endTime, saveJob, cancelJob;
    private static Spinner payTypeSpinner;
    private static String[] payTypeArray;
    private String payTypeString, currentJournalId;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.add_job_layout, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        currentJournalId = preferences.getString("universalJournalId", "none");

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

                    NewJob newJob = new NewJob();
                    newJob.setRelatedJournal(currentJournalId);
                    newJob.setSID(Integer.valueOf(etSID.getText().toString()));
                    newJob.setStartTime(String.valueOf(startTime.getText()));
                    newJob.setEndTime(String.valueOf(endTime.getText()));
                    newJob.setGrossSale(Double.valueOf(etGrossSale.getText().toString()));
                    newJob.setNetSale(Double.valueOf(etNetSale.getText().toString()));
                    newJob.setReceiptNumber(Integer.valueOf(etReceiptNumber.getText().toString()));
                    newJob.setPayType(payTypeString);
                    newJob.setJobNotes(String.valueOf(etJobNotes.getText()));

                    newJob.saveEventually();
                    newJob.pinInBackground();

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // This helps to always show cancel and save button when keyboard is open
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

}