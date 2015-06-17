package com.garrisonthomas.junkapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Android on 5/28/2015.
 */
public class Tab1DashFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("tab", "Tab1DashFragment"); //save the tab selected
        super.onSaveInstanceState(outState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.tab1_dash_layout, container, false);

        ImageButton btnPhone = (ImageButton) v.findViewById(R.id.btn_phone);
        btnPhone.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel: 18007436348"));
                startActivity(i);
            }
        });

        Button email = (Button) v.findViewById(R.id.office_email);
        email.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "rcrawford@ridofittoronto.com", null));
                startActivity(Intent.createChooser(i, "Choose an Email client :"));
            }
        });

        ImageButton officeDirections = (ImageButton) v.findViewById(R.id.office_map_directions);
        officeDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.ca/search?q=2333+dundas+street+west&oq=2333+dundas&aqs=chrome.0.0j69i57j0l4.2407j0j7&sourceid=chrome&es_sm=122&ie=UTF-8"));
                startActivity(intent);

            }
        });

        Button calcTotal = (Button) v.findViewById(R.id.calculate_percentage);
        final EditText enterTotal = (EditText) v.findViewById(R.id.et_enter_total);
        final TextView percentOfGoal = (TextView) v.findViewById(R.id.tv_percent_of_goal);
        calcTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double totalEarnings = Integer.parseInt(enterTotal.getText().toString());
                String percentOf = String.valueOf(Math.round((totalEarnings / 1400) * 100));
                percentOfGoal.setText (percentOf+"%");

            }
        });

        Button calcDumps = (Button) v.findViewById(R.id.calculate_dump_percentage);
        final EditText enterDump = (EditText) v.findViewById(R.id.et_enter_dump_cost);
        final TextView percentOfTotal = (TextView) v.findViewById(R.id.tv_percent_of_total);

        calcDumps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double totalEarnings = Integer.parseInt(enterTotal.getText().toString());
                double totalDump = Integer.parseInt(enterDump.getText().toString());
                String percentOfT = String.valueOf(Math.round((totalDump / totalEarnings) * 100.0));
                percentOfTotal.setText(percentOfT+"%");

            }
        });

        return v;



    }

}
