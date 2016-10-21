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
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.entryobjects.RebateObject;

/**
 * Created by Garrison on 2016-07-04.
 */
public class RebateDumpFragment extends Fragment {

    private EditText etRebateWeight, etRebateAmount, etRebateReceiptNumber;
    private Button saveRebate, cancelRebate;
    private Spinner materialTypeSpinner, rebateLocationSpinner;
    private String[] materialTypeArray, rebateLocationArray;
    private int[] materialRebateArray;
    private int pricePerTonne;
    private String materialTypeString, rebateLocationString, firebaseJournalRef;
    private SharedPreferences preferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.add_rebate_dump_layout, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        firebaseJournalRef = preferences.getString("currentJournalRef", null);

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
                android.R.layout.simple_spinner_item, materialTypeArray));

        rebateLocationSpinner.setAdapter(new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_spinner_item, rebateLocationArray));

        materialTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                materialTypeString = materialTypeArray[position];
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

                    final double rebateAmount = Double.valueOf(String.valueOf(etRebateAmount.getText()));
                    final int receiptNumber = Integer.valueOf(String.valueOf(etRebateReceiptNumber.getText()));
                    final int rebateWeight = Integer.valueOf(String.valueOf(etRebateWeight.getText()));

                    Firebase fbrRebate = new Firebase(firebaseJournalRef + "rebate/" + rebateLocationString + " (" +
                            String.valueOf(etRebateReceiptNumber.getText()) + ")");

                    RebateObject rebate = new RebateObject();

                    rebate.setRebateLocation(rebateLocationString);
                    rebate.setRebateAmount(rebateAmount);
                    rebate.setReceiptNumber(receiptNumber);
                    rebate.setRebateWeight(rebateWeight);
                    rebate.setMaterialType(materialTypeString);

                    fbrRebate.setValue(rebate);

                    Toast.makeText(getActivity(), "Rebate from " + rebateLocationString + " saved", Toast.LENGTH_SHORT).show();

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
