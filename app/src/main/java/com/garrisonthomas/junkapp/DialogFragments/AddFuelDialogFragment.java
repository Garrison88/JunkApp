package com.garrisonthomas.junkapp.dialogfragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.garrisonthomas.junkapp.DialogFragmentHelper;
import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.entryobjects.FuelObject;

public class AddFuelDialogFragment extends DialogFragmentHelper {

    private EditText etFuelVendor, etFuelCost, etReceiptNumber;
    private Button saveFuel, cancelFuel;
    private String currentJournalRef;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.add_fuel_layout, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        currentJournalRef = preferences.getString("currentJournalRef", null);

        etFuelVendor = (EditText) v.findViewById(R.id.et_fuel_vendor);
        etFuelCost = (EditText) v.findViewById(R.id.et_fuel_cost);
        etReceiptNumber = (EditText) v.findViewById(R.id.et_fuel_receipt_number);
        etReceiptNumber.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        View cancelSaveLayout = v.findViewById(R.id.fuel_cancel_save_button_bar);
        saveFuel = (Button) cancelSaveLayout.findViewById(R.id.btn_save);
        cancelFuel = (Button) cancelSaveLayout.findViewById(R.id.btn_cancel);

        saveFuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(etFuelVendor.getText())
                        && (!TextUtils.isEmpty(etFuelCost.getText()))
                        && (!TextUtils.isEmpty(etReceiptNumber.getText()))) {

                    Firebase fbrFuel = new Firebase(currentJournalRef + "fuel/"
                            + String.valueOf(etReceiptNumber.getText()));

                    FuelObject fuel = new FuelObject();

                    fuel.setFuelVendor(String.valueOf(etFuelVendor.getText()));
                    fuel.setFuelCost(Double.valueOf(String.valueOf(etFuelCost.getText())));
                    fuel.setFuelReceiptNumber(String.valueOf(etReceiptNumber.getText()));

                    fbrFuel.setValue(fuel);

                    Toast.makeText(getActivity(), "Fuel entry at " + String.valueOf(etFuelVendor.getText()) +
                            " saved", Toast.LENGTH_SHORT).show();

                    dismiss();

                } else {

                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();

                }
            }
        });

        cancelFuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        setCancelable(false);
        return v;

    }
}
