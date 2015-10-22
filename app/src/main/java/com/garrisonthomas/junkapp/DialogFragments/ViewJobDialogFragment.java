package com.garrisonthomas.junkapp.DialogFragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.garrisonthomas.junkapp.ParseObjects.DailyJournal;
import com.garrisonthomas.junkapp.ParseObjects.NewDump;
import com.garrisonthomas.junkapp.ParseObjects.NewFuel;
import com.garrisonthomas.junkapp.ParseObjects.NewJob;
import com.garrisonthomas.junkapp.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ViewJobDialogFragment extends DialogFragment {

    @Bind(R.id.tv_view_job_gross)
    TextView vjGross;
    @Bind(R.id.tv_view_job_net)
    TextView vjNet;
    @Bind(R.id.tv_view_pay_type)
    TextView vjPayType;
    @Bind(R.id.tv_view_job_receipt_number)
    TextView vjReceiptNumber;
    @Bind(R.id.tv_view_job_notes)
    TextView vjNotes;
    @Bind(R.id.tv_notes_display)
    TextView tvNotesDisplay;
    @Bind(R.id.view_jobs_pbar)
    ProgressBar vjPbar;
    @Bind(R.id.btn_delete_job)
    ImageButton deleteJobBtn;
    private static int vjSSID;
    public static String currentJournalId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.view_jobs_layout, container, false);

        ButterKnife.bind(this, v);

        tvNotesDisplay.setVisibility(View.GONE);
        vjNotes.setVisibility(View.GONE);
        vjPbar.setVisibility(View.VISIBLE);

        populateJobInfo();

        deleteJobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteJob();
            }
        });

        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Bundle vjBundle = getArguments();
        vjSSID = vjBundle.getInt("spinnerSSID");
        currentJournalId = vjBundle.getString("relatedJournalId");
        dialog.setTitle(String.valueOf(vjSSID));

        return dialog;
    }

    public void populateJobInfo() {

        ParseQuery<NewJob> query = ParseQuery.getQuery(NewJob.class);
        query.setLimit(1);
        query.whereEqualTo("relatedJournal", currentJournalId);
        query.whereEqualTo("ssid", vjSSID);
        query.findInBackground(new FindCallback<NewJob>() {
            @Override
            public void done(List<NewJob> list, com.parse.ParseException e) {

                vjPbar.setVisibility(View.GONE);

                if (e == null) {

                    for (NewJob job : list) {

                        if (isAdded()) {

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


                    }
                }
            }

        });

    }

    private void deleteJob() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Delete this job?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        vjPbar.setVisibility(View.VISIBLE);

                        final ParseQuery<NewJob> query = ParseQuery.getQuery(NewJob.class);
                        query.setLimit(1);
                        query.whereEqualTo("relatedJournal", currentJournalId);
                        query.whereEqualTo("ssid", vjSSID);

                        query.findInBackground(new FindCallback<NewJob>() {
                            @Override
                            public void done(List<NewJob> list, ParseException e) {

                                if (e == null) {

                                    for (NewJob nj : list) {

                                        try {
                                            nj.delete();
                                        } catch (ParseException e1) {
                                            e1.printStackTrace();
                                        }
                                    }

                                }
                                Toast.makeText(getActivity(), "Job number " + vjSSID + " successfully deleted",
                                        Toast.LENGTH_SHORT).show();
                                ViewJobDialogFragment.this.dismiss();
                            }
                        });

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
}