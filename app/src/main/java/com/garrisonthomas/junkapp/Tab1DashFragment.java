package com.garrisonthomas.junkapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Tab1DashFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Button calcDumps, calcTotal, btnClear, newJournal;
    EditText enterTotal, enterDump;
    TextView percentOfGoal, percentOfTotal;
    String percentOf, percentOfTotalString;
    double totalEarnings, totalDump;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.tab1_dash_layout, container, false);

        calcTotal = (Button) v.findViewById(R.id.calculate_percentage);
        calcDumps = (Button) v.findViewById(R.id.calculate_dump_percentage);
        btnClear = (Button) v.findViewById(R.id.btn_dash_clear);
        newJournal = (Button) v.findViewById(R.id.new_journal);

        enterDump = (EditText) v.findViewById(R.id.et_enter_dump_cost);
        enterTotal = (EditText) v.findViewById(R.id.et_enter_total);

        percentOfTotal = (TextView) v.findViewById(R.id.tv_percent_of_total);
        percentOfGoal = (TextView) v.findViewById(R.id.tv_percent_of_goal);

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
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

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

                Intent intent = new Intent(getActivity(), DailyJournal.class);
                startActivity(intent);
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
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}
