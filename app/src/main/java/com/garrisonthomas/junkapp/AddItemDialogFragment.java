package com.garrisonthomas.junkapp;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;

/**
 * Created by Garrison on 2016-06-11.
 */
public class AddItemDialogFragment extends DialogFragment {

    public Button save;
    public SharedPreferences sharedPreferences;
    public String currentJournalID;

    public void cancelFragment(Button cancelButton) {

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

}
