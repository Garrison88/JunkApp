package com.garrisonthomas.junkapp.DialogFragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.garrisonthomas.junkapp.DailyJournal;
import com.garrisonthomas.junkapp.NewDump;
import com.garrisonthomas.junkapp.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddDumpDialogFragment extends DialogFragment {

    private static EditText etAddDumpWeight, etDumpReceiptNumber, etPercentPrevious;
    private static TextView tvGrossCost, tvNetCost;
    private static Button saveDump;
    private static Spinner dumpNameSpinner;
    private static String[] dumpNameArray;
    private static int[] rate;
    private static int dumpRateInt;
    private static double weightNumber, result, withTax;
    private static String dumpNameString, resultString, withTaxString;

    Date date = new Date();
    SimpleDateFormat df2 = new SimpleDateFormat("EEE, dd MMM yyyy");
    String todaysDate = df2.format(date);

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(todaysDate);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.add_dump_layout, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        etAddDumpWeight = (EditText) v.findViewById(R.id.et_add_dump_weight);
        etDumpReceiptNumber = (EditText) v.findViewById(R.id.et_dump_receipt_number);
        etPercentPrevious = (EditText) v.findViewById(R.id.et_percent_previous);

        tvGrossCost = (TextView) v.findViewById(R.id.tv_dump_gross_cost);
        tvNetCost = (TextView) v.findViewById(R.id.tv_dump_net_cost);

        dumpNameArray = getResources().getStringArray(R.array.dumps_name);
        rate = getResources().getIntArray(R.array.dumps_rate);

        dumpNameSpinner = (Spinner) v.findViewById(R.id.spinner_dump_dialog);

        saveDump = (Button) v.findViewById(R.id.btn_save_dump);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.dumps_name, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        dumpNameSpinner.setAdapter(adapter);
        dumpNameSpinner.setSelection(0);

        dumpNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                dumpNameString = dumpNameArray[position];
                dumpRateInt = rate[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        etAddDumpWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {

                if ((!hasFocus) && (!TextUtils.isEmpty(etAddDumpWeight.getText()))) {

                    weightNumber = Double.parseDouble(etAddDumpWeight.getText().toString());
                    weightNumber = weightNumber / 1000;

                    result = Math.round((weightNumber * dumpRateInt) * 100.00) / 100.00;
                    withTax = Math.round((result * 1.13) * 100.00) / 100.00;

                    resultString = String.valueOf(result);
                    withTaxString = String.valueOf(withTax);

                    tvGrossCost.setText("$" + resultString);
                    tvNetCost.setText("$" + withTaxString);

                }
            }
        });

        saveDump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(etAddDumpWeight.getText())
                        && (!TextUtils.isEmpty(etDumpReceiptNumber.getText()))) {

                    ParseQuery<DailyJournal> query = ParseQuery.getQuery("DailyJournal");
                    query.whereEqualTo("date", todaysDate);
                    query.setLimit(1);
                    query.findInBackground(new FindCallback<DailyJournal>() {
                        public void done(List<DailyJournal> list, ParseException e) {
                            if (e == null) {

                                for (DailyJournal newJournal : list) {

                                    NewDump newDump = new NewDump();
                                    newDump.setRelatedJournal(newJournal.getObjectId());
                                    newDump.setDumpName(dumpNameString);
                                    newDump.setGrossCost(Double.valueOf(resultString));
                                    newDump.setNetCost(Double.valueOf(withTaxString));
                                    newDump.setDumpReceiptNumber(Integer.valueOf(etDumpReceiptNumber.getText().toString()));
                                    newDump.setPercentPrevious(Integer.valueOf(etPercentPrevious.getText().toString()));

                                    newDump.saveInBackground();

                                    newJournal.saveInBackground();

                                }

                            } else {
                                Log.d("score", "Error: " + e.getMessage());
                            }
                        }
                    });

                    Toast.makeText(getActivity(), "Dump saved", Toast.LENGTH_SHORT).show();

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
