package com.garrisonthomas.junkapp.DialogFragments;

import android.app.DialogFragment;
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

import com.garrisonthomas.junkapp.ParseObjects.NewQuote;
import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.Utils;

public class AddQuoteDialogFragment extends DialogFragment {

    private static EditText etQuoteSSID, etLowEnd, etHighEnd, etQuoteNotes;
    private static Button saveQuote, cancelQuote;
    private static String currentJournalId;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.add_quote_layout, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        currentJournalId = preferences.getString("universalJournalId", "none");

        etQuoteSSID = (EditText) v.findViewById(R.id.et_quote_ssid);
        etLowEnd = (EditText) v.findViewById(R.id.et_low_end);
        etHighEnd = (EditText) v.findViewById(R.id.et_high_end);
        etQuoteNotes = (EditText) v.findViewById(R.id.et_quote_notes);

        saveQuote = (Button) v.findViewById(R.id.btn_save_quote);
        cancelQuote = (Button) v.findViewById(R.id.btn_cancel_quote);

        saveQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(etQuoteSSID.getText())
                        && (!TextUtils.isEmpty(etLowEnd.getText()))) {

                    Utils.hideKeyboard(v, getActivity());

                    NewQuote newQuote = new NewQuote();
                    newQuote.setRelatedJournal(currentJournalId);
                    newQuote.setQuoteSSID(Integer.valueOf(etQuoteSSID.getText().toString()));
                    newQuote.setLowEnd(Integer.valueOf(etLowEnd.getText().toString()));
                    if (!TextUtils.isEmpty(etHighEnd.getText())) {
                        newQuote.setHighEnd(Integer.valueOf(etHighEnd.getText().toString()));
                    } else {
                        newQuote.setHighEnd(0);
                    }
                    newQuote.setQuoteNotes(String.valueOf(etQuoteNotes.getText()));

                    newQuote.saveEventually();
                    newQuote.pinInBackground();

                    Toast.makeText(getActivity(), "Quote number " + etQuoteSSID.getText().toString() + " saved",
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Utils.showKeyboardInDialog(getDialog());
    }

}
