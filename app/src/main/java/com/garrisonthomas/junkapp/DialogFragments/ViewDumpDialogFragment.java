package com.garrisonthomas.junkapp.dialogfragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.ViewItemHelper;
import com.garrisonthomas.junkapp.parseobjects.NewDump;
import com.parse.FindCallback;
import com.parse.ParseQuery;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by GarrisonThomas on 2015-10-22.
 */
public class ViewDumpDialogFragment extends ViewItemHelper {

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
    @Bind(R.id.btn_view_dump_ok)
    Button okBtn;
    @Bind(R.id.btn_delete_dump)
    ImageButton deleteDumpBtn;
    public static String currentJournalId, thisDumpID, dumpName;
    public static int dumpReceiptNumber;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.view_dump_layout, container, false);

        ButterKnife.bind(this, v);

        vdPercentPreviousText.setVisibility(View.GONE);
        vdPercentPrevious.setVisibility(View.GONE);

        populateDumpInfo();

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        deleteDumpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(ViewDumpDialogFragment.this, currentJournalId, thisDumpID, getActivity(), "NewDump");
            }
        });

        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Bundle vdBundle = getArguments();
        dumpName = vdBundle.getString("dumpName");
        dumpReceiptNumber = vdBundle.getInt("dumpReceiptNumber");
        currentJournalId = vdBundle.getString("relatedJournalId");
        dialog.setTitle(dumpName);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    public void populateDumpInfo() {

        ParseQuery<NewDump> query = ParseQuery.getQuery(NewDump.class);
        query.setLimit(1);
        query.whereEqualTo("relatedJournal", currentJournalId);
        query.whereEqualTo("dumpReceiptNumber", dumpReceiptNumber);
        query.fromPin();
        query.findInBackground(new FindCallback<NewDump>() {
            @Override
            public void done(List<NewDump> list, com.parse.ParseException e) {

                if (e == null) {

                    for (NewDump dump : list) {

                            thisDumpID = dump.getObjectId();

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

        });

    }

}
