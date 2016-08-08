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

import com.firebase.client.Firebase;
import com.garrisonthomas.junkapp.DialogFragmentHelper;
import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.parseobjects.NewFuel;

public class AddFuelDialogFragment extends DialogFragmentHelper {

    private EditText etFuelVendor, etFuelCost, etReceiptNumber;
    private static Button saveFuel, cancelFuel;
    private String currentJournalId;
    private SharedPreferences preferences;
    private Firebase fbrFuel;


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
        etFuelCost = (EditText) v.findViewById(R.id.et_fuel_cost);
        etReceiptNumber = (EditText) v.findViewById(R.id.et_fuel_receipt_number);

        saveFuel = (Button) v.findViewById(R.id.btn_save_fuel);
        cancelFuel = (Button) v.findViewById(R.id.btn_cancel_fuel);



        saveFuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(etFuelVendor.getText())
                        && (!TextUtils.isEmpty(etFuelCost.getText()))
                        && (!TextUtils.isEmpty(etReceiptNumber.getText()))) {

                    NewFuel newFuel = new NewFuel();
                    newFuel.setRelatedJournal(currentJournalId);
                    newFuel.setFuelVendor(String.valueOf(etFuelVendor.getText()));
                    newFuel.setFuelCost(Double.valueOf(String.valueOf(etFuelCost.getText())));
                    newFuel.setFuelReceiptNumber(String.valueOf(etReceiptNumber.getText()));

                    fbrFuel = new Firebase(preferences.getString("firebaseURL", "none") + "/" + "fuel/" +
                            String.valueOf(etFuelVendor.getText()) + " (" + String.valueOf(etReceiptNumber.getText()) + ")");

//                    fbrJob.setValue(newFuel);

                    fbrFuel.child("cost").setValue(Double.valueOf(etFuelCost.getText().toString()));

                    newFuel.saveEventually();
                    newFuel.pinInBackground();

                    Toast.makeText(getActivity(), "Fuel entry saved", Toast.LENGTH_SHORT).show();

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
