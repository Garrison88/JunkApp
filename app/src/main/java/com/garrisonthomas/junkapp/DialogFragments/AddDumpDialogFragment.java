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
import android.widget.TextView;
import android.widget.Toast;

import com.garrisonthomas.junkapp.AddItemHelper;
import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.Utils;
import com.garrisonthomas.junkapp.parseobjects.NewDump;

public class AddDumpDialogFragment extends AddItemHelper {

    private static EditText etAddDumpWeight, etDumpReceiptNumber, etPercentPrevious;
    private static TextView tvGrossCost, tvNetCost;
    private static Button saveDump, cancelDump;
    private static Spinner dumpNameSpinner;
    private static String[] dumpNameArray;
    private static int[] rate;
    private static int dumpRateInt;
    private static double weightNumber, result, withTax;
    private static String dumpNameString, resultString, withTaxString, currentJournalId;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.add_dump_layout, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        currentJournalId = preferences.getString("universalJournalId", "none");

        etAddDumpWeight = (EditText) v.findViewById(R.id.et_add_dump_weight);
        etDumpReceiptNumber = (EditText) v.findViewById(R.id.et_dump_receipt_number);
        etPercentPrevious = (EditText) v.findViewById(R.id.et_percent_previous);

        tvGrossCost = (TextView) v.findViewById(R.id.tv_dump_gross_cost);
        tvNetCost = (TextView) v.findViewById(R.id.tv_dump_net_cost);

        saveDump = (Button) v.findViewById(R.id.btn_save_dump);
        cancelDump = (Button) v.findViewById(R.id.btn_cancel_dump);

        dumpNameArray = getResources().getStringArray(R.array.dumps_name);
        rate = getResources().getIntArray(R.array.dumps_rate);

        dumpNameSpinner = (Spinner) v.findViewById(R.id.spinner_dump_dialog);

        dumpNameSpinner.setAdapter(new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_dropdown_item_1line, dumpNameArray));

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

                    weightNumber = Double.parseDouble(etAddDumpWeight.getText().toString()) / 1000;

                    result = Math.round((weightNumber * dumpRateInt) * 100.00) / 100.00;

                    withTax = Utils.calculateTax(result);

                    resultString = getString(R.string.dollar_sign) + String.valueOf(result);
                    withTaxString = getString(R.string.dollar_sign) + String.valueOf(withTax);

                    tvGrossCost.setVisibility(View.VISIBLE);
                    tvGrossCost.setText(resultString);
                    tvNetCost.setVisibility(View.VISIBLE);
                    tvNetCost.setText(withTaxString);

                }
            }
        });

        saveDump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(etAddDumpWeight.getText())
                        && (!TextUtils.isEmpty(etDumpReceiptNumber.getText()))) {

                    Utils.hideKeyboard(v, getActivity());

                    NewDump newDump = new NewDump();
                    newDump.setRelatedJournal(currentJournalId);
                    newDump.setDumpName(dumpNameString);
                    newDump.setGrossCost(result);
                    newDump.setNetCost(withTax);
                    newDump.setDumpReceiptNumber(Integer.valueOf(etDumpReceiptNumber.getText().toString()));
                    if (!TextUtils.isEmpty(etPercentPrevious.getText())) {
                        newDump.setPercentPrevious(Integer.valueOf(etPercentPrevious.getText().toString()));
                    } else {
                        newDump.setPercentPrevious(0);
                    }

                    newDump.saveEventually();
                    newDump.pinInBackground();

                    Toast.makeText(getActivity(), "Dump at " + dumpNameString + " saved", Toast.LENGTH_SHORT).show();

                    dismiss();

                } else {

                    Toast.makeText(getActivity(), "Please fill all required fields", Toast.LENGTH_SHORT).show();

                }
            }
        });

        cancelDump.setOnClickListener(new View.OnClickListener() {
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
        Utils.showKeyboardInDialog(getDialog());
    }

}
