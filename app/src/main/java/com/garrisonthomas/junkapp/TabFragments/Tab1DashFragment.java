package com.garrisonthomas.junkapp.tabfragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.garrisonthomas.junkapp.CurrentJournal;
import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.Utils;
import com.garrisonthomas.junkapp.dialogfragments.DailyJournalDialogFragment;

public class Tab1DashFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private static Button calcDumps, calcTotal, btnClear, openJournal;
    private static EditText enterTotal, enterDump;
    private static TextView percentOfGoal, percentOfTotal;
    private String percentOf, percentOfTotalString, currentJournalId;
    private static double totalEarnings, totalDump;
    private SharedPreferences preferences;

    FragmentManager manager;
    DailyJournalDialogFragment djFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.tab1_dash_layout, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        currentJournalId = preferences.getString("universalJournalId", "none");

        calcTotal = (Button) v.findViewById(R.id.calculate_percentage);
        calcDumps = (Button) v.findViewById(R.id.calculate_dump_percentage);
        btnClear = (Button) v.findViewById(R.id.btn_dash_clear);
        openJournal = (Button) v.findViewById(R.id.btn_open_journal);

        enterDump = (EditText) v.findViewById(R.id.et_enter_dump_cost);
        enterTotal = (EditText) v.findViewById(R.id.et_enter_total);

        percentOfTotal = (TextView) v.findViewById(R.id.tv_percent_of_total);
        percentOfGoal = (TextView) v.findViewById(R.id.tv_percent_of_goal);

        manager = getActivity().getSupportFragmentManager();
        djFragment = new DailyJournalDialogFragment();

        final View coordinatorLayoutView = v.findViewById(R.id.snackbar_create_journal);

        calcTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(enterTotal.getText())) {

                    totalEarnings = Integer.parseInt(enterTotal.getText().toString());
                    percentOf = String.valueOf(Math.round((totalEarnings / 1400) * 100)) + getString(R.string.percent_sign);
                    percentOfGoal.setText(percentOf);

                }
            }
        });

        calcDumps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(enterDump.getText())) {

                    totalEarnings = Integer.parseInt(enterTotal.getText().toString());
                    totalDump = Integer.parseInt(enterDump.getText().toString());
                    percentOfTotalString = getString(R.string.dollar_sign) + String.valueOf(Math.round((totalDump / totalEarnings) * 100.0));
                    percentOfTotal.setText(percentOfTotalString);

                    Utils.hideKeyboard(v, getActivity());

                } else {

                    Toast.makeText(getActivity(), "Please calculate total first", Toast.LENGTH_SHORT).show();

                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterTotal.setText("");
                enterDump.setText("");
                percentOfGoal.setText("");
                percentOfTotal.setText("");
            }
        });

        //handle snackbar clicks
        final View.OnClickListener createJournalClickListener = new View.OnClickListener() {
            public void onClick(View v) {

                djFragment.show(manager, "Dialog");

            }
        };

        openJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentJournalId.equals("none")) {

                    Snackbar
                            .make(coordinatorLayoutView, "No journal available", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.CYAN)
                            .setAction("CREATE", createJournalClickListener)
                            .show();
                } else {

                    Intent intent = new Intent(getActivity(), CurrentJournal.class);
                    startActivity(intent);

                }
            }
        });

        return v;

    }

    @Override
    public void onStop() {
        super.onStop();
        currentJournalId = preferences.getString("universalJournalId", "none");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (preferences.getBoolean("firstrun", true)) {
            preferences.edit().putString("universalJournalId", "none");
            preferences.edit().putBoolean("firstrun", false).apply();
        }
        currentJournalId = preferences.getString("universalJournalId", "none");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putString("tab", "Tab1DashFragment"); //save the tab selected

    }

}