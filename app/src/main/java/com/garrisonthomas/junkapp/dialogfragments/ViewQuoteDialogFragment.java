package com.garrisonthomas.junkapp.dialogfragments;

import android.app.Dialog;
import android.graphics.Typeface;
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
import com.garrisonthomas.junkapp.parseobjects.NewQuote;
import com.parse.FindCallback;
import com.parse.ParseQuery;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Garrison on 2016-06-16.
 */
public class ViewQuoteDialogFragment extends ViewItemHelper {

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
    public static String currentJournalId;
    public static String thisQuoteId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.view_quote_layout, container, false);

        ButterKnife.bind(this, v);

        tvQuoteNotesDisplay.setVisibility(View.GONE);
        vqNotes.setVisibility(View.GONE);

        Typeface custom_font = Typeface.createFromAsset(v.getContext().getApplicationContext().getAssets(),  "fonts/WorkSans-Regular.ttf");

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
                ViewItemHelper.deleteItem(ViewQuoteDialogFragment.this, currentJournalId, thisQuoteId, getActivity(), "NewQuote");
            }
        });

        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Bundle vqBundle = getArguments();
        vqSID = vqBundle.getInt("quoteSpinnerSID");
        currentJournalId = vqBundle.getString("relatedJournalId");
        dialog.setTitle("Quote SID: " + String.valueOf(vqSID));
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    public void populateQuoteInfo() {

        ParseQuery<NewQuote> query = ParseQuery.getQuery(NewQuote.class);
        query.setLimit(1);
        query.whereEqualTo("relatedJournal", currentJournalId);
        query.whereEqualTo("quoteSID", vqSID);
        query.fromPin();
        query.findInBackground(new FindCallback<NewQuote>() {
            @Override
            public void done(List<NewQuote> list, com.parse.ParseException e) {

                if (e == null) {

                    for (NewQuote quote : list) {

                        if (isAdded()) {

                            thisQuoteId = quote.getObjectId();
                            String lowEndString = getString(R.string.dollar_sign) + quote.getLowEnd();
                            String highEndString = getString(R.string.dollar_sign) + quote.getHighEnd();
                            String startEndTime = quote.getQuoteStartTime() + " - " + quote.getQuoteEndTime();
                            vqLowEnd.setText(lowEndString);
                            vqHighEnd.setText(highEndString);
                            vqTime.setText(startEndTime);
                            vqNotes.setText(quote.getQuoteNotes());

                            if (!TextUtils.isEmpty(vqNotes.getText())) {

                                tvQuoteNotesDisplay.setVisibility(View.VISIBLE);
                                vqNotes.setVisibility(View.VISIBLE);

                            }
                        }
                    }
                }
            }

        });
    }

}
