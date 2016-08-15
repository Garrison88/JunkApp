package com.garrisonthomas.junkapp.dialogfragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.garrisonthomas.junkapp.DialogFragmentHelper;
import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.Utils;
import com.garrisonthomas.junkapp.entryobjects.QuoteObject;

public class AddQuoteDialogFragment extends DialogFragmentHelper {

    private static EditText etQuoteSID, etLowEnd, etHighEnd, etQuoteNotes;
    private static Button saveQuote, cancelQuote, startTime, endTime;
    private static String firebaseJournalRef;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.add_quote_layout, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        firebaseJournalRef = preferences.getString("firebaseRef", "none");

        etQuoteSID = (EditText) v.findViewById(R.id.et_quote_sid);
        etLowEnd = (EditText) v.findViewById(R.id.et_low_end);
        etHighEnd = (EditText) v.findViewById(R.id.et_high_end);
        etQuoteNotes = (EditText) v.findViewById(R.id.et_quote_notes);

        startTime = (Button) v.findViewById(R.id.quote_start_time);
        endTime = (Button) v.findViewById(R.id.quote_end_time);
        saveQuote = (Button) v.findViewById(R.id.btn_save_quote);
        cancelQuote = (Button) v.findViewById(R.id.btn_cancel_quote);

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.chooseTime(getActivity(), startTime);
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.chooseTime(getActivity(), endTime);
            }
        });

        saveQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(etQuoteSID.getText())
                        && (!TextUtils.isEmpty(etLowEnd.getText()))) {

                    Firebase fbrQuote = new Firebase(firebaseJournalRef + "quotes/" + String.valueOf(etQuoteSID.getText()));

                    QuoteObject quote = new QuoteObject();
                    quote.setQuoteSID(Integer.valueOf(etQuoteSID.getText().toString()));
                    quote.setQuoteStartTime(String.valueOf(startTime.getText()));
                    quote.setQuoteEndTime(String.valueOf(endTime.getText()));
                    quote.setLowEnd(Double.valueOf(etLowEnd.getText().toString()));
                    if (!TextUtils.isEmpty(etHighEnd.getText())) {
                        quote.setHighEnd(Double.valueOf(etHighEnd.getText().toString()));
                    } else {
                        quote.setHighEnd(0);
                    }
                    if (!TextUtils.isEmpty(etQuoteNotes.getText())){
                        quote.setQuoteNotes(String.valueOf(etQuoteNotes.getText()));
                    }

                    fbrQuote.setValue(quote);

                    Toast.makeText(getActivity(), "Quote number " + etQuoteSID.getText().toString() + " saved",
                            Toast.LENGTH_SHORT).show();

                    dismiss();

                } else {

                    Toast.makeText(getActivity(), "Please fill all required fields", Toast.LENGTH_SHORT).show();

                }
            }
        });

        cancelQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setCancelable(false);
        return v;

    }

}
