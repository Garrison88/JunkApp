package com.garrisonthomas.junkapp.tabfragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.garrisonthomas.junkapp.BaseActivity;
import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.Utils;

public class Tab3DumpFragment extends Fragment {

    private static Spinner dumpsSpinner;
    private static Button infoBtn, dirBtn, calcBtn, dumpsClearBtn;
    private static String[] dumpNameArray, dumpAddressArray, dumpInfoArray, dumpPhoneNumberArray;
    private static Integer[] dumpRateArray;
    private static int selectedDumpRate;
    private static EditText etDumpCost;
    private static TextView tvDumpCost;
    private static String selectedDumpInfo, selectedDumpName;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.tab3_dumps_layout, container, false);

        infoBtn = (Button) v.findViewById(R.id.btn_dump_info);
        dirBtn = (Button) v.findViewById(R.id.btn_dump_directions);
        calcBtn = (Button) v.findViewById(R.id.btn_calculate_dump);
        dumpsClearBtn = (Button) v.findViewById(R.id.dumps_clear);

        dumpsSpinner = (Spinner) v.findViewById(R.id.spinner_dumps);

        etDumpCost = (EditText) v.findViewById(R.id.et_dump_cost);
        tvDumpCost = (TextView) v.findViewById(R.id.tv_dump_cost);

        dumpNameArray = BaseActivity.dumpNameArray;
        dumpAddressArray = BaseActivity.dumpAddressArray;
        dumpInfoArray = BaseActivity.dumpInfoArray;
        dumpRateArray = BaseActivity.dumpRateArray;
        dumpPhoneNumberArray = BaseActivity.dumpPhoneNumberArray;

        dumpsSpinner.setAdapter(new DumpsAdapter(getActivity(), R.layout.custom_spinner_layout, dumpNameArray));
        dumpsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                final String dir = getString(R.string.google_maps_url) + dumpAddressArray[position];
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

                                        Uri number = Uri.parse("tel:" + dumpPhoneNumberArray[position]);
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

                        if (!String.valueOf(etDumpCost.getText()).equals("")) {
                            // close keyboard
                            View view = getActivity().getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }

                            float weightInTonnes = Float.valueOf(etDumpCost.getText().toString());

                            double result = Utils.calculateDump(selectedDumpRate, weightInTonnes, dumpsSpinner);

                            String resultString = "$" + String.valueOf(result);

                            etDumpCost.setVisibility(View.GONE);
                            tvDumpCost.setVisibility(View.VISIBLE);
                            tvDumpCost.setText(resultString);

                            calcBtn.setClickable(false);
                        }
                    }


                });

                dumpsClearBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        tvDumpCost.setVisibility(View.GONE);
                        etDumpCost.setVisibility(View.VISIBLE);
                        etDumpCost.setText("");
                        calcBtn.setClickable(true);


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