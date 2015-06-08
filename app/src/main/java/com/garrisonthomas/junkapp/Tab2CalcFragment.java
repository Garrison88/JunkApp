package com.garrisonthomas.junkapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

        ImageButton btnPhone = (ImageButton) v.findViewById(R.id.btn_phone);
        btnPhone.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel: 18007436348"));
                startActivity(i);
            }
        });

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
        Button labour = (Button) v.findViewById(R.id.btn_labour);
        labour.setOnClickListener(this);
        Button tire = (Button) v.findViewById(R.id.btn_tire);
        tire.setOnClickListener(this);
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
                tvCost.setTextColor(Color.BLUE);

                if (tvSize.length() > 0) {
                    tvSize.append(", ");
                }

                tvSize.append("1/8");

                priceArray.add(99);

                if (tvCost.length() > 0) {
                    tvCost.append(" + ");
                }

                tvCost.append("99");

                break;
            }

            case R.id.btn_v_4: {

                TextView tvSize, tvCost;
                tvCost = (TextView) getActivity().findViewById(R.id.tv_cost);
                tvSize = (TextView) getActivity().findViewById(R.id.tv_size);
                tvCost.setTextColor(Color.BLUE);

                if (tvSize.length() > 0) {
                    tvSize.append(", ");
                }

                tvSize.append("1/4");

                priceArray.add(189);

                if (tvCost.length() > 0) {
                    tvCost.append(" + ");
                }

                tvCost.append("189");

                break;
            }

            case R.id.btn_v_2: {

                TextView tvSize, tvCost;
                tvCost = (TextView) getActivity().findViewById(R.id.tv_cost);
                tvSize = (TextView) getActivity().findViewById(R.id.tv_size);
                tvCost.setTextColor(Color.BLUE);

                if (tvSize.length() > 0) {
                    tvSize.append(", ");
                }

                tvSize.append("1/2");

                priceArray.add(309);

                if (tvCost.length() > 0) {
                    tvCost.append(" + ");
                }

                tvCost.append("309");
                break;

            }

            case R.id.btn_v_full: {

                TextView tvSize, tvCost;
                tvCost = (TextView) getActivity().findViewById(R.id.tv_cost);
                tvSize = (TextView) getActivity().findViewById(R.id.tv_size);
                tvCost.setTextColor(Color.BLUE);

                if (tvSize.length() > 0) {
                    tvSize.append(", ");
                }

                tvSize.append("Full");

                priceArray.add(479);

                if (tvCost.length() > 0) {
                    tvCost.append(" + ");
                }

                tvCost.append("479");

                break;
            }

            case R.id.btn_b_8: {

                TextView tvSize, tvCost;
                tvCost = (TextView) getActivity().findViewById(R.id.tv_cost);
                tvSize = (TextView) getActivity().findViewById(R.id.tv_size);

                if (tvSize.length() > 0) {
                    tvSize.append(", ");
                }

                tvSize.append("1/8");

                priceArray.add(159);

                if (tvCost.length() > 0) {
                    tvCost.append(" + ");
                }

                tvCost.append("159");

                break;
            }

            case R.id.btn_b_4: {

                TextView tvSize, tvCost;
                tvCost = (TextView) getActivity().findViewById(R.id.tv_cost);
                tvSize = (TextView) getActivity().findViewById(R.id.tv_size);

                if (tvSize.length() > 0) {
                    tvSize.append(", ");
                }

                tvSize.append("1/4");

                priceArray.add(229);

                if (tvCost.length() > 0) {
                    tvCost.append(" + ");
                }

                tvCost.append("229");

                break;
            }

            case R.id.btn_b_2: {

                TextView tvSize, tvCost;
                tvCost = (TextView) getActivity().findViewById(R.id.tv_cost);
                tvSize = (TextView) getActivity().findViewById(R.id.tv_size);

                if (tvSize.length() > 0) {
                    tvSize.append(", ");
                }

                tvSize.append("1/2");

                priceArray.add(289);

                if (tvCost.length() > 0) {
                    tvCost.append(" + ");
                }

                tvCost.append("289");

                break;
            }

            case R.id.btn_b_full: {

                TextView tvSize, tvCost;
                tvCost = (TextView) getActivity().findViewById(R.id.tv_cost);
                tvSize = (TextView) getActivity().findViewById(R.id.tv_size);

                if (tvSize.length() > 0) {
                    tvSize.append(", ");
                }

                tvSize.append("Full B/L");

                priceArray.add(459);

                if (tvCost.length() > 0) {
                    tvCost.append(" + ");
                }

                tvCost.append("459");

                break;
            }

            case R.id.btn_labour: {

                TextView tvSize, tvCost;
                tvCost = (TextView) getActivity().findViewById(R.id.tv_cost);
                tvSize = (TextView) getActivity().findViewById(R.id.tv_size);

                if (tvSize.length() > 0) {
                    tvSize.append(", ");
                }

                tvSize.append("1 Hour Labour");

                priceArray.add(99);

                if (tvCost.length() > 0) {
                    tvCost.append(" + ");
                }

                tvCost.append("99");

                break;
            }

            case R.id.btn_tire: {

                TextView tvSize, tvCost;
                tvCost = (TextView) getActivity().findViewById(R.id.tv_cost);
                tvSize = (TextView) getActivity().findViewById(R.id.tv_size);

                if (tvSize.length() > 0) {
                    tvSize.append(", ");
                }

                tvSize.append("1 Tire");

                priceArray.add(10);

                if (tvCost.length() > 0) {
                    tvCost.append(" + ");
                }

                tvCost.append("10");

                break;
            }

            case R.id.btn_calculate: {

                double sum = 0;
                for (int i : priceArray) {
                    sum += i;
                }

                AlertDialog total = new AlertDialog.Builder(getActivity()).create();
                total.setTitle("Total Charges");

                total.setMessage("Before HST: " + String.valueOf(sum) +
                        "\nHST: " + String.valueOf(Math.round((sum * 1.13 - sum) * 100) / 100.0) +
                        "\nIncluding HST: " + String.valueOf(Math.round((sum * 1.13) * 100) / 100.0) +
                        "\n" +
                        "\nWith 10% Discount: " + String.valueOf(Math.round((sum * .9 * 1.13) * 100) / 100.0) +
                        "\nWith 15% Discount: " + String.valueOf(Math.round((sum * .85 * 1.13) * 100) / 100.0) +
                        "\nWith 20% Discount: " + String.valueOf(Math.round((sum * .8 * 1.13) * 100) / 100.0));
                total.setButton(DialogInterface.BUTTON_NEUTRAL, "Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        priceArray.clear();

                        TextView tvSize, tvCost;
                        tvSize = (TextView) getActivity().findViewById(R.id.tv_size);
                        tvCost = (TextView) getActivity().findViewById(R.id.tv_cost);

                        if (tvSize.length() > 0) {

                            tvSize.setText("");

                        }

                        if (tvCost.length() > 0) {

                            tvCost.setText("");

                        }

                    }
                });
                total.show();

                priceArray.clear();

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