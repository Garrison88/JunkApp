package com.garrisonthomas.junkapp.tabfragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.garrisonthomas.junkapp.CurrentJournal;
import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.dialogfragments.DailyJournalDialogFragment;
import com.google.firebase.auth.FirebaseAuth;

public class Tab1DashFragment extends Fragment {

    private static Button openJournal;
    private String firebaseJournalRef;
    private SharedPreferences preferences;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public Tab1DashFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FragmentManager manager;
    DailyJournalDialogFragment djFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.tab1_journal_layout, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        firebaseJournalRef = preferences.getString("firebaseURL", "none");

        openJournal = (Button) v.findViewById(R.id.btn_open_journal);

        manager = getActivity().getSupportFragmentManager();
        djFragment = new DailyJournalDialogFragment();

        final View coordinatorLayoutView = v.findViewById(R.id.snackbar_create_journal);

        //handle snackbar clicks for create journal
        final View.OnClickListener createJournalClickListener = new View.OnClickListener() {
            public void onClick(View v) {

                djFragment.show(manager, "Dialog");

            }
        };

        openJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (firebaseJournalRef.equals("none") && auth.getCurrentUser() != null) {

                    Snackbar
                            .make(coordinatorLayoutView, "No journal available", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.CYAN)
                            .setAction("CREATE", createJournalClickListener)
                            .show();
                } else if (!firebaseJournalRef.equals("none") && auth.getCurrentUser() != null) {

                    Intent intent = new Intent(getActivity(), CurrentJournal.class);
                    startActivity(intent);

                } else if (auth.getCurrentUser() == null) {

                    Toast.makeText(getActivity(), "Please login first", Toast.LENGTH_LONG).show();
                }

            }
        });

        return v;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putString("tab", "Tab1DashFragment"); //save the tab selected

    }

}