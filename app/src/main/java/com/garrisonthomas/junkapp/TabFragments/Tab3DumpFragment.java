package com.garrisonthomas.junkapp.tabfragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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

import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.Utils;

public class Tab3DumpFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private static Spinner dumpsSpinner;
    private static Button infoBtn, dirBtn, calcBtn, dumpsClearBtn, addHSTButton;
    private static EditText etWeight;
    private static String[] dumpNameArray, dumpDirectionsArray, dumpInfoArray, dumpPhoneNumberArray;
    private static int[] dumpRateArray;
    private static int selectedDumpRate;
    private static TextView tvDumpCost;
    private static String selectedDumpInfo, withTaxString, selectedDumpName;
    private static double weightNumber, result, withTax;

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

        etWeight = (EditText) v.findViewById(R.id.et_enter_weight);

        tvDumpCost = (TextView) v.findViewById(R.id.tv_dump_cost);

        dumpNameArray = v.getResources().getStringArray(R.array.dumps_name);
        dumpDirectionsArray = v.getResources().getStringArray(R.array.dumps_address);
        dumpInfoArray = v.getResources().getStringArray(R.array.dumps_info);
        dumpRateArray = v.getResources().getIntArray(R.array.dumps_rate);
        dumpPhoneNumberArray = v.getResources().getStringArray(R.array.dumps_phone_number);

        dumpsSpinner.setAdapter(new DumpsAdapter(getActivity(), R.layout.custom_spinner_layout, dumpNameArray));
        dumpsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                final String dir = getString(R.string.google_maps_url) + dumpDirectionsArray[position];
                selectedDumpInfo = dumpInfoArray[position];
                selectedDumpRate = dumpRateArray[position];
                selectedDumpName = dumpNameArray[position];

                dirBtn.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse(dir));
                            startActivity(intent);
                    }
                });

                infoBtn.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle(selectedDumpName)
                                    .setMessage(selectedDumpInfo)
                                    .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            Uri number = Uri.parse("tel:"+dumpPhoneNumberArray[position]);
                                            Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                                            startActivity(callIntent);

                                        }
                                    })
                                    .setNeutralButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // close dialog
                                        }
                                    })
                                    .show();

                    }
                });

                calcBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!TextUtils.isEmpty(etWeight.getText())) {

                            // close keyboard
                            View view = getActivity().getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }

                                weightNumber = Double.parseDouble(etWeight.getText().toString());
                                weightNumber = weightNumber / 1000;

                                result = Math.round((weightNumber * selectedDumpRate) * 100.00) / 100.00;
                                withTax = Utils.calculateTax(result);

                                String resultString = String.valueOf(result);
                                withTaxString = String.valueOf(withTax);

                                tvDumpCost.setText("$" + resultString);

                                etWeight.setText("");

                        } else {

                            Toast.makeText(getActivity(), "Enter weight", Toast.LENGTH_SHORT).show();

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

                            tvDumpCost.setText("");
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

            TextView mainText = (TextView) mySpinner.findViewById(R.id.spinner_text_dump_name);

            mainText.setText(dumpNameArray[position]);

            TextView subText = (TextView) mySpinner.findViewById(R.id.spinner_text_dump_rate);
            subText.setText("$" + dumpRateArray[position] + "/MT");

            return mySpinner;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putString("tab", "Tab3DumpFragment"); //save the tab selected

    }

}