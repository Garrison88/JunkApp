package com.garrisonthomas.junkapp.DialogFragments;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.garrisonthomas.junkapp.R;

/**
 * Created by GarrisonThomas on 2015-11-10.
 */
public class PasswordCheckDialogFragment extends DialogFragment {

    SharedPreferences preferences;
    EditText password;
    Button passwordCancel, passwordOK;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Enter Password");
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.password_dialog_layout, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());

        password = (EditText) v.findViewById(R.id.password);

        passwordCancel = (Button) v.findViewById(R.id.password_cancel);
        passwordOK = (Button) v.findViewById(R.id.password_ok);

        passwordCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                System.exit(0);
            }
        });

        passwordOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().equals(getString(R.string.password))) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("firstrun", "entry").commit();
                    getDialog().dismiss();
                } else {
                    Toast.makeText(getActivity(), "Incorrect password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setCancelable(false);
        return v;

    }

}
