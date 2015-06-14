package com.garrisonthomas.junkapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Tab2CalcFragment extends Fragment implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.tab2_calc_layout, container, false);

        final ArrayList<Integer> priceArray = new ArrayList<>();

        final Spinner vSpinner = (Spinner) v.findViewById(R.id.spinner_volume);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.string_load_name, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vSpinner.setAdapter(adapter);
        vSpinner.setSelection(0);
        vSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

               @Override
               public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                   Button addVolume = (Button) getActivity().findViewById(R.id.btn_add_volume);
                   Button deleteVolume = (Button) getActivity().findViewById(R.id.btn_delete_volume);

                   final TextView vSize = (TextView) getActivity().findViewById(R.id.tv_display_volume);

                   final int[] volumePrice = getActivity().getResources().getIntArray(R.array.string_volume_price);

                   final String[] loadSize = getActivity().getResources().getStringArray(R.array.string_load_name);

                   final int vPrice = volumePrice[position];

                   addVolume.setOnClickListener(new View.OnClickListener() {

                       public void onClick(View v) {

                           if (vSize.length() > 0) {
                               vSize.append(" + ");
                           }

                           vSize.append(loadSize[position] +" ($"+volumePrice[position]+")");

                           priceArray.add(vPrice);
                       }
                   });

                   deleteVolume.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {

                           if (vSize.length() > 0) {

                               priceArray.remove(priceArray.size() - 1);

                           }
                       }
                   });

               }

               @Override
               public void onNothingSelected(AdapterView<?> parent) {

               }

           });

        final Spinner bSpinner = (Spinner) v.findViewById(R.id.spinner_bedload);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.string_load_name, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bSpinner.setAdapter(adapter2);
        bSpinner.setSelection(0);
        bSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                           @Override
                           public void onItemSelected(AdapterView<?> parent, View view, final int position,
                                                      long id) {

                               Button addBedload = (Button) getActivity().findViewById(R.id.btn_add_bedload);

                               final TextView bSize = (TextView) getActivity().findViewById(R.id.tv_display_bedload);

                               final int[] bedloadPrice = getActivity().getResources().getIntArray(R.array.string_bedload_price);

                               final String[] loadSize = getActivity().getResources().getStringArray(R.array.string_load_name);

                               final int bPrice = bedloadPrice[position];

                               final String bPriceString = String.valueOf(bPrice);

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

        Button calcCost = (Button) v.findViewById(R.id.btn_calc_cost);
        Button clearCost = (Button) v.findViewById(R.id.btn_clear_cost);

        calcCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView tvTotal = (TextView) getActivity().findViewById(R.id.load_total);

                double sum = 0;
                for (int i : priceArray) {
                    sum += i;
                }

                String sumString = String.valueOf(sum);
                tvTotal.setText("$"+sumString);

            }


        });

        clearCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView vSize = (TextView) getActivity().findViewById(R.id.tv_display_volume);
                TextView bSize = (TextView) getActivity().findViewById(R.id.tv_display_bedload);
                TextView tvTotal = (TextView) getActivity().findViewById(R.id.load_total);

                priceArray.clear();
                vSize.setText("");
                bSize.setText("");
                tvTotal.setText("");


            }
        });

            return v;
        }

        @Override
    public void onClick(View v) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("tab", "Tab2CalcFragment"); //save the tab selected
        super.onSaveInstanceState(outState);
//        outState.putString("volume display", );

    }

}
