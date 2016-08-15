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
import com.garrisonthomas.junkapp.entryobjects.DumpObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by GarrisonThomas on 2015-10-22.
 */
public class ViewDumpDialogFragment extends DialogFragmentHelper {

    @Bind(R.id.tv_view_dump_gross_cost)
    TextView vdGross;
    @Bind(R.id.tv_view_dump_tonnage)
    TextView vdTonnage;
    @Bind(R.id.tv_view_dump_percent_previous)
    TextView vdPercentPrevious;
    @Bind(R.id.v_d_percent_previous_text)
    TextView vdPercentPreviousText;
    @Bind(R.id.btn_view_dump_ok)
    Button okBtn;
    @Bind(R.id.btn_delete_dump)
    ImageButton deleteDumpBtn;
    public static String dumpName, firebaseJournalRef;
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
                deleteItem(ViewDumpDialogFragment.this, firebaseJournalRef + "/dumps/" +
                        dumpName);
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
        firebaseJournalRef = vdBundle.getString("firebaseJournalRef");
        dialog.setTitle(dumpName);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    public void populateDumpInfo() {

        Firebase ref = new Firebase(firebaseJournalRef + "dumps");
        Query queryRef = ref.orderByChild("dumpReceiptNumber").equalTo(dumpReceiptNumber);
        queryRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {

                DumpObject dumpObject = snapshot.getValue(DumpObject.class);
                vdGross.setText("$" + String.valueOf(dumpObject.getGrossCost()));
                vdTonnage.setText(String.valueOf(dumpObject.getTonnage()));
                if (dumpObject.getPercentPrevious() != 0) {
                    vdPercentPrevious.setText(String.valueOf(dumpObject.getPercentPrevious()) + "%");
                    vdPercentPreviousText.setVisibility(View.VISIBLE);
                    vdPercentPrevious.setVisibility(View.VISIBLE);
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
