package com.garrisonthomas.junkapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 5/28/2015.
 */
public class Tab2CalcFragment extends Fragment implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab2_calculator_layout, container, false);

        Button mClickButton1 = (Button) v.findViewById(R.id.btn_v_8);
        mClickButton1.setOnClickListener(this);
        Button mClickButton2 = (Button) v.findViewById(R.id.btn_v_4);
        mClickButton2.setOnClickListener(this);
        Button mClickButton3 = (Button) v.findViewById(R.id.btn_v_2);
        mClickButton3.setOnClickListener(this);
        Button mClickButton4 = (Button) v.findViewById(R.id.btn_v_full);
        mClickButton4.setOnClickListener(this);
        Button mClickButton5 = (Button) v.findViewById(R.id.btn_b_8);
        mClickButton5.setOnClickListener(this);
        Button mClickButton6 = (Button) v.findViewById(R.id.btn_b_4);
        mClickButton6.setOnClickListener(this);
        Button mClickButton7 = (Button) v.findViewById(R.id.btn_b_2);
        mClickButton7.setOnClickListener(this);
        Button mClickButton8 = (Button) v.findViewById(R.id.btn_b_full);
        mClickButton8.setOnClickListener(this);
        Button calculate = (Button) v.findViewById(R.id.btn_calculate);
        calculate.setOnClickListener(this);
        Button clear = (Button) v.findViewById(R.id.btn_clear);
        clear.setOnClickListener(this);

        return v;
    }

    ArrayList<Integer> priceArray = new ArrayList<>();

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_v_8: {

                TextView tvSize, tvCost;
                tvCost = (TextView) getActivity().findViewById(R.id.tv_cost);
                tvSize = (TextView) getActivity().findViewById(R.id.tv_size);

                if (tvSize.length() > 0) {
                    tvSize.append(", ");
                }

                tvSize.append("1/8");

                priceArray.add(99);

                tvCost.append("99");

                if (tvCost.length() > 0) {
                    tvCost.append(" + ");
                }

                Toast.makeText(getActivity(), "You added 1/8 volume", Toast.LENGTH_SHORT).show();

                break;
            }

            case R.id.btn_v_4: {
                TextView tvSize;
                tvSize = (TextView) getActivity().findViewById(R.id.tv_size);

                if (tvSize.length() > 0) {
                    tvSize.append(", ");
                }

                tvSize.append("1/4 ");
                Toast.makeText(getActivity(), "You added 1/4 volume", Toast.LENGTH_SHORT).show();

                break;
            }

            case R.id.btn_v_2: {
                TextView tvSize;
                tvSize = (TextView) getActivity().findViewById(R.id.tv_size);
                tvSize.append("1/2 ");
                break;
            }

            case R.id.btn_v_full: {
                TextView tvSize;
                tvSize = (TextView) getActivity().findViewById(R.id.tv_size);
                tvSize.append("Full ");
                break;
            }

            case R.id.btn_b_8: {
                // do something for button 2 click
                break;
            }

            case R.id.btn_b_4: {
                // do something for button 2 click
                break;
            }

            case R.id.btn_b_2: {
                // do something for button 2 click
                break;
            }

            case R.id.btn_b_full: {
                // do something for button 2 click
                break;
            }

            case R.id.btn_calculate: {
                // do something for button 2 click
                break;
            }

            case R.id.btn_clear: {

                TextView tvSize, tvCost;
                tvSize = (TextView) getActivity().findViewById(R.id.tv_size);
                tvCost = (TextView) getActivity().findViewById(R.id.tv_cost);

                if (tvSize.length() > 0) {

                    tvSize.setText("");

                }

                if (tvCost.length() > 0) {

                    tvCost.setText("");

                }

                break;
            }

        }
    }
}