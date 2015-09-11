package com.garrisonthomas.junkapp;

import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.garrisonthomas.junkapp.DialogFragments.DailyJournalDialogFragment;
import com.parse.FindCallback;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Tab1DashFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Button calcDumps, calcTotal, btnClear, newJournal, viewJournal;
    EditText enterTotal, enterDump;
    TextView percentOfGoal, percentOfTotal;
    String percentOf, percentOfTotalString, todaysDate;
    ProgressBar dashProgressBar;
    double totalEarnings, totalDump;

    FragmentManager manager;
    DailyJournalDialogFragment djFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.tab1_dash_layout, container, false);

        Date date = new Date();
        SimpleDateFormat df2 = new SimpleDateFormat("EEE, dd MMM yyyy");

        todaysDate = df2.format(date);

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
                    percentOf = String.valueOf(Math.round((totalEarnings / 1400) * 100));
                    percentOfGoal.setText(percentOf + "%");

                }
            }
        });

        calcDumps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(enterDump.getText())) {

                    totalEarnings = Integer.parseInt(enterTotal.getText().toString());
                    totalDump = Integer.parseInt(enterDump.getText().toString());
                    percentOfTotalString = String.valueOf(Math.round((totalDump / totalEarnings) * 100.0));
                    percentOfTotal.setText(percentOfTotalString + "%");

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

                djFragment.show(manager, "Dialog");

            }
        });

        final View.OnClickListener clickListener = new View.OnClickListener() {
            public void onClick(View v) {

                djFragment.show(manager, "Dialog");

            }
        };

        viewJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dashProgressBar.setVisibility(View.VISIBLE);

                ParseQuery<DailyJournal> query = ParseQuery.getQuery("DailyJournal");
                query.whereEqualTo("date", todaysDate);
                query.orderByAscending("createdAt");
                query.setLimit(1);
                query.findInBackground(new FindCallback<DailyJournal>() {
                    @Override
                    public void done(List<DailyJournal> list, com.parse.ParseException e) {

                        if (e == null) {

                            for (DailyJournal newJournal : list) {

                                Intent intent = new Intent(getActivity(), CurrentJournal.class);
                                intent.putExtra("EXTRA_DATE", todaysDate);
                                intent.putExtra("EXTRA_CREW", newJournal.getCrew());
                                intent.putExtra("EXTRA_TRUCK_NUMBER", newJournal.getTruckNumber());
                                intent.putExtra("EXTRA_DJ_ID", newJournal.getObjectId());
                                startActivity(intent);

                            }

                            dashProgressBar.setVisibility(View.GONE);

                            if (list.size() == 0) {

                                Snackbar
                                        .make(coordinatorLayoutView, "No journal available", Snackbar.LENGTH_LONG)
                                        .setActionTextColor(Color.YELLOW).setAction("CREATE", clickListener)
                                        .show();

                            }

                        }
                    }
                });

            }
        });

        return v;

    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("tab", "Tab1DashFragment"); //save the tab selected
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onResume() {
        super.onResume();
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}