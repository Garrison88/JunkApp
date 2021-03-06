package com.garrisonthomas.junkapp.dialogfragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
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
import com.garrisonthomas.junkapp.DialogFragmentHelper;
import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.entryobjects.DumpObject;
import com.garrisonthomas.junkapp.inputFilters.InputFilterMinMax;

import java.text.NumberFormat;

public class GarbageDumpFragment extends Fragment {

    private TextInputEditText etAddDumpWeight, etDumpReceiptNumber, etPercentPrevious;
    private EditText etEditCost;
    private TextView tvGrossCost;
    private ImageButton btnEditCost;
    private Spinner dumpNameSpinner;
    private double pricePerTonne;
    private double result;
    private String dumpNameString, currentJournalRef;
    private boolean costIsEditable;
    private LinearLayout dumpCostLayout;
    private SharedPreferences preferences;

    private NumberFormat currencyFormat;

    private int mPage;

    public static final String ARG_PAGE = "ARG_PAGE";

    public static GarbageDumpFragment newInstance(int page) {
//        Bundle args = new Bundle();
//        args.putInt(ARG_PAGE, page);
        GarbageDumpFragment fragment = new GarbageDumpFragment();
//        fragment.setArguments(args);
        return fragment;
//        return args.getView(page).constructFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.add_garbage_dump_layout, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        currentJournalRef = preferences.getString("currentJournalRef", null);

//        mPage = getArguments().getInt(ARG_PAGE);

        TextInputLayout enterWeightWrapper = (TextInputLayout) v.findViewById(R.id.enter_weight_wrapper);
        etAddDumpWeight = (TextInputEditText) enterWeightWrapper.getEditText();
        TextInputLayout enterReceiptNumberWrapper = (TextInputLayout) v.findViewById(R.id.enter_receipt_number_wrapper);
        etDumpReceiptNumber = (TextInputEditText) enterReceiptNumberWrapper.getEditText();
        TextInputLayout enterPercentPreviousWrapper = (TextInputLayout) v.findViewById(R.id.enter_percent_previous_wrapper);
        etPercentPrevious = (TextInputEditText) enterPercentPreviousWrapper.getEditText();
        etPercentPrevious.setFilters(new InputFilter[]{new InputFilterMinMax("1", "100")});

        currencyFormat = NumberFormat.getCurrencyInstance();

        etEditCost = (EditText) v.findViewById(R.id.et_edit_dump_cost);

        tvGrossCost = (TextView) v.findViewById(R.id.tv_dump_gross_cost);

        View cancelSaveLayout = v.findViewById(R.id.garbage_cancel_save_button_bar);
        Button saveDump = (Button) cancelSaveLayout.findViewById(R.id.btn_save);
        Button cancelDump = (Button) cancelSaveLayout.findViewById(R.id.btn_cancel);

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

                    int minimum = BaseActivity.dumpMinimumArray[dumpNameSpinner.getSelectedItemPosition()];

                    result = DialogFragmentHelper.calculateDump(pricePerTonne, weightInTonnes, minimum);

                    dumpCostLayout.setVisibility(View.VISIBLE);
                    String resultString = currencyFormat.format(result);
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

                    Firebase fbrDump = new Firebase(currentJournalRef + "dumps/" + dumpNameString + " (" +
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
                getActivity().getFragmentManager().popBackStack();
            }
        });

        btnEditCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!costIsEditable) {
                    etEditCost.setText("");
                    tvGrossCost.setVisibility(View.GONE);
                    etEditCost.setVisibility(View.VISIBLE);
                    etEditCost.setHint(tvGrossCost.getText().toString());
                    etEditCost.requestFocus();
                    btnEditCost.setImageResource(R.drawable.ic_close_white_24dp);
                    costIsEditable = true;
                } else {
                    etEditCost.setText("");
                    tvGrossCost.setVisibility(View.VISIBLE);
                    etEditCost.setVisibility(View.GONE);
                    btnEditCost.setImageResource(R.drawable.ic_edit_white_24dp);
                    costIsEditable = false;
                }
            }
        });

//        setCancelable(false);
        return v;

    }
}
