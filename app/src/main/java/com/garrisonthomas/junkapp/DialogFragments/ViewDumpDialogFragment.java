//package com.garrisonthomas.junkapp.DialogFragments;
//
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.app.DialogFragment;
//import android.support.v7.app.AlertDialog;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.garrisonthomas.junkapp.ParseObjects.NewJob;
//import com.garrisonthomas.junkapp.R;
//import com.parse.FindCallback;
//import com.parse.ParseException;
//import com.parse.ParseQuery;
//
//import java.util.List;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//
///**
// * Created by GarrisonThomas on 2015-10-22.
// */
//public class ViewDumpDialogFragment extends DialogFragment {
//
//    @Bind(R.id.tv_view_dump_site)
//    TextView vdSite;
//    @Bind(R.id.tv_view_dump_gross_cost)
//    TextView vdGross;
//    @Bind(R.id.tv_view_dump_net_cost)
//    TextView vdNet;
//    @Bind(R.id.tv_view_dump_receipt_number)
//    TextView vdReceiptNumber;
//    @Bind(R.id.tv_view_dump_percent_previous)
//    TextView vdPercentPrevious;
//    @Bind(R.id.v_d_percent_previous_text)
//    TextView vdPercentPreviousText;
//    @Bind(R.id.view_dumps_pbar)
//    ProgressBar vdPbar;
//    @Bind(R.id.btn_delete_job)
//    ImageButton deleteJobBtn;
//    public static String currentJournalId;
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        final View v = inflater.inflate(R.layout.view_dumps_layout, container, false);
//
//        ButterKnife.bind(this, v);
//
//        vdPercentPreviousText.setVisibility(View.GONE);
//        vdPercentPrevious.setVisibility(View.GONE);
//        vdPbar.setVisibility(View.VISIBLE);
//
//        populateDumpInfo();
//
//        deleteJobBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                deleteJob();
//            }
//        });
//
//        return v;
//    }
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//
//        Dialog dialog = super.onCreateDialog(savedInstanceState);
//        Bundle vdBundle = getArguments();
//        vdSSID = vdBundle.getInt("spinnerSSID");
//        currentJournalId = vdBundle.getString("relatedJournalId");
//        dialog.setTitle(String.valueOf(vdSite));
//
//        return dialog;
//    }
//
//    public void populateDumpInfo() {
//
//        ParseQuery<NewJob> query = ParseQuery.getQuery(NewJob.class);
//        query.setLimit(1);
//        query.whereEqualTo("relatedJournal", currentJournalId);
//        query.whereEqualTo("ssid", vjSSID);
//        query.findInBackground(new FindCallback<NewJob>() {
//            @Override
//            public void done(List<NewJob> list, com.parse.ParseException e) {
//
//                vdPbar.setVisibility(View.GONE);
//
//                if (e == null) {
//
//                    for (NewJob job : list) {
//
//                        if (isAdded()) {
//
//                            String grossSaleString = getString(R.string.dollar_sign) + String.valueOf(job.getGrossSale());
//                            String netSaleString = getString(R.string.dollar_sign) + String.valueOf(job.getNetSale());
//                            vjGross.setText(grossSaleString);
//                            vjNet.setText(netSaleString);
//                            vjPayType.setText(String.valueOf(job.getPayType()));
//                            vjReceiptNumber.setText(String.valueOf(job.getReceiptNumber()));
//                            vjNotes.setText(String.valueOf(job.getJobNotes()));
//
//                            if (!TextUtils.isEmpty(vjNotes.getText())) {
//
//                                tvNotesDisplay.setVisibility(View.VISIBLE);
//                                vjNotes.setVisibility(View.VISIBLE);
//
//                            }
//                        }
//
//
//                    }
//                }
//            }
//
//        });
//
//    }
//
//    private void deleteJob() {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setMessage("Delete this job?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//
//                        vdPbar.setVisibility(View.VISIBLE);
//
//                        final ParseQuery<NewJob> query = ParseQuery.getQuery(NewJob.class);
//                        query.setLimit(1);
//                        query.whereEqualTo("relatedJournal", currentJournalId);
////                        query.whereEqualTo("dumpName", );
//
//                        query.findInBackground(new FindCallback<NewJob>() {
//                            @Override
//                            public void done(List<NewJob> list, ParseException e) {
//
//                                if (e == null) {
//
//                                    for (NewJob nj : list) {
//
//                                        try {
//                                            nj.delete();
//                                        } catch (ParseException e1) {
//                                            e1.printStackTrace();
//                                        }
//                                    }
//
//                                }
//                                Toast.makeText(getActivity(), "Dump successfully deleted",
//                                        Toast.LENGTH_SHORT).show();
//                                ViewDumpDialogFragment.this.dismiss();
//                            }
//                        });
//
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.show();
//
//    }
//
//}
