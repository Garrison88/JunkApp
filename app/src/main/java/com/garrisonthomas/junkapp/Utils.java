package com.garrisonthomas.junkapp;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.Calendar;

public class Utils {

    static void populateIntegerSpinner(final Context context, final Firebase firebase,
                                              final ArrayList<Integer> arrayList, final Spinner spinner) {

        firebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {

                arrayList.add(Integer.valueOf(snapshot.getKey()));

                ArrayAdapter<Integer> adapter = new ArrayAdapter<>(context,
                        android.R.layout.simple_spinner_item, arrayList);
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spinner.setAdapter(adapter);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                arrayList.remove(Integer.valueOf(dataSnapshot.getKey()));

                ArrayAdapter<Integer> adapter = new ArrayAdapter<>(context,
                        android.R.layout.simple_spinner_item, arrayList);
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }


    static void populateStringSpinner(final Context context, final Firebase firebase,
                                             final ArrayList<String> arrayList, final Spinner spinner) {

        firebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {

                arrayList.add(snapshot.getKey());

                ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                        android.R.layout.simple_spinner_item, arrayList);
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spinner.setAdapter(adapter);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                arrayList.remove(dataSnapshot.getKey());

                ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                        android.R.layout.simple_spinner_item, arrayList);
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public static double calculateTax(double grossSale) {
        return Math.round((grossSale * 1.13) * 100.00) / 100.00;
    }

    public static double calculateDump(int pricePerTonne,
                                       double weightInTonnes, Spinner spinner) {

        double result;
        int position = spinner.getSelectedItemPosition();
        int minimum = BaseActivity.dumpMinimumArray[position];

        if (Math.round((weightInTonnes * pricePerTonne) * 100.00) / 100.00 < minimum) {
            result = minimum;
        } else {
            result = Math.round((weightInTonnes * pricePerTonne) * 100.00) / 100.00;
        }

        return result;

    }

    public static void chooseTime(Context context, final Button button) {

        final Calendar c = Calendar.getInstance();
        final int hour = c.get(Calendar.HOUR_OF_DAY);
        final int minute = c.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, AlertDialog.BUTTON_POSITIVE, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if (selectedMinute == 00) {
                    button.setText(selectedHour + ":" + selectedMinute + "0");
                } else if (selectedMinute < 10) {
                    button.setText(selectedHour + ":" + String.format("%02d", selectedMinute));
                } else {
                    button.setText(selectedHour + ":" + selectedMinute);
                }
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle(null);
        mTimePicker.show();

    }

}