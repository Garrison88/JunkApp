package com.garrisonthomas.junkapp.tabfragments;

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

import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.Utils;

import java.util.ArrayList;

public class Tab2CalcFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private static Spinner vSpinner, bSpinner;
    private static Button clearCost, addHST;
    private static TextView tvVolumeSize, tvBedloadSize, tvTotal;
    private static int[] volumePrice, bedloadPrice;
    private static String[] volumeSize, bedloadSize;
    private static int vPrice, bPrice;
    private double beforeTax, sum, discount;
    private static String doubleValue, totalText, sumString;

    ArrayList<Integer> priceArray = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.tab2_calc_layout, container, false);

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

        vSpinner.setAdapter(new CustomVolumeSpinnerAdapter(getActivity(), R.layout.custom_spinner_layout, volumeSize));
        vSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                vSpinner.setSelection(0);
                vPrice = volumePrice[position];
                if (position != 0 && position != 12 && position != 15) {
                    if (!TextUtils.isEmpty(tvVolumeSize.getText())) {
                        tvVolumeSize.append("\n" + "+" + "\n");

                    }
                    if (position >= 16) {
                        tvVolumeSize.append(volumeSize[position]);
                        discount = vPrice / 100;
                    } else {
                        tvVolumeSize.append(volumeSize[position] + " ($" + volumePrice[position] + ")");
                        priceArray.add(vPrice);
                        calcCost();
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        bSpinner.setAdapter(new CustomBedloadSpinnerAdapter(getActivity(), R.layout.custom_spinner_layout, bedloadSize));
        bSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                bSpinner.setSelection(0);
                bPrice = bedloadPrice[position];
                if (position != 0) {
                    if (!TextUtils.isEmpty(tvBedloadSize.getText())) {
                        tvBedloadSize.append("\n" + "+" + "\n");
                    }

                    tvBedloadSize.append(bedloadSize[position] + " ($" + bedloadPrice[position] + ")");
                    priceArray.add(bPrice);

                    calcCost();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //clears priceArray and all textViews
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

                        totalText = Double.toString(Utils.calculateTax(beforeTax));
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

            sumString = getString(R.string.dollar_sign) + String.valueOf(sum);
            tvTotal.setText(sumString);

        }
    }

    public void showHST() {

        double tax = Double.parseDouble(totalText) - sum;
        String taxString = "$" + (Math.round(tax * 100.00) / 100.00);
        tvTotal.setText(taxString);

    }

    public class CustomVolumeSpinnerAdapter extends ArrayAdapter<String> {

        public CustomVolumeSpinnerAdapter(Context ctx, int txtViewResourceId, String[] objects) {
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
            View mySpinner = inflater.inflate(R.layout.custom_spinner_layout, parent,
                    false);

            TextView main_text = (TextView) mySpinner.findViewById(R.id.tv_custom_spinner_first);
            main_text.setText(volumeSize[position]);

            TextView subSpinner = (TextView) mySpinner.findViewById(R.id.tv_custom_spinner_second);

            if (position == 0 || position == 12 || position >= 15) {
                subSpinner.setVisibility(View.GONE);
            } else {
                subSpinner.setText("$" + volumePrice[position]);
            }

//            ImageView left_icon = (ImageView) mySpinner.findViewById(R.id.left_pic);
//            left_icon.setImageResource(R.drawable.dump_truck);

            return mySpinner;
        }

    }

    public class CustomBedloadSpinnerAdapter extends ArrayAdapter<String> {

        public CustomBedloadSpinnerAdapter(Context ctx, int txtViewResourceId, String[] objects) {
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
            View mySpinner = inflater.inflate(R.layout.custom_spinner_layout, parent,
                    false);

            TextView main_text = (TextView) mySpinner.findViewById(R.id.tv_custom_spinner_first);
            main_text.setText(bedloadSize[position]);

            TextView subSpinner = (TextView) mySpinner.findViewById(R.id.tv_custom_spinner_second);

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

        super.onSaveInstanceState(outState);
        outState.putString("tab", "Tab2CalcFragment"); //save the tab selected

    }

}