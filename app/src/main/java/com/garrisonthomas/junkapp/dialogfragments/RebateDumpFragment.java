package com.garrisonthomas.junkapp.dialogfragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.garrisonthomas.junkapp.R;

/**
 * Created by Garrison on 2016-07-04.
 */
public class RebateDumpFragment extends Fragment {

    private static EditText etRebateWeight, etRebateAmount, etRebateReceiptNumber;
    private static Button saveRebate, cancelRebate;
    private static Spinner materialTypeSpinner, rebateLocationSpinner;
    private static String[] materialTypeArray, rebateLocationArray;
    private static int[] materialRebateArray;
    private static int pricePerTonne;
    private static String materialNameString, rebateLocationString, firebaseJournalRef;
    private SharedPreferences preferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.add_rebate_dump_layout, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        firebaseJournalRef = preferences.getString("firebaseRef", "none");

        etRebateWeight = (EditText) v.findViewById(R.id.et_rebate_weight);
        etRebateAmount = (EditText) v.findViewById(R.id.et_rebate_amount);
        etRebateReceiptNumber = (EditText) v.findViewById(R.id.et_rebate_receipt_number);

        saveRebate = (Button) v.findViewById(R.id.btn_save_rebate);
        cancelRebate = (Button) v.findViewById(R.id.btn_cancel_rebate);

        materialTypeArray = getResources().getStringArray(R.array.material);
        rebateLocationArray = getResources().getStringArray(R.array.rebate_location);
        materialRebateArray = getResources().getIntArray(R.array.material_rebate);

        materialTypeSpinner = (Spinner) v.findViewById(R.id.spinner_material_type);
        rebateLocationSpinner = (Spinner) v.findViewById(R.id.spinner_rebate_location);

        materialTypeSpinner.setAdapter(new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_dropdown_item_1line, materialTypeArray));

        rebateLocationSpinner.setAdapter(new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_dropdown_item_1line, rebateLocationArray));

        materialTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                materialNameString = materialTypeArray[position];
                pricePerTonne = materialRebateArray[position];

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

        etRebateWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {


            }

        });

        saveRebate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((!TextUtils.isEmpty(etRebateWeight.getText())
                        || (!TextUtils.isEmpty(etRebateAmount.getText())))
                        && (!TextUtils.isEmpty(etRebateReceiptNumber.getText()))) {

                    getActivity().finish();

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
