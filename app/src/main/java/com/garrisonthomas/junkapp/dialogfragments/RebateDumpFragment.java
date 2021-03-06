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
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.entryobjects.RebateObject;
import com.garrisonthomas.junkapp.inputFilters.InputFilterMinMax;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Garrison on 2016-07-04.
 */
public class RebateDumpFragment extends Fragment {

    private TextInputEditText etRebateTonnage, etRebateAmount, etRebateReceiptNumber, etPercentPrevious;
    private TextInputLayout enterTonnageWrapper, enterAmountWrapper, enterReceiptNumberWrapper,
            enterPercentPreviousWrapper;
    private Button saveRebate, cancelRebate;
    private Spinner materialTypeSpinner, rebateLocationSpinner, weightUnitSpinner;
    private String[] materialTypeArray, rebateLocationArray;
    private double tonnageMultiplier;
    //    private int[] materialRebateArray;
//    private int pricePerTonne;
    private String materialTypeString, rebateLocationString, firebaseJournalRef;
    private SharedPreferences preferences;

    private int mPage;

    public static final String ARG_PAGE = "ARG_PAGE";

    public static RebateDumpFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        RebateDumpFragment fragment = new RebateDumpFragment();
        fragment.setArguments(args);
        return fragment;
//        return args.getView(page).constructFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.add_rebate_dump_layout, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        firebaseJournalRef = preferences.getString("currentJournalRef", null);

        enterTonnageWrapper = (TextInputLayout) v.findViewById(R.id.enter_rebate_tonnage_wrapper);
        etRebateTonnage = (TextInputEditText) enterTonnageWrapper.getEditText();
        enterAmountWrapper = (TextInputLayout) v.findViewById(R.id.enter_rebate_amount_wrapper);
        etRebateAmount = (TextInputEditText) enterAmountWrapper.getEditText();
        enterReceiptNumberWrapper = (TextInputLayout) v.findViewById(R.id.rebate_receipt_number_wrapper);
        etRebateReceiptNumber = (TextInputEditText) enterReceiptNumberWrapper.getEditText();
        enterPercentPreviousWrapper = (TextInputLayout) v.findViewById(R.id.rebate_percent_previous_wrapper);
        etPercentPrevious = (TextInputEditText) enterPercentPreviousWrapper.getEditText();
        etPercentPrevious.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "100")});

        View cancelSaveLayout = v.findViewById(R.id.rebate_cancel_save_button_bar);
        saveRebate = (Button) cancelSaveLayout.findViewById(R.id.btn_save);
        cancelRebate = (Button) cancelSaveLayout.findViewById(R.id.btn_cancel);

//        saveRebate = (Button) v.findViewById(R.id.btn_save_rebate);
//        cancelRebate = (Button) v.findViewById(R.id.btn_cancel_rebate);

        materialTypeArray = getResources().getStringArray(R.array.material);
        rebateLocationArray = getResources().getStringArray(R.array.rebate_location);
//        materialRebateArray = getResources().getIntArray(R.array.material_rebate);

        materialTypeSpinner = (Spinner) v.findViewById(R.id.spinner_material_type);
        rebateLocationSpinner = (Spinner) v.findViewById(R.id.spinner_rebate_location);
        weightUnitSpinner = (Spinner) v.findViewById(R.id.spinner_weight_unit);

        materialTypeSpinner.setAdapter(new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_spinner_item, materialTypeArray));

        rebateLocationSpinner.setAdapter(new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_spinner_item, rebateLocationArray));

        List<String> weightUnitArray = new ArrayList<>();
        weightUnitArray.add("kg");
        weightUnitArray.add("lb");
        weightUnitArray.add("t");

        weightUnitSpinner.setAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, weightUnitArray));

        materialTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                materialTypeString = materialTypeArray[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        rebateLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                rebateLocationString = rebateLocationArray[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        weightUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                switch (position) {
                    case 0:
                        tonnageMultiplier = 0.001;
                        break;
                    case 1:
                        tonnageMultiplier = 0.000453592;
                        break;
                    case 2:
                        tonnageMultiplier = 1;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        saveRebate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((!TextUtils.isEmpty(etRebateTonnage.getText())
                        && (!TextUtils.isEmpty(etRebateAmount.getText())))
                        && (!TextUtils.isEmpty(etRebateReceiptNumber.getText()))) {

                    final int rebateAmount = Integer.valueOf(etRebateAmount.getText().toString());
                    final int receiptNumber = Integer.valueOf(String.valueOf(etRebateReceiptNumber.getText()));
                    final int percentPrevious = Integer.valueOf(String.valueOf(etPercentPrevious.getText()));

                    final double rebateTonnage = Math.round((tonnageMultiplier
                            * Double.valueOf(String.valueOf(etRebateTonnage.getText()))) * 100.00) / 100.00;

                    Firebase fbrRebate = new Firebase(firebaseJournalRef + "rebate/" + rebateLocationString + " (" +
                            String.valueOf(etRebateReceiptNumber.getText()) + ")");

                    RebateObject rebate = new RebateObject();

                    rebate.setRebateLocation(rebateLocationString);
                    rebate.setRebateAmount(rebateAmount);
                    rebate.setReceiptNumber(receiptNumber);
                    rebate.setRebateTonnage(rebateTonnage);
                    rebate.setMaterialType(materialTypeString);
                    rebate.setPercentPrevious(percentPrevious);

                    fbrRebate.setValue(rebate);

                    Toast.makeText(getActivity(), "Rebate from " + rebateLocationString + " saved", Toast.LENGTH_SHORT).show();

                    getActivity().finish();

                } else {

                    if (TextUtils.isEmpty(etRebateTonnage.getText())) {
                        enterTonnageWrapper.setErrorEnabled(true);
                        enterTonnageWrapper.setError("Cannot be empty");
                    } else {
                        enterTonnageWrapper.setErrorEnabled(false);
                    }
                    if (TextUtils.isEmpty(etRebateAmount.getText())) {
                        enterAmountWrapper.setErrorEnabled(true);
                        enterAmountWrapper.setError("Cannot be empty");
                    } else {
                        enterAmountWrapper.setErrorEnabled(false);
                    }

                }
            }
        });

        cancelRebate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return v;

    }

}
