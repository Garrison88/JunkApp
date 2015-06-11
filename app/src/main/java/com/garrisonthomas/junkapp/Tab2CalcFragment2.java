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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Tab2CalcFragment2 extends Fragment implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.tab2_calc_fragment_2, container, false);

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

                   final TextView vSize, jCost;
                   vSize = (TextView) getActivity().findViewById(R.id.tv_display_volume);
                   jCost = (TextView) getActivity().findViewById(R.id.tv_display_price);

                   final int[] volumePrice = getActivity().getResources().getIntArray(R.array.string_volume_price);

                   final int vPrice = volumePrice[position];

                   addVolume.setOnClickListener(new View.OnClickListener() {

                       public void onClick(View v) {

                           if (vSize.length() > 0) {
                               vSize.append(", ");
                           }

                           vSize.append(volumePrice.toString());

                           priceArray.add(vPrice);

                           if (jCost.length() > 0) {
                               jCost.append(" + ");
                           }

                           jCost.append(volumePrice.toString());

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

                               final TextView bSize, jCost;
                               bSize = (TextView) getActivity().findViewById(R.id.tv_display_bedload);
                               jCost = (TextView) getActivity().findViewById(R.id.tv_display_price);

                               final int[] bedloadPrice = getActivity().getResources().getIntArray(R.array.string_bedload_price);
                               final String[] loadSize = getActivity().getResources().getStringArray(R.array.string_load_name);

                               final int bPrice = bedloadPrice[position];

                               addBedload.setOnClickListener(new View.OnClickListener() {

                                   public void onClick(View v) {

                                       if (bSize.length() > 0) {
                                           bSize.append(", ");
                                       }

                                       bSize.append(loadSize.toString());

                                       priceArray.add(bPrice);

                                       if (jCost.length() > 0) {
                                           jCost.append(" + ");
                                       }

                                       jCost.append(bedloadPrice.toString());

                                   }
                               });

               }

               @Override
               public void onNothingSelected(AdapterView<?> parent) {

               }
           }

            );

        Button calcCost = (Button) v.findViewById(R.id.btn_calc_cost);

        calcCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "toast", Toast.LENGTH_SHORT).show();

            }


        });

            return v;
        }

        @Override
    public void onClick(View v) {

    }
}
