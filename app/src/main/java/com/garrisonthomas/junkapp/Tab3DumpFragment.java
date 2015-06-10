package com.garrisonthomas.junkapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Android on 5/28/2015.
 */
public class Tab3DumpFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.tab3_dumps_layout, container, false);

        ImageButton btnPhone = (ImageButton) v.findViewById(R.id.btn_phone);
        btnPhone.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel: 18007436348"));
                startActivity(i);
            }
        });

        final Spinner dumpsSpinner;
        dumpsSpinner = (Spinner) v.findViewById(R.id.spinner_dumps);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.dumps, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dumpsSpinner.setAdapter(adapter);
        dumpsSpinner.setSelection(0);
        dumpsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Button infoBtn = (Button) getActivity().findViewById(R.id.btn_dump_info);
                Button dirBtn = (Button) getActivity().findViewById(R.id.btn_dump_directions);

                String[] directions = getActivity().getResources().getStringArray(R.array.dumps_address);
                final String dir = directions[position];

                dirBtn.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse(dir));
                        startActivity(intent);
                    }
                });

                String[] information = getActivity().getResources().getStringArray(R.array.dumps_info);
                final String info = information[position];

                infoBtn.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Dump Info")
                                .setMessage(info)
                                .setNeutralButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;

    }

}