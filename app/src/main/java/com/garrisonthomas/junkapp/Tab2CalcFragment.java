package com.garrisonthomas.junkapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class Tab2CalcFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Spinner vSpinner, bSpinner;
    Button clearCost, addHST;
    TextView tvVolumeSize, tvBedloadSize, tvTotal;
    int[] volumePrice, bedloadPrice;
    String[] volumeSize, bedloadSize;
    int vPrice, bPrice;
    double beforeTax, sum;
    String doubleValue, totalText, sumString;

    ArrayList<Integer> priceArray = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab2_calc_layout, container, false);

        addHST = (Button) v.findViewById(R.id.btn_add_hst);
        clearCost = (Button) v.findViewById(R.id.btn_clear_cost);

        vSpinner = (Spinner) v.findViewById(R.id.spinner_volume);
        bSpinner = (Spinner) v.findViewById(R.id.spinner_bedload);

        tvVolumeSize = (TextView) v.findViewById(R.id.tv_display_volume);
        tvBedloadSize = (TextView) v.findViewById(R.id.tv_display_bedload);
        tvTotal = (TextView) v.findViewById(R.id.load_total);

        volumePrice = v.getResources().getIntArray(R.array.string_volume_price);
        bedloadPrice = v.getResources().getIntArray(R.array.string_bedload_price);
        volumeSize = v.getResources().getStringArray(R.array.string_volume_name);
        bedloadSize = v.getResources().getStringArray(R.array.string_bedload_name);

        vSpinner.setAdapter(new VolumeAdapter(getActivity(), R.layout.custom_volume_spinner, volumeSize));
        vSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                vSpinner.setSelection(0);
                vPrice = volumePrice[position];
                if (position != 0 && position != 12) {
                    if (!TextUtils.isEmpty(tvVolumeSize.getText())) {
                        tvVolumeSize.append("\n" + "+" + "\n");
                    }

                    tvVolumeSize.append(volumeSize[position] + " ($" + volumePrice[position] + ")");
                    priceArray.add(vPrice);

                    calcCost();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        bSpinner.setAdapter(new BedloadAdapter(getActivity(), R.layout.custom_bedload_spinner, bedloadSize));
        bSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                bPrice = bedloadPrice[position];
                if (position != 0) {
                    if (!TextUtils.isEmpty(tvBedloadSize.getText())) {
                        tvBedloadSize.append("\n" + "+" + "\n");
                    }

                    tvBedloadSize.append(bedloadSize[position] + " ($" + bedloadPrice[position] + ")");
                    priceArray.add(bPrice);

                    calcCost();
                    bSpinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        clearCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                priceArray.clear();
                tvVolumeSize.setText("");
                tvBedloadSize.setText("");
                tvTotal.setText("");
                addHST.setClickable(true);
                addHST.setText("Add HST");

            }
        });

        addHST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (addHST.getText().equals("Add HST")) {
                if (!TextUtils.isEmpty(tvTotal.getText())) {

                    doubleValue = tvTotal.getText().toString();
                    beforeTax = Double.parseDouble(doubleValue.substring(1));

                    totalText = Double.toString(Math.round((beforeTax * 1.13) * 100.00) / 100.00);
                    tvTotal.setText("$" + totalText);

                    addHST.setText("Display Tax");
                }
                } else {
                    showHST();
                    addHST.setClickable(false);
                }
            }
        });

        return v;
    }

    public void calcCost() {

        if (!TextUtils.isEmpty(tvVolumeSize.getText()) || !TextUtils.isEmpty(tvBedloadSize.getText())) {
            sum = 0;
            for (int i : priceArray) {
                sum += i;
            }
            sumString = String.valueOf(sum);
            tvTotal.setText("$" + sumString);

        }
    }

    public void showHST() {

        double tax = Double.parseDouble(totalText)-sum;

        tvTotal.setText("$"+(Math.round((tax) * 100.00) / 100.00));

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

            if (position == 0 || position == 12) {
                subSpinner.setVisibility(View.GONE);
            } else {
                subSpinner.setText("$" + volumePrice[position]);
            }

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

            if (position == 0) {
                subSpinner.setVisibility(View.GONE);
            } else {
                subSpinner.setText("$" + bedloadPrice[position]);
            }

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