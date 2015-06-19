package com.garrisonthomas.junkapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class Tab1DashFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    ImageButton btnPhone, officeDirections;
    Button btnEmail, calcDumps, calcTotal, btnClear;
    EditText enterTotal, enterDump;
    TextView percentOfGoal, percentOfTotal;
    String percentOf, percentOfTotalString;
    double totalEarnings, totalDump;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.tab1_dash_layout, container, false);

        btnPhone = (ImageButton) v.findViewById(R.id.btn_phone);
        btnPhone.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel: 18007436348"));
                startActivity(i);
            }
        });

        btnEmail = (Button) v.findViewById(R.id.office_email);
        btnEmail.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "rcrawford@ridofittoronto.com", null));
                startActivity(Intent.createChooser(i, "Choose an Email client :"));
            }
        });

        officeDirections = (ImageButton) v.findViewById(R.id.office_map_directions);
        officeDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.ca/search?q=2333+dundas+street+west&oq=2333+dundas&aqs=chrome.0.0j69i57j0l4.2407j0j7&sourceid=chrome&es_sm=122&ie=UTF-8"));
                startActivity(intent);

            }
        });

        calcDumps = (Button) v.findViewById(R.id.calculate_dump_percentage);
        calcDumps.setEnabled(false);
        calcTotal = (Button) v.findViewById(R.id.calculate_percentage);
        enterTotal = (EditText) v.findViewById(R.id.et_enter_total);
        percentOfGoal = (TextView) v.findViewById(R.id.tv_percent_of_goal);
        calcTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                totalEarnings = Integer.parseInt(enterTotal.getText().toString());
                percentOf = String.valueOf(Math.round((totalEarnings / 1400) * 100));
                percentOfGoal.setText(percentOf + "%");

                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                calcDumps.setEnabled(true);

            }
        });


        enterDump = (EditText) v.findViewById(R.id.et_enter_dump_cost);
        percentOfTotal = (TextView) v.findViewById(R.id.tv_percent_of_total);

        calcDumps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                totalEarnings = Integer.parseInt(enterTotal.getText().toString());
                totalDump = Integer.parseInt(enterDump.getText().toString());
                percentOfTotalString = String.valueOf(Math.round((totalDump / totalEarnings) * 100.0));
                percentOfTotal.setText(percentOfTotalString + "%");

                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

            }
        });

        btnClear = (Button) v.findViewById(R.id.btn_dash_clear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterTotal.setText("");
                enterDump.setText("");
                percentOfGoal.setText("");
                percentOfTotal.setText("");
                calcDumps.setEnabled(false);
            }
        });

        return v;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("tab", "Tab1DashFragment"); //save the tab selected
        super.onSaveInstanceState(outState);

    }
}
