package com.garrisonthomas.junkapp.DialogFragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.garrisonthomas.junkapp.NewJob;
import com.garrisonthomas.junkapp.R;
import com.parse.FindCallback;
import com.parse.ParseQuery;

import java.util.List;

public class ViewJobDialogFragment extends DialogFragment {

    private static TextView vjGross, vjNet, vjPayType, vjReceiptNumber, vjNotes, tvNotesDisplay;
    private static int vjSSID;
    private static ProgressBar vjPbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.view_jobs_layout, container, false);

        vjGross = (TextView) v.findViewById(R.id.tv_view_job_gross);
        vjNet = (TextView) v.findViewById(R.id.tv_view_job_net);
        vjPayType = (TextView) v.findViewById(R.id.tv_view_pay_type);
        vjReceiptNumber = (TextView) v.findViewById(R.id.tv_view_job_receipt_number);
        tvNotesDisplay = (TextView) v.findViewById(R.id.tv_notes_display);
        tvNotesDisplay.setVisibility(View.GONE);
        vjNotes = (TextView) v.findViewById(R.id.tv_view_job_notes);
        vjNotes.setVisibility(View.GONE);

        vjPbar = (ProgressBar) v.findViewById(R.id.view_jobs_pbar);
        vjPbar.setVisibility(View.VISIBLE);

        populateJobInfo();

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

    public void populateJobInfo() {

        ParseQuery<NewJob> query = ParseQuery.getQuery(NewJob.class);
        query.setLimit(1);
        query.whereEqualTo("ssid", vjSSID);
        query.findInBackground(new FindCallback<NewJob>() {
            @Override
            public void done(List<NewJob> list, com.parse.ParseException e) {

                if (e == null) {

                    for (NewJob job : list) {

                        String grossSaleString = getString(R.string.dollar_sign) + String.valueOf(job.getGrossSale());
                        String netSaleString = getString(R.string.dollar_sign) + String.valueOf(job.getNetSale());
                        vjGross.setText(grossSaleString);
                        vjNet.setText(netSaleString);
                        vjPayType.setText(String.valueOf(job.getPayType()));
                        vjReceiptNumber.setText(String.valueOf(job.getReceiptNumber()));
                        vjNotes.setText(String.valueOf(job.getJobNotes()));

                        if (!TextUtils.isEmpty(vjNotes.getText())) {

                            tvNotesDisplay.setVisibility(View.VISIBLE);
                            vjNotes.setVisibility(View.VISIBLE);

                        }
                    }

                    vjPbar.setVisibility(View.GONE);
                }
            }

        });

    }


}