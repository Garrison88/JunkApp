package com.garrisonthomas.junkapp.dialogfragments;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.ViewItemHelper;
import com.garrisonthomas.junkapp.parseobjects.NewJob;
import com.parse.FindCallback;
import com.parse.ParseQuery;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Garrison on 2016-06-16.
 */
public class ViewQuoteDialogFragment extends ViewItemHelper {

    @Bind(R.id.tv_view_job_gross)
    TextView vjGross;
    @Bind(R.id.tv_view_job_net)
    TextView vjNet;
    @Bind(R.id.tv_view_pay_type)
    TextView vjPayType;
    @Bind(R.id.tv_view_time)
    TextView vjTime;
    @Bind(R.id.tv_view_job_receipt_number)
    TextView vjReceiptNumber;
    @Bind(R.id.tv_view_job_notes)
    TextView vjNotes;
    @Bind(R.id.tv_notes_display)
    TextView tvNotesDisplay;
    @Bind(R.id.btn_view_job_ok)
    Button okBtn;
    @Bind(R.id.btn_delete_job)
    ImageButton deleteJobBtn;
    private static int vjSID;
    public static String currentJournalId;
    public static String thisJobId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.view_jobs_layout, container, false);

        ButterKnife.bind(this, v);

        tvNotesDisplay.setVisibility(View.GONE);
        vjNotes.setVisibility(View.GONE);

        populateJobInfo();

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        deleteJobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewItemHelper.deleteItem(ViewQuoteDialogFragment.this, currentJournalId, thisJobId, getActivity(), "NewQuote");
            }
        });

        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Bundle vjBundle = getArguments();
        vjSID = vjBundle.getInt("spinnerSID");
        currentJournalId = vjBundle.getString("relatedJournalId");
        dialog.setTitle("SID: " + String.valueOf(vjSID));
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    public void populateJobInfo() {

        ParseQuery<NewJob> query = ParseQuery.getQuery(NewJob.class);
        query.setLimit(1);
        query.whereEqualTo("relatedJournal", currentJournalId);
        query.whereEqualTo("sid", vjSID);
        query.fromPin();
        query.findInBackground(new FindCallback<NewJob>() {
            @Override
            public void done(List<NewJob> list, com.parse.ParseException e) {

                if (e == null) {

                    for (NewJob job : list) {

                        if (isAdded()) {

                            thisJobId = job.getObjectId();
                            String grossSaleString = getString(R.string.dollar_sign) + job.getGrossSale();
                            String netSaleString = getString(R.string.dollar_sign) + job.getNetSale();
                            String startEndTime = job.getStartTime() + " - " + job.getEndTime();
                            vjGross.setText(grossSaleString);
                            vjNet.setText(netSaleString);
                            vjPayType.setText(job.getPayType());
                            vjTime.setText(startEndTime);
                            vjReceiptNumber.setText(String.valueOf(job.getReceiptNumber()));
                            vjNotes.setText(job.getJobNotes());

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

}
