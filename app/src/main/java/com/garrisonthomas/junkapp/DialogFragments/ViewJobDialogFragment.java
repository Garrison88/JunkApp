package com.garrisonthomas.junkapp.dialogfragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.garrisonthomas.junkapp.DialogFragmentHelper;
import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.parseobjects.JobObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ViewJobDialogFragment extends DialogFragmentHelper {

    @Bind(R.id.tv_view_job_gross)
    TextView vjGross;
    @Bind(R.id.tv_view_job_net)
    TextView vjNet;
    @Bind(R.id.tv_view_pay_type)
    TextView vjPayType;
    @Bind(R.id.tv_view_job_time)
    TextView vjTime;
    @Bind(R.id.tv_view_job_receipt_number)
    TextView vjReceiptNumber;
    @Bind(R.id.tv_view_job_notes)
    TextView vjNotes;
    @Bind(R.id.tv_view_job_notes_display)
    TextView tvNotesDisplay;
    @Bind(R.id.btn_view_job_ok)
    Button okBtn;
    @Bind(R.id.btn_delete_job)
    ImageButton deleteJobBtn;
    private static int vjSID;
    public static String firebaseJournalRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.view_job_layout, container, false);

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
                DialogFragmentHelper.deleteItem(ViewJobDialogFragment.this,
                        firebaseJournalRef + "/jobs/" + String.valueOf(vjSID));
            }
        });

        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Bundle vjBundle = getArguments();
        vjSID = vjBundle.getInt("jobSpinnerSID");
        firebaseJournalRef = vjBundle.getString("firebaseJournalRef");
        dialog.setTitle("Job SID: " + String.valueOf(vjSID));
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    public void populateJobInfo() {

        Firebase ref = new Firebase(firebaseJournalRef + "/jobs");
        Query queryRef = ref.orderByChild("sid").equalTo(vjSID);
        queryRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {

                JobObject jobObject = snapshot.getValue(JobObject.class);
                vjGross.setText("$" + String.valueOf(jobObject.getGrossSale()));
                vjNet.setText("$" + String.valueOf(jobObject.getNetSale()));
                vjPayType.setText(jobObject.getPayType());
                vjTime.setText(jobObject.getStartTime() + " - " + jobObject.getEndTime());
                vjReceiptNumber.setText(String.valueOf(jobObject.getReceiptNumber()));
                // if there are no notes, do not display them
                if (jobObject.getJobNotes() != null) {
                    tvNotesDisplay.setVisibility(View.VISIBLE);
                    vjNotes.setVisibility(View.VISIBLE);
                    vjNotes.setText(jobObject.getJobNotes());
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
}