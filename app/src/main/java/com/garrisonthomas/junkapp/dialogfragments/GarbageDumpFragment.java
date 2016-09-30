package com.garrisonthomas.junkapp.dialogfragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.garrisonthomas.junkapp.BaseActivity;
import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.Utils;
import com.garrisonthomas.junkapp.entryobjects.DumpObject;

public class GarbageDumpFragment extends Fragment {

    private TextInputEditText etAddDumpWeight, etDumpReceiptNumber, etPercentPrevious;
    private EditText etEditCost;
    private TextInputLayout enterWeightWrapper, enterReceiptNumberWrapper, enterPercentPreviousWrapper;
    private TextView tvGrossCost;
    private Button saveDump, cancelDump;
    private ImageButton btnEditCost;
    private Spinner dumpNameSpinner;
    private int pricePerTonne;
    private double result;
    private String dumpNameString, firebaseJournalRef;
    private boolean costIsEditable = true;
    private LinearLayout dumpCostLayout;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.add_garbage_dump_layout, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        firebaseJournalRef = preferences.getString("firebaseRef", "none");

        enterWeightWrapper = (TextInputLayout) v.findViewById(R.id.enter_weight_wrapper);
        etAddDumpWeight = (TextInputEditText) enterWeightWrapper.getEditText();
        enterReceiptNumberWrapper = (TextInputLayout) v.findViewById(R.id.enter_receipt_number_wrapper);
        etDumpReceiptNumber = (TextInputEditText) enterReceiptNumberWrapper.getEditText();
        enterPercentPreviousWrapper = (TextInputLayout) v.findViewById(R.id.enter_percent_previous_wrapper);
        etPercentPrevious = (TextInputEditText) enterPercentPreviousWrapper.getEditText();

        etEditCost = (EditText) v.findViewById(R.id.et_edit_dump_cost);

        tvGrossCost = (TextView) v.findViewById(R.id.tv_dump_gross_cost);

        saveDump = (Button) v.findViewById(R.id.btn_save_dump);
        cancelDump = (Button) v.findViewById(R.id.btn_cancel_dump);

        btnEditCost = (ImageButton) v.findViewById(R.id.btn_edit_dump_cost);

        dumpNameSpinner = (Spinner) v.findViewById(R.id.spinner_dump_dialog);

        dumpCostLayout = (LinearLayout) v.findViewById(R.id.dump_cost_layout);

        dumpNameSpinner.setAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, BaseActivity.dumpNameArray));

        dumpNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                dumpNameString = BaseActivity.dumpNameArray[position];
                pricePerTonne = BaseActivity.dumpRateArray[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        etAddDumpWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {

                if (!TextUtils.isEmpty(etAddDumpWeight.getText())) {

                    double weightInTonnes = Double.valueOf(etAddDumpWeight.getText().toString());

                    result = Utils.calculateDump(pricePerTonne, weightInTonnes, dumpNameSpinner);

                    dumpCostLayout.setVisibility(View.VISIBLE);
                    String resultString = "$" + String.valueOf(result);
                    tvGrossCost.setText(resultString);


                } else if (TextUtils.isEmpty(etAddDumpWeight.getText())) {

                    dumpCostLayout.setVisibility(View.GONE);

                }
            }

        });

        saveDump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(etAddDumpWeight.getText())
                        && (!TextUtils.isEmpty(etDumpReceiptNumber.getText()))) {

                    Firebase fbrDump = new Firebase(firebaseJournalRef + "dumps/" + dumpNameString + " (" +
                            String.valueOf(etDumpReceiptNumber.getText()) + ")");

                    DumpObject dump = new DumpObject();

                    dump.setDumpName(dumpNameString);
                    if (!TextUtils.isEmpty(etEditCost.getText())) {
                        dump.setGrossCost(Double.valueOf(etEditCost.getText().toString()));
                    } else {
                        dump.setGrossCost(result);
                    }
                    dump.setTonnage(Double.valueOf(etAddDumpWeight.getText().toString()));
                    dump.setDumpReceiptNumber(Integer.valueOf(etDumpReceiptNumber.getText().toString()));
                    if (!TextUtils.isEmpty(etPercentPrevious.getText())) {
                        dump.setPercentPrevious(Integer.valueOf(etPercentPrevious.getText().toString()));
                    } else {
                        dump.setPercentPrevious(0);
                    }

                    fbrDump.setValue(dump);

                    Toast.makeText(getActivity(), "Dump at " + dumpNameString + " saved", Toast.LENGTH_SHORT).show();

                    getActivity().finish();
                } else {

                    Toast.makeText(getActivity(), "Please fill all required fields", Toast.LENGTH_SHORT).show();

                }
            }
        });

        cancelDump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        btnEditCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (costIsEditable) {
                    tvGrossCost.setVisibility(View.GONE);
                    etEditCost.setVisibility(View.VISIBLE);
                    etEditCost.requestFocus();
                    btnEditCost.setImageResource(R.drawable.ic_close_white_24dp);
                    costIsEditable = false;
                } else {
                    tvGrossCost.setVisibility(View.VISIBLE);
                    etEditCost.setVisibility(View.GONE);
                    btnEditCost.setImageResource(R.drawable.ic_edit_white_24dp);
                    costIsEditable = true;
                }
            }
        });

//        setCancelable(false);
        return v;

    }
}
