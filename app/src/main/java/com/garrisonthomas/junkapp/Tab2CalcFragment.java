package com.garrisonthomas.junkapp;

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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class Tab2CalcFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    ImageButton btnPhone;
    Spinner vSpinner, bSpinner;
    Button addVolume, addBedload, deleteVolume, deleteBedload, calcCost, clearCost, addHST;
    TextView vSize, bSize, tvTotal;
    int[] volumePrice, bedloadPrice;
    String[] loadSize;
    int vPrice, bPrice, vCount = 0, bCount = 0;
    double beforeTax, sum;
    String doubleValue, totalText, sumString;

    ArrayList<Integer> priceArray = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.tab2_calc_layout, container, false);

        btnPhone = (ImageButton) v.findViewById(R.id.btn_phone);
        clearCost = (Button) v.findViewById(R.id.btn_clear_cost);
        addHST = (Button) v.findViewById(R.id.btn_add_hst);
        addVolume = (Button) v.findViewById(R.id.btn_add_volume);
        addBedload = (Button) v.findViewById(R.id.btn_add_bedload);
        deleteVolume = (Button) v.findViewById(R.id.btn_delete_volume);
        deleteBedload = (Button) v.findViewById(R.id.btn_delete_bedload);
        calcCost = (Button) v.findViewById(R.id.btn_calc_cost);

        vSpinner = (Spinner) v.findViewById(R.id.spinner_volume);
        bSpinner = (Spinner) v.findViewById(R.id.spinner_bedload);

        vSize = (TextView) v.findViewById(R.id.tv_display_volume);
        bSize = (TextView) v.findViewById(R.id.tv_display_bedload);
        tvTotal = (TextView) v.findViewById(R.id.load_total);

        volumePrice = v.getResources().getIntArray(R.array.string_volume_price);
        bedloadPrice = v.getResources().getIntArray(R.array.string_bedload_price);
        loadSize = v.getResources().getStringArray(R.array.string_load_name);

        btnPhone.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel: 18007436348"));
                startActivity(i);
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.string_load_name, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vSpinner.setAdapter(adapter);
        vSpinner.setSelection(0);
        vSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                vPrice = volumePrice[position];

                addVolume.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {

                        if (vSize.length() > 0) {
                            vSize.append(" + ");
                        }

                        vSize.append(loadSize[position] + " ($" + volumePrice[position] + ")");

                        priceArray.add(vPrice);
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.string_load_name, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bSpinner.setAdapter(adapter2);
        bSpinner.setSelection(0);
        bSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

               @Override
               public void onItemSelected(AdapterView<?> parent, View view, final int position,
                                          long id) {

                   bPrice = bedloadPrice[position];

                   addBedload.setOnClickListener(new View.OnClickListener() {

                       public void onClick(View v) {

                           if (bSize.length() > 0) {
                               bSize.append(" + ");
                           }

                           bSize.append(loadSize[position] +" ($"+bedloadPrice[position]+")");
                           priceArray.add(bPrice);

                       }
                   });

               }

               @Override
               public void onNothingSelected(AdapterView<?> parent) {

               }
           }

            );

        calcCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (vSize.length() != 0 || bSize.length() != 0) {

                    sum = 0;
                    for (int i : priceArray) {
                        sum += i;
                    }

                    sumString = String.valueOf(sum);
                    tvTotal.setText("$" + sumString);

                }
            }


        });

        clearCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                priceArray.clear();
                vSize.setText("");
                bSize.setText("");
                tvTotal.setText("");

            }
        });

        addHST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tvTotal.length() != 0) {

                    doubleValue = tvTotal.getText().toString();
                    beforeTax = Double.parseDouble(doubleValue.substring(1));

                    totalText = Double.toString(Math.round((beforeTax * 1.13) * 100.00) / 100.00);
                    tvTotal.setText("$" + totalText);

                }
            }
        });

            return v;
        }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("tab", "Tab2CalcFragment"); //save the tab selected
        super.onSaveInstanceState(outState);

    }

}