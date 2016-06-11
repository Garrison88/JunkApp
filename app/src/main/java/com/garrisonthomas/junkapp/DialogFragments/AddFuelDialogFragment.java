package com.garrisonthomas.junkapp.dialogfragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.garrisonthomas.junkapp.AddItemDialogFragment;
import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.Utils;
import com.garrisonthomas.junkapp.parseobjects.NewFuel;

public class AddFuelDialogFragment extends AddItemDialogFragment {

    private EditText etFuelVendor, etGrossCost, etNetCost, etReceiptNumber;
    private static Button saveFuel, cancelFuel;
    private String currentJournalId;
    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.add_fuel_layout, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        currentJournalId = preferences.getString("universalJournalId", "none");

        etFuelVendor = (EditText) v.findViewById(R.id.et_fuel_vendor);
        etGrossCost = (EditText) v.findViewById(R.id.et_fuel_gross_cost);
        etNetCost = (EditText) v.findViewById(R.id.et_fuel_net_cost);
        etReceiptNumber = (EditText) v.findViewById(R.id.et_fuel_receipt_number);

        saveFuel = (Button) v.findViewById(R.id.btn_save_fuel);
        cancelFuel = (Button) v.findViewById(R.id.btn_cancel_fuel);

        saveFuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(etFuelVendor.getText())
                        && (!TextUtils.isEmpty(etNetCost.getText()))) {

                    Utils.hideKeyboard(v, getActivity());

                    NewFuel newFuel = new NewFuel();
                    newFuel.setRelatedJournal(currentJournalId);
                    newFuel.setFuelVendor(String.valueOf(etFuelVendor.getText()));
                    if (!TextUtils.isEmpty(etGrossCost.getText())) {
                        newFuel.setFuelGrossCost(Double.valueOf(String.valueOf(etGrossCost.getText())));
                    } else {
                        newFuel.setFuelGrossCost(0);
                    }
                    newFuel.setFuelNetCost(Double.valueOf(String.valueOf(etNetCost.getText())));
                    if (!TextUtils.isEmpty(etReceiptNumber.getText())) {
                        newFuel.setFuelReceiptNumber(String.valueOf(etReceiptNumber.getText()));
                    } else {
                        newFuel.setFuelReceiptNumber("Not provided");
                    }

                    newFuel.saveEventually();
                    newFuel.pinInBackground();

                    Toast.makeText(getActivity(), "Fuel entry saved", Toast.LENGTH_SHORT).show();

                    dismiss();

                } else {

                    Toast.makeText(getActivity(), "Please fill all required fields", Toast.LENGTH_SHORT).show();

                }
            }
        });

        cancelFragment(cancelFuel);

        setCancelable(false);
        return v;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Utils.showKeyboardInDialog(getDialog());
    }

}
