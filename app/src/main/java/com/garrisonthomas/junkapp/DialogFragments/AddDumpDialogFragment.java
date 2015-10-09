package com.garrisonthomas.junkapp.DialogFragments;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.garrisonthomas.junkapp.ParseObjects.NewDump;
import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.Utils;
import com.parse.ParseException;
import com.parse.SaveCallback;

public class AddDumpDialogFragment extends DialogFragment {

    private static EditText etAddDumpWeight, etDumpReceiptNumber, etPercentPrevious;
    private static TextView tvGrossCost, tvNetCost;
    private static Button saveDump;
    private static Spinner dumpNameSpinner;
    private static String[] dumpNameArray;
    private static int[] rate;
    private static int dumpRateInt;
    private static double weightNumber, result, withTax;
    private static String dumpNameString, resultString, withTaxString, currentJournalId;
    private SharedPreferences preferences;
    private ProgressBar pbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.add_dump_layout, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        currentJournalId = preferences.getString("universalJournalId", null);

        etAddDumpWeight = (EditText) v.findViewById(R.id.et_add_dump_weight);
        etDumpReceiptNumber = (EditText) v.findViewById(R.id.et_dump_receipt_number);
        etPercentPrevious = (EditText) v.findViewById(R.id.et_percent_previous);

        tvGrossCost = (TextView) v.findViewById(R.id.tv_dump_gross_cost);
        tvNetCost = (TextView) v.findViewById(R.id.tv_dump_net_cost);

        saveDump = (Button) v.findViewById(R.id.btn_save_dump);

        pbar = (ProgressBar) v.findViewById(R.id.add_dump_pbar);

        dumpNameArray = getResources().getStringArray(R.array.dumps_name);
        rate = getResources().getIntArray(R.array.dumps_rate);

        dumpNameSpinner = (Spinner) v.findViewById(R.id.spinner_dump_dialog);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.dumps_name, android.R.layout.simple_spinner_item);
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

                    resultString = getString(R.string.dollar_sign) + String.valueOf(result);
                    withTaxString = getString(R.string.dollar_sign) + String.valueOf(withTax);

                    tvGrossCost.setText(resultString);
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

                    saveDump.setVisibility(View.GONE);
                    pbar.setVisibility(View.VISIBLE);

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

                    newDump.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                            if (e == null) {
                                Toast.makeText(getActivity(), "Dump saved", Toast.LENGTH_SHORT).show();
                                pbar.setVisibility(View.GONE);
                                saveDump.setVisibility(View.VISIBLE);
                                dismiss();
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.parse_exception_text), Toast.LENGTH_SHORT).show();
                                pbar.setVisibility(View.GONE);
                                saveDump.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                } else {

                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();

                }
            }
        });

        return v;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

}
