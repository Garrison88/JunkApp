package com.garrisonthomas.junkapp.DialogFragments;

import android.app.DialogFragment;
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

import com.garrisonthomas.junkapp.ParseObjects.NewJob;
import com.garrisonthomas.junkapp.R;

public class AddJobDialogFragment extends DialogFragment {

    private EditText etSSID, etGrossSale, etNetSale, etReceiptNumber, etJobNotes;
    private static Button saveJob;
    private static Spinner payTypeSpinner;
    private static String[] payTypeArray;
    private String payTypeString, currentJournalId;
    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.add_job_layout, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        currentJournalId = preferences.getString("universalJournalId", null);

        etSSID = (EditText) v.findViewById(R.id.et_ssid);
        etGrossSale = (EditText) v.findViewById(R.id.et_gross_sale);
        etNetSale = (EditText) v.findViewById(R.id.et_net_sale);
        etReceiptNumber = (EditText) v.findViewById(R.id.et_receipt_number);
        etJobNotes = (EditText) v.findViewById(R.id.et_job_notes);

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

        etGrossSale.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {

                if ((!hasFocus) && (!TextUtils.isEmpty(etGrossSale.getText()))) {

                    final double doubleValue = Double.valueOf(etGrossSale.getText().toString());

                    final String withTax = String.valueOf(Math.round((doubleValue * 1.13) * 100.00) / 100.00);
                    etNetSale.setText(withTax);
                }
            }
        });

        saveJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(etSSID.getText())
                        && (!TextUtils.isEmpty(etGrossSale.getText()))
                        && (!TextUtils.isEmpty(etNetSale.getText())
                        && (!TextUtils.isEmpty(etReceiptNumber.getText())))) {

                    final NewJob newJob = new NewJob();
                    newJob.setRelatedJournal(currentJournalId);
                    newJob.setSSID(Integer.valueOf(etSSID.getText().toString()));
                    newJob.setGrossSale(Double.valueOf(etGrossSale.getText().toString()));
                    newJob.setNetSale(Double.valueOf(etNetSale.getText().toString()));
                    newJob.setReceiptNumber(Integer.valueOf(etReceiptNumber.getText().toString()));
                    newJob.setPayType(payTypeString);
                    newJob.setJobNotes(String.valueOf(etJobNotes.getText()));

                    newJob.saveInBackground();

                    Toast.makeText(getActivity(), "Job number " + etSSID.getText().toString() +
                            " saved", Toast.LENGTH_SHORT).show();

                    dismiss();

                } else {

                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();

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
