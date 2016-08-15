package com.garrisonthomas.junkapp.dialogfragments;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.garrisonthomas.junkapp.entryobjects.QuoteObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Garrison on 2016-06-16.
 */
public class ViewQuoteDialogFragment extends DialogFragmentHelper {

    @Bind(R.id.tv_view_quote_low_end)
    TextView vqLowEnd;
    @Bind(R.id.tv_view_quote_high_end)
    TextView vqHighEnd;
    @Bind(R.id.tv_view_quote_time)
    TextView vqTime;
    @Bind(R.id.tv_view_quote_notes)
    TextView vqNotes;
    @Bind(R.id.tv_view_quote_notes_display)
    TextView tvQuoteNotesDisplay;
    @Bind(R.id.btn_view_quote_ok)
    Button okBtn;
    @Bind(R.id.btn_delete_quote)
    ImageButton deleteQuoteBtn;
    private static int vqSID;
    public static String firebaseJournalRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.view_quote_layout, container, false);

        ButterKnife.bind(this, v);

        tvQuoteNotesDisplay.setVisibility(View.GONE);
        vqNotes.setVisibility(View.GONE);

        Typeface custom_font = Typeface.createFromAsset(v.getContext().getApplicationContext().getAssets(), "fonts/WorkSans-Regular.ttf");

        vqNotes.setTypeface(custom_font);

        populateQuoteInfo();

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        deleteQuoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragmentHelper.deleteItem(ViewQuoteDialogFragment.this,
                        firebaseJournalRef + "/quotes/" + String.valueOf(vqSID));
            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Bundle vqBundle = getArguments();
        vqSID = vqBundle.getInt("quoteSpinnerSID");
        firebaseJournalRef = vqBundle.getString("firebaseJournalRef");
        dialog.setTitle("Quote SID: " + String.valueOf(vqSID));
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    public void populateQuoteInfo() {

        Firebase ref = new Firebase(firebaseJournalRef + "quotes");
        Query queryRef = ref.orderByChild("quoteSID").equalTo(vqSID);
        queryRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {

                QuoteObject quoteObject = snapshot.getValue(QuoteObject.class);
                String lowEndString = getString(R.string.dollar_sign) + quoteObject.getLowEnd();
                String highEndString = getString(R.string.dollar_sign) + quoteObject.getHighEnd();
                String startEndTime = quoteObject.getQuoteStartTime() + " - " + quoteObject.getQuoteEndTime();
                vqLowEnd.setText(lowEndString);
                vqHighEnd.setText(highEndString);
                vqTime.setText(startEndTime);
                vqNotes.setText(quoteObject.getQuoteNotes());

                if (!TextUtils.isEmpty(vqNotes.getText())) {

                    tvQuoteNotesDisplay.setVisibility(View.VISIBLE);
                    vqNotes.setVisibility(View.VISIBLE);

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
