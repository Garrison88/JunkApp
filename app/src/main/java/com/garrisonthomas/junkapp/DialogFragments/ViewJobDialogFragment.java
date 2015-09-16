package com.garrisonthomas.junkapp.DialogFragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.garrisonthomas.junkapp.R;

public class ViewJobDialogFragment extends DialogFragment {

    TextView vjGross, vjNet, vjPayType, vjReceiptNumber;
    int vjSSID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.view_jobs_layout, container, false);


        vjGross = (TextView) v.findViewById(R.id.tv_view_job_gross);
        vjNet = (TextView) v.findViewById(R.id.tv_view_job_net);
        vjPayType = (TextView) v.findViewById(R.id.tv_view_pay_type);
        vjReceiptNumber = (TextView) v.findViewById(R.id.tv_view_job_receipt_number);

        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Bundle vjBundle = getArguments();
        vjSSID = vjBundle.getInt("spinnerSSID");
        dialog.setTitle(String.valueOf(vjSSID));
        return dialog;
    }


}