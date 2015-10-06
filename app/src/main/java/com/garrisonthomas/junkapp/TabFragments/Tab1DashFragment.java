package com.garrisonthomas.junkapp.TabFragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.garrisonthomas.junkapp.CurrentJournal;
import com.garrisonthomas.junkapp.DialogFragments.DailyJournalDialogFragment;
import com.garrisonthomas.junkapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Tab1DashFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private static Button calcDumps, calcTotal, btnClear, newJournal, viewJournal;
    private static EditText enterTotal, enterDump;
    private static TextView percentOfGoal, percentOfTotal;
    private static String percentOf, percentOfTotalString, todaysDate, currentJournalId;
    private static ProgressBar dashProgressBar;
    private static double totalEarnings, totalDump;
    private SharedPreferences preferences;

    FragmentManager manager;
    DailyJournalDialogFragment djFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.tab1_dash_layout, container, false);

        Date date = new Date();
        SimpleDateFormat df2 = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.CANADA);
        todaysDate = df2.format(date);

        preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());

        calcTotal = (Button) v.findViewById(R.id.calculate_percentage);
        calcDumps = (Button) v.findViewById(R.id.calculate_dump_percentage);
        btnClear = (Button) v.findViewById(R.id.btn_dash_clear);
        newJournal = (Button) v.findViewById(R.id.btn_new_journal);
        viewJournal = (Button) v.findViewById(R.id.btn_view_journal);

        enterDump = (EditText) v.findViewById(R.id.et_enter_dump_cost);
        enterTotal = (EditText) v.findViewById(R.id.et_enter_total);

        percentOfTotal = (TextView) v.findViewById(R.id.tv_percent_of_total);
        percentOfGoal = (TextView) v.findViewById(R.id.tv_percent_of_goal);

        dashProgressBar = (ProgressBar) v.findViewById(R.id.dashboard_progress_bar);
        dashProgressBar.setVisibility(View.GONE);

        manager = getFragmentManager();
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

                    final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

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

        newJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentJournalId.equals("none")) {

                    djFragment.show(manager, "Dialog");

                } else {

                    Toast.makeText(getActivity(), "Journal already exists", Toast.LENGTH_SHORT).show();

                }
            }
        });

        //handle snackbar clicks
        final View.OnClickListener createJournalClickListener = new View.OnClickListener() {
            public void onClick(View v) {

                djFragment.show(manager, "Dialog");

            }
        };

        viewJournal.setOnClickListener(new View.OnClickListener() {
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
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("tab", "Tab1DashFragment"); //save the tab selected
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onResume() {
        super.onResume();

        currentJournalId = preferences.getString("universalJournalId", "none");

        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}