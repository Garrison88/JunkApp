package com.garrisonthomas.junkapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

public class Tab3DumpFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Spinner dumpsSpinner;
    Button infoBtn, dirBtn, calcBtn, dumpsClearBtn, addHSTButton;
    EditText weight;
    String[] dumpName, directions, information;
    int[] rate;
    int rateNumber;
    TextView tvDumpCost;
    String dir, info, resultString, withTaxString;
    double weightNumber, result, withTax;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.tab3_dumps_layout, container, false);

        infoBtn = (Button) v.findViewById(R.id.btn_dump_info);
        dirBtn = (Button) v.findViewById(R.id.btn_dump_directions);
        calcBtn = (Button) v.findViewById(R.id.btn_calculate_dump);
        addHSTButton = (Button) v.findViewById(R.id.btn_dumps_add_hst);
        dumpsClearBtn = (Button) v.findViewById(R.id.dumps_clear);

        dumpsSpinner = (Spinner) v.findViewById(R.id.spinner_dumps);

        weight = (EditText) v.findViewById(R.id.et_enter_weight);

        tvDumpCost = (TextView) v.findViewById(R.id.tv_dump_cost);

        dumpName = v.getResources().getStringArray(R.array.dumps_name);
        directions = v.getResources().getStringArray(R.array.dumps_address);
        information = v.getResources().getStringArray(R.array.dumps_info);
        rate = v.getResources().getIntArray(R.array.dumps_rate);

        dumpsSpinner.setAdapter(new DumpsAdapter(getActivity(), R.layout.custom_bedload_spinner, dumpName));
        dumpsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                dir = directions[position];
                info = information[position];
                rateNumber = rate[position];

                dirBtn.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        if (position != 0) {
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse(dir));
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "Select a dump", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                infoBtn.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        if (position != 0) {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Dump Info")
                                    .setMessage(info)
                                    .setNeutralButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                        }
                                    })
                                    .setIcon(R.drawable.ic_dashboard_white_24dp)
                                    .show();
                        } else {
                            Toast.makeText(getActivity(), "Select a dump", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                calcBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!TextUtils.isEmpty(weight.getText())) {

                            weightNumber = Double.parseDouble(weight.getText().toString());
                            weightNumber = weightNumber / 1000;

                            result = Math.round((weightNumber * rateNumber) * 100.00) / 100.00;
                            withTax = Math.round((result * 1.13) * 100.00) / 100.00;

                            resultString = String.valueOf(result);
                            withTaxString = String.valueOf(withTax);

                            tvDumpCost.setText("$" + resultString);

                            weight.setText("");

                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                        }
                    }


                });

                addHSTButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!TextUtils.isEmpty(tvDumpCost.getText())) {
                            tvDumpCost.setText("$" + withTaxString);
                        }
                    }
                });

                dumpsClearBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!TextUtils.isEmpty(tvDumpCost.getText())) {

                            tvDumpCost.setText("$0.0");
                            withTaxString = "";

                        }
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;

    }

    public class DumpsAdapter extends ArrayAdapter<String> {

        public DumpsAdapter(Context ctx, int txtViewResourceId, String[] objects) {
            super(ctx, txtViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
            return getCustomView(position, cnvtView, prnt);
        }

        @Override
        public View getView(int pos, View cnvtView, ViewGroup prnt) {
            return getCustomView(pos, cnvtView, prnt);
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.custom_dumps_spinner, parent,
                    false);

            TextView main_text = (TextView) mySpinner.findViewById(R.id.spinner_text_dump_name);

            main_text.setText(dumpName[position]);

            TextView subSpinner = (TextView) mySpinner.findViewById(R.id.spinner_text_dump_rate);
            if (position == 0) {
                subSpinner.setVisibility(View.GONE);
            }
            subSpinner.setText("$" + rate[position] + "/tonne");

//            ImageView left_icon = (ImageView) mySpinner.findViewById(R.id.left_pic);
//            left_icon.setImageResource(R.drawable.dump_truck);

            return mySpinner;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("tab", "Tab3DumpFragment"); //save the tab selected
        super.onSaveInstanceState(outState);

    }

}