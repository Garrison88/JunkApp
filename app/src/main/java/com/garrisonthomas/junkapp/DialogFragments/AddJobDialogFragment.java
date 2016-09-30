package com.garrisonthomas.junkapp.dialogfragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.garrisonthomas.junkapp.DialogFragmentHelper;
import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.Utils;
import com.garrisonthomas.junkapp.entryobjects.JobObject;

import info.hoang8f.android.segmented.SegmentedGroup;

public class AddJobDialogFragment extends DialogFragmentHelper {

    private TextInputEditText etSID, etGrossSale, etNetSale, etReceiptNumber, etJobNotes;
    private TextInputLayout enterSIDWrapper, enterGrossSaleWrapper, enterNetSaleWrapper,
            enterReceiptNumberWrapper;
    private Button startTime, endTime, saveJob, cancelJob;
    private Spinner payTypeSpinner;
    private RadioButton commButton, resButton, cancellationButton;
    private String[] payTypeArray;
    private String payTypeString, firebaseJournalRef;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.add_job_layout, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.90);

        getDialog().setContentView(R.layout.add_job_layout);

        getDialog().getWindow().setLayout(screenWidth, LinearLayout.LayoutParams.WRAP_CONTENT);

        preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        firebaseJournalRef = preferences.getString("firebaseRef", "none");

        enterSIDWrapper = (TextInputLayout) v.findViewById(R.id.enter_sid_wrapper);
        etSID = (TextInputEditText) enterSIDWrapper.getEditText();

        enterGrossSaleWrapper = (TextInputLayout) v.findViewById(R.id.enter_gross_sale_wrapper);
        etGrossSale = (TextInputEditText) enterGrossSaleWrapper.getEditText();

        enterNetSaleWrapper = (TextInputLayout) v.findViewById(R.id.enter_net_sale_wrapper);
        etNetSale = (TextInputEditText) enterNetSaleWrapper.getEditText();

        enterReceiptNumberWrapper = (TextInputLayout) v.findViewById(R.id.enter_receipt_number_wrapper);
        etReceiptNumber = (TextInputEditText) enterReceiptNumberWrapper.getEditText();

        etJobNotes = (TextInputEditText) v.findViewById(R.id.et_job_notes);

        resButton = (RadioButton) v.findViewById(R.id.switch_residential);
        commButton = (RadioButton) v.findViewById(R.id.switch_commercial);
        cancellationButton = (RadioButton) v.findViewById(R.id.switch_cancellation);

        payTypeArray = getResources().getStringArray(R.array.job_pay_type);

        payTypeSpinner = (Spinner) v.findViewById(R.id.spinner_pay_type);

        startTime = (Button) v.findViewById(R.id.job_start_time);
        endTime = (Button) v.findViewById(R.id.job_end_time);
        saveJob = (Button) v.findViewById(R.id.btn_save_job);
        cancelJob = (Button) v.findViewById(R.id.btn_cancel_job);

        // handle setting of jobType to "Cancellation" by auto-filling receiptNumber, gross, and net sale
        // and setting payType to "Cancellation"
        SegmentedGroup segmentedGroup = (SegmentedGroup) v.findViewById(R.id.job_type_segmented_group);
        segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                if (checkedId == R.id.switch_cancellation) {
                    etReceiptNumber.setEnabled(false);
                    etGrossSale.setEnabled(false);
                    etNetSale.setEnabled(false);
                    payTypeSpinner.setEnabled(false);
                }
                if (checkedId != R.id.switch_cancellation) {
                    etReceiptNumber.setEnabled(true);
                    etGrossSale.setEnabled(true);
                    etNetSale.setEnabled(true);
                    payTypeSpinner.setEnabled(true);
                }
            }
        });

        payTypeSpinner.setAdapter(new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_spinner_item, payTypeArray));

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

                if (validateEditTextLength(etSID, 4, 6)
                        //TODO: fix SID entry length
                        && (!TextUtils.isEmpty(etGrossSale.getText()) || !etGrossSale.isEnabled())
                        && (!TextUtils.isEmpty(etNetSale.getText()) || !etNetSale.isEnabled())
                        && (validateEditTextLength(etReceiptNumber, 5, 5) || !etReceiptNumber.isEnabled())
                        && (payTypeSpinner.getSelectedItemPosition() != 0) || !payTypeSpinner.isEnabled()) {

                    Firebase fbrJob = new Firebase(firebaseJournalRef + "jobs/" + String.valueOf(etSID.getText()));

                    JobObject job = new JobObject();

                    if (cancellationButton.isChecked()) {
                        job.setJobType(String.valueOf(cancellationButton.getText()));
                        job.setGrossSale(0.0);
                        job.setNetSale(0.0);
                        job.setPayType(String.valueOf(cancellationButton.getText()));
                        job.setReceiptNumber(0);
                        job.setPayType("N/A");
                    } else {
                        job.setGrossSale(Double.valueOf(etGrossSale.getText().toString()));
                        job.setNetSale(Double.valueOf(etNetSale.getText().toString()));
                        job.setReceiptNumber(Integer.valueOf(etReceiptNumber.getText().toString()));
                        job.setPayType(payTypeString);
                        if (resButton.isChecked()) {
                            job.setJobType(String.valueOf(resButton.getText()));
                        } else if (commButton.isChecked()) {
                            job.setJobType(String.valueOf(commButton.getText()));
                        }
                    }

                    job.setSID(Integer.valueOf(String.valueOf(etSID.getText())));
                    job.setStartTime(String.valueOf(startTime.getText()));
                    job.setEndTime(String.valueOf(endTime.getText()));
                    job.setJobNotes(String.valueOf(etJobNotes.getText()));


                    fbrJob.setValue(job);

                    Toast.makeText(getActivity(), "Job number " + etSID.getText().toString() + " saved", Toast.LENGTH_SHORT).show();

                    dismiss();

                } else {

                    if (!validateEditTextLength(etSID, 4, 6)) {
                        enterSIDWrapper.setErrorEnabled(true);
                        enterSIDWrapper.setError("Must be 4-6 numbers");
                    } else {
                        enterSIDWrapper.setErrorEnabled(false);
                    }
                    if (!validateEditTextLength(etReceiptNumber, 5, 5)) {
                        enterReceiptNumberWrapper.setErrorEnabled(true);
                        enterReceiptNumberWrapper.setError("Must be 5 numbers");
                    } else {
                        enterReceiptNumberWrapper.setErrorEnabled(false);
                    }
                    if (TextUtils.isEmpty(etGrossSale.getText())) {
                        enterGrossSaleWrapper.setErrorEnabled(true);
                        enterGrossSaleWrapper.setError(getString(R.string.empty_et_error_message));
                    } else {
                        enterGrossSaleWrapper.setErrorEnabled(false);
                    }
                    if (TextUtils.isEmpty(etNetSale.getText())) {
                        enterNetSaleWrapper.setErrorEnabled(true);
                        enterNetSaleWrapper.setError(getString(R.string.empty_et_error_message));
                    } else {
                        enterNetSaleWrapper.setErrorEnabled(false);
                    }
                    if (payTypeSpinner.getSelectedItemPosition() == 0) {
                        TextView errorText = (TextView) payTypeSpinner.getSelectedView();
                        errorText.setError("");
//                        errorText.setHintTextColor(ContextCompat.getColor(getActivity(), R.color.etErrorColor));//just to highlight that this is an error
//                        errorText.setText("Please select a pay type");//changes the selected item text to this
                    }

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