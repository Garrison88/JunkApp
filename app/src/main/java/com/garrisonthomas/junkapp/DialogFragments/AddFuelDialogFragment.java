package com.garrisonthomas.junkapp.DialogFragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.garrisonthomas.junkapp.DailyJournal;
import com.garrisonthomas.junkapp.NewFuel;
import com.garrisonthomas.junkapp.NewJob;
import com.garrisonthomas.junkapp.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddFuelDialogFragment extends DialogFragment {

    EditText etFuelVendor, etGrossCost, etNetCost, etReceiptNumber;
    Button saveFuel;
    String todaysDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.add_fuel_layout, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        etFuelVendor = (EditText) v.findViewById(R.id.et_fuel_vendor);
        etGrossCost = (EditText) v.findViewById(R.id.et_fuel_gross_cost);
        etNetCost = (EditText) v.findViewById(R.id.et_fuel_net_cost);
        etReceiptNumber = (EditText) v.findViewById(R.id.et_fuel_receipt_number);

        Date date = new Date();
        SimpleDateFormat df2 = new SimpleDateFormat("EEE, dd MMM yyyy");

        todaysDate = df2.format(date);

        saveFuel = (Button) v.findViewById(R.id.btn_save_fuel);

        saveFuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(etFuelVendor.getText())
                        && (!TextUtils.isEmpty(etNetCost.getText()))) {

                    ParseQuery<DailyJournal> query = ParseQuery.getQuery("DailyJournal");
                    query.whereEqualTo("date", todaysDate);
                    query.setLimit(1);
                    query.findInBackground(new FindCallback<DailyJournal>() {
                        public void done(List<DailyJournal> list, ParseException e) {
                            if (e == null) {

                                for (DailyJournal newJournal : list) {

                                    NewFuel newFuel = new NewFuel();
                                    newFuel.setRelatedJournal(newJournal.getObjectId());
                                    newFuel.setFuelVendor(String.valueOf(etFuelVendor.getText()));
                                    newFuel.setFuelGrossCost(Double.valueOf(String.valueOf(etGrossCost.getText())));
                                    newFuel.setFuelNetCost(Double.valueOf(String.valueOf(etNetCost.getText())));
                                    newFuel.setFuelReceiptNumber(String.valueOf(etReceiptNumber.getText()));

                                    newFuel.saveInBackground();

                                    newJournal.saveInBackground();

                                }

                            } else {
                                Log.d("score", "Error: " + e.getMessage());
                            }
                        }
                    });

                    Toast.makeText(getActivity(), "Fuel entry saved", Toast.LENGTH_SHORT).show();

                    dismiss();

                } else {

                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_LONG).show();

                }
            }
        });

        return v;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
