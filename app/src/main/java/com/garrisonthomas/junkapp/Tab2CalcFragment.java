package com.garrisonthomas.junkapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class Tab2CalcFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Spinner vSpinner, bSpinner;
    Button calcCost, clearCost, addHST;
    TextView vSize, bSize, tvTotal;
    int[] volumePrice, bedloadPrice;
    String[] volumeSize, bedloadSize;
    int vPrice, bPrice, vCount = 0, bCount = 0;
    double beforeTax, sum;
    String doubleValue, totalText, sumString;

    ArrayList<Integer> priceArray = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab2_calc_layout, container, false);

        clearCost = (Button) v.findViewById(R.id.btn_clear_cost);

        addHST = (Button) v.findViewById(R.id.btn_add_hst);
        calcCost = (Button) v.findViewById(R.id.btn_calc_cost);

        vSpinner = (Spinner) v.findViewById(R.id.spinner_volume);
        bSpinner = (Spinner) v.findViewById(R.id.spinner_bedload);

        vSize = (TextView) v.findViewById(R.id.tv_display_volume);
        bSize = (TextView) v.findViewById(R.id.tv_display_bedload);
        tvTotal = (TextView) v.findViewById(R.id.load_total);

        volumePrice = v.getResources().getIntArray(R.array.string_volume_price);
        bedloadPrice = v.getResources().getIntArray(R.array.string_bedload_price);
        volumeSize = v.getResources().getStringArray(R.array.string_volume_name);
        bedloadSize = v.getResources().getStringArray(R.array.string_bedload_name);

//        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(), , R.layout.custom_volume_spinner);
//        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vSpinner.setAdapter(new VolumeAdapter(getActivity(), R.layout.custom_volume_spinner, volumeSize));
//        vSpinner.setAdapter(adapter1);
        vSpinner.setSelection(0);
        vSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                vPrice = volumePrice[position];

                if (vSize.length() > 0) {
                    vSize.append(" + ");
                }

                vSize.append(volumeSize[position] + " ($" + volumePrice[position] + ")");

                priceArray.add(vPrice);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

//        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.string_bedload_name, R.layout.custom_volume_spinner);
//        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bSpinner.setAdapter(new BedloadAdapter(getActivity(), R.layout.custom_bedload_spinner, bedloadSize));
//        bSpinner.setAdapter(adapter2);
        bSpinner.setSelection(0);
        bSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position,
                                       long id) {

                bPrice = bedloadPrice[position];

                if (bSize.length() > 0) {
                    bSize.append(" + ");
                }

                bSize.append(bedloadSize[position] + " ($" + bedloadPrice[position] + ")");
                priceArray.add(bPrice);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

    public class VolumeAdapter extends ArrayAdapter<String> {

        public VolumeAdapter(Context ctx, int txtViewResourceId, String[] objects) {
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
            View mySpinner = inflater.inflate(R.layout.custom_volume_spinner, parent,
                    false);
            TextView main_text = (TextView) mySpinner.findViewById(R.id.spinner_text_volume_name);
            main_text.setText(volumeSize[position]);

            TextView subSpinner = (TextView) mySpinner.findViewById(R.id.spinner_text_volume_price);
            subSpinner.setText("$"+volumePrice[position]);

//            ImageView left_icon = (ImageView) mySpinner.findViewById(R.id.left_pic);
//            left_icon.setImageResource(R.drawable.dump_truck);

            return mySpinner;
        }
    }

    public class BedloadAdapter extends ArrayAdapter<String> {

        public BedloadAdapter(Context ctx, int txtViewResourceId, String[] objects) {
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
            View mySpinner = inflater.inflate(R.layout.custom_bedload_spinner, parent,
                    false);
            TextView main_text = (TextView) mySpinner.findViewById(R.id.spinner_text_bedload_name);
            main_text.setText(bedloadSize[position]);

            TextView subSpinner = (TextView) mySpinner.findViewById(R.id.spinner_text_bedload_price);
            subSpinner.setText("$"+bedloadPrice[position]);

//            ImageView left_icon = (ImageView) mySpinner.findViewById(R.id.left_pic);
//            left_icon.setImageResource(R.drawable.dump_truck);

            return mySpinner;
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("tab", "Tab2CalcFragment"); //save the tab selected
        super.onSaveInstanceState(outState);

    }

}