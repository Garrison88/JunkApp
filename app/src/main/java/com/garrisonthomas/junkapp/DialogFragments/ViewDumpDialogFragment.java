package com.garrisonthomas.junkapp.dialogfragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.garrisonthomas.junkapp.parseobjects.NewDump;
import com.garrisonthomas.junkapp.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by GarrisonThomas on 2015-10-22.
 */
public class ViewDumpDialogFragment extends DialogFragment {

    @Bind(R.id.tv_view_dump_gross_cost)
    TextView vdGross;
    @Bind(R.id.tv_view_dump_net_cost)
    TextView vdNet;
    @Bind(R.id.tv_view_dump_receipt_number)
    TextView vdReceiptNumber;
    @Bind(R.id.tv_view_dump_percent_previous)
    TextView vdPercentPrevious;
    @Bind(R.id.v_d_percent_previous_text)
    TextView vdPercentPreviousText;
    @Bind(R.id.btn_delete_dump)
    ImageButton deleteDumpBtn;
    public static String currentJournalId, thisDumpId, dumpName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.view_dumps_layout, container, false);

        ButterKnife.bind(this, v);

        vdPercentPreviousText.setVisibility(View.GONE);
        vdPercentPrevious.setVisibility(View.GONE);

        populateDumpInfo();

        deleteDumpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDump();
            }
        });

        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Bundle vdBundle = getArguments();
        dumpName = vdBundle.getString("dumpName");
        currentJournalId = vdBundle.getString("relatedJournalId");
        dialog.setTitle(String.valueOf(dumpName));

        return dialog;
    }

    public void populateDumpInfo() {

        ParseQuery<NewDump> query = ParseQuery.getQuery(NewDump.class);
        query.setLimit(1);
        query.whereEqualTo("relatedJournal", currentJournalId);
        query.whereEqualTo("dumpName", dumpName);
        query.fromPin();
        query.findInBackground(new FindCallback<NewDump>() {
            @Override
            public void done(List<NewDump> list, com.parse.ParseException e) {

                if (e == null) {

                    for (NewDump dump : list) {

                        if (isAdded()) {

                            thisDumpId = dump.getObjectId();

                            String grossSaleString = getString(R.string.dollar_sign) + String.valueOf(dump.getGrossCost());
                            String netSaleString = getString(R.string.dollar_sign) + String.valueOf(dump.getNetCost());
                            vdGross.setText(grossSaleString);
                            vdNet.setText(netSaleString);
                            vdReceiptNumber.setText(String.valueOf(dump.getDumpReceiptNumber()));
                            vdPercentPrevious.setText(String.valueOf(dump.getPercentPrevious()) + "%");


                            if (!vdPercentPrevious.getText().equals("0%")) {

                                vdPercentPreviousText.setVisibility(View.VISIBLE);
                                vdPercentPrevious.setVisibility(View.VISIBLE);

                            }
                        }


                    }
                }
            }

        });

    }

    private void deleteDump() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Delete this dump?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        final ParseQuery<NewDump> query = ParseQuery.getQuery(NewDump.class);
                        query.setLimit(1);
                        query.fromPin();
                        query.whereEqualTo("relatedJournal", currentJournalId);
                        query.whereEqualTo("objectId", thisDumpId);

                        query.findInBackground(new FindCallback<NewDump>() {
                            @Override
                            public void done(List<NewDump> list, ParseException e) {

                                if (e == null) {

                                    for (NewDump nd : list) {

                                        nd.deleteEventually();
                                    }

                                }
                                Toast.makeText(getActivity(), "Dump at " + dumpName + " successfully deleted",
                                        Toast.LENGTH_SHORT).show();
                                ViewDumpDialogFragment.this.dismiss();
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
