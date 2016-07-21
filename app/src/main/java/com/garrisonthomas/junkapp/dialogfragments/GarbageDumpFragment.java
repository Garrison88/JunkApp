package com.garrisonthomas.junkapp.dialogfragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.parseobjects.NewDump;

public class GarbageDumpFragment extends Fragment {

    private static EditText etAddDumpWeight, etDumpReceiptNumber, etPercentPrevious;
    private static TextView tvGrossCost;
    private static Button saveDump, cancelDump;
    private static Spinner dumpNameSpinner;
    private static String[] dumpNameArray;
    private static int[] dumpRateArray;
    private static int pricePerTonne;
    private static double result, weightInTonnes;
    private static String dumpNameString, currentJournalId;
    private static LinearLayout dumpCostLayout;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.add_garbage_dump_layout, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        currentJournalId = preferences.getString("universalJournalId", "none");

        etAddDumpWeight = (EditText) v.findViewById(R.id.et_add_dump_weight);
        etDumpReceiptNumber = (EditText) v.findViewById(R.id.et_dump_receipt_number);
        etPercentPrevious = (EditText) v.findViewById(R.id.et_percent_previous);

        tvGrossCost = (TextView) v.findViewById(R.id.tv_dump_gross_cost);

        saveDump = (Button) v.findViewById(R.id.btn_save_dump);
        cancelDump = (Button) v.findViewById(R.id.btn_cancel_dump);

        dumpNameArray = getResources().getStringArray(R.array.dumps_name);
        dumpRateArray = getResources().getIntArray(R.array.dumps_rate);

        dumpNameSpinner = (Spinner) v.findViewById(R.id.spinner_dump_dialog);

        dumpCostLayout = (LinearLayout) v.findViewById(R.id.dump_cost_layout);


        dumpNameSpinner.setAdapter(new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_dropdown_item_1line, dumpNameArray));

        dumpNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                dumpNameString = dumpNameArray[position];
                pricePerTonne = dumpRateArray[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        etAddDumpWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {

                if (!TextUtils.isEmpty(etAddDumpWeight.getText())) {

                    weightInTonnes = Double.parseDouble(String.valueOf(etAddDumpWeight.getText()));

                    switch (dumpNameSpinner.getSelectedItemPosition()) {
//                        Cherry
                        case 1:
//                        GFL Pickering
                        case 7:
//                        GFL Etobicoke
                        case 8:
//                        GFL Mississauga
                        case 9:
                            if (weightInTonnes <= .48) {
                                result = 40;
                            } else {
                                result = Math.round((GarbageDumpFragment.weightInTonnes * pricePerTonne) * 100.00) / 100.00;
                            }
                            break;
//                        Shorncliffe
                        case 2:
                            if (weightInTonnes <= .38) {
                                result = 25;
                            } else {
                                result = Math.round((GarbageDumpFragment.weightInTonnes * pricePerTonne) * 100.00) / 100.00;
                            }
                            break;
//                        Fenmar
                        case 3:
                            if (weightInTonnes <= .52) {
                                result = 40;
                            } else {
                                result = Math.round((GarbageDumpFragment.weightInTonnes * pricePerTonne) * 100.00) / 100.00;
                            }
                            break;
//                        Tor-Can
                        case 14:
                            if (weightInTonnes <= .82) {
                                result = 65;
                            } else {
                                result = Math.round((GarbageDumpFragment.weightInTonnes * pricePerTonne) * 100.00) / 100.00;
                            }
                            break;
                        default:
                            result = Math.round((GarbageDumpFragment.weightInTonnes * pricePerTonne) * 100.00) / 100.00;
                            break;
                    }

                    String resultString = getString(R.string.dollar_sign) + String.valueOf(result);

                    dumpCostLayout.setVisibility(View.VISIBLE);
                    tvGrossCost.setText(resultString);


                } else if (TextUtils.isEmpty(etAddDumpWeight.getText())) {

                    dumpCostLayout.setVisibility(View.GONE);

                }
            }

        });

        saveDump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(etAddDumpWeight.getText())
                        && (!TextUtils.isEmpty(etDumpReceiptNumber.getText()))) {

                    NewDump newDump = new NewDump();
                    newDump.setRelatedJournal(currentJournalId);
                    newDump.setDumpName(dumpNameString);
                    newDump.setGrossCost(result);
                    newDump.setTonnage(weightInTonnes);
                    newDump.setDumpReceiptNumber(Integer.valueOf(etDumpReceiptNumber.getText().toString()));
                    if (!TextUtils.isEmpty(etPercentPrevious.getText())) {
                        newDump.setPercentPrevious(Integer.valueOf(etPercentPrevious.getText().toString()));
                    } else {
                        newDump.setPercentPrevious(0);
                    }

                    newDump.pinInBackground();
                    newDump.saveEventually();

                    Toast.makeText(getActivity(), "Dump at " + dumpNameString + " saved", Toast.LENGTH_SHORT).show();

                    getActivity().finish();

                } else {

                    Toast.makeText(getActivity(), "Please fill all required fields", Toast.LENGTH_SHORT).show();

                }
            }
        });

        cancelDump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

//        setCancelable(false);
        return v;

    }


}
