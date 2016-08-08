package com.garrisonthomas.junkapp.dialogfragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.garrisonthomas.junkapp.DialogFragmentHelper;
import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.parseobjects.NewFuel;
import com.parse.FindCallback;
import com.parse.ParseQuery;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by GarrisonThomas on 2015-10-22.
 */
public class ViewFuelDialogFragment extends DialogFragmentHelper {

    @Bind(R.id.tv_view_fuel_vendor)
    TextView vfVendor;
    @Bind(R.id.tv_view_fuel_cost)
    TextView vfCost;
    @Bind(R.id.btn_view_fuel_ok)
    Button okBtn;
    @Bind(R.id.btn_delete_fuel)
    ImageButton deleteFuelBtn;
    public static String currentJournalId, thisFuelID, fuelReceiptNumber;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.view_fuel_layout, container, false);

        ButterKnife.bind(this, v);

        populateFuelInfo();

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        deleteFuelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                deleteItem(ViewFuelDialogFragment.this, currentJournalId, thisFuelID, getActivity(), "NewFuel");
            }
        });

        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Bundle vfBundle = getArguments();
        fuelReceiptNumber = vfBundle.getString("fuelReceiptNumber");
        currentJournalId = vfBundle.getString("relatedJournalId");
        dialog.setTitle(fuelReceiptNumber);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    public void populateFuelInfo() {

        ParseQuery<NewFuel> query = ParseQuery.getQuery(NewFuel.class);
        query.setLimit(1);
        query.whereEqualTo("relatedJournal", currentJournalId);
        query.whereEqualTo("fuelReceiptNumber", fuelReceiptNumber);
        query.fromPin();
        query.findInBackground(new FindCallback<NewFuel>() {
            @Override
            public void done(List<NewFuel> list, com.parse.ParseException e) {

                if (e == null) {

                    for (NewFuel fuel : list) {

                        thisFuelID = fuel.getObjectId();

                        vfVendor.setText(fuel.getFuelVendor());
                        vfCost.setText("$" + String.valueOf(fuel.getFuelCost()));

                    }
                }
            }

        });

    }

}
