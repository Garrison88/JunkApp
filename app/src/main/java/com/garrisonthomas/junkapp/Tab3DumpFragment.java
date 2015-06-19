package com.garrisonthomas.junkapp;

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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class Tab3DumpFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    ImageButton btnPhone;
    Spinner dumpsSpinner;
    Button infoBtn, dirBtn, calcBtn, dumpsClearBtn;
    EditText weight;
    String[] directions, information;
    int[] rate;
    int rateNumber;
    TextView grossCostNumber, netCostNumber, grossCost, netCost;
    String dir, info, weightString, resultString, withTaxString;
    double weightNumber, result, withTax;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.tab3_dumps_layout, container, false);

        btnPhone = (ImageButton) v.findViewById(R.id.btn_phone);
        btnPhone.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel: 18007436348"));
                startActivity(i);
            }
        });

        dumpsSpinner = (Spinner) v.findViewById(R.id.spinner_dumps);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.dumps, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dumpsSpinner.setSelection(0);
        dumpsSpinner.setAdapter(adapter);
        dumpsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                infoBtn = (Button) getActivity().findViewById(R.id.btn_dump_info);
                dirBtn = (Button) getActivity().findViewById(R.id.btn_dump_directions);
                calcBtn = (Button) getActivity().findViewById(R.id.btn_calculate_dump);
                dumpsClearBtn = (Button) getActivity().findViewById(R.id.dumps_clear);
                dumpsClearBtn.setVisibility(View.INVISIBLE);

                weight = (EditText) getActivity().findViewById(R.id.et_enter_weight);

                directions = getActivity().getResources().getStringArray(R.array.dumps_address);
                information = getActivity().getResources().getStringArray(R.array.dumps_info);
                rate = getActivity().getResources().getIntArray(R.array.dumps_rate);

                grossCostNumber = (TextView) getActivity().findViewById(R.id.tv_gross_cost_number);
                netCostNumber = (TextView) getActivity().findViewById(R.id.tv_net_cost_number);
                grossCost = (TextView) getActivity().findViewById(R.id.tv_dump_gross_cost);
                netCost = (TextView) getActivity().findViewById(R.id.tv_dump_net_cost);

                grossCost.setVisibility(View.INVISIBLE);
                netCost.setVisibility(View.INVISIBLE);

                dir = directions[position];
                info = information[position];
                rateNumber = rate[position];

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
                                .setTitle("Dump Info")
                                .setMessage(info)
                                .setNeutralButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                    }
                                })
                                .setIcon(R.drawable.info)
                                .show();
                    }
                });

                calcBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        weightString = weight.getText().toString();
                        weightNumber = Double.parseDouble(weightString);

                        result = Math.round((weightNumber * rateNumber) * 100.00) / 100.00;
                        withTax = Math.round((result * 1.13) * 100.00) / 100.00;

                        grossCost.setVisibility(View.VISIBLE);
                        netCost.setVisibility(View.VISIBLE);
                        dumpsClearBtn.setVisibility(View.VISIBLE);

                        resultString = String.valueOf(result);
                        withTaxString = String.valueOf(withTax);

                        grossCostNumber.setText("$"+resultString);
                        netCostNumber.setText("$"+withTaxString);

                        weight.setText("");

                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                        }



                });

                dumpsClearBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        grossCostNumber.setText("");
                        netCostNumber.setText("");
                        grossCost.setVisibility(View.INVISIBLE);
                        netCost.setVisibility(View.INVISIBLE);

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("tab", "Tab3DumpFragment"); //save the tab selected
        super.onSaveInstanceState(outState);

    }

}