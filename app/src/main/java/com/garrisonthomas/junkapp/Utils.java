package com.garrisonthomas.junkapp;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

//import com.garrisonthomas.junkapp.parseobjects.NewDump;
//import com.garrisonthomas.junkapp.parseobjects.NewFuel;
//import com.garrisonthomas.junkapp.parseobjects.NewQuote;

/**
 * Created by GarrisonThomas on 2015-10-08.
 */
public abstract class Utils {

    public static void populateJobSpinner(final Context context,
                                          final ArrayList<Integer> jobsArray, final Spinner jobsSpinner) {

    }

//    public static void populateQuoteSpinner(final Context context, String currentJournalId,
//                                            final ArrayList<Integer> quotesArray, final Spinner quotesSpinner) {
//
//        ParseQuery<NewQuote> query = ParseQuery.getQuery(NewQuote.class);
//        query.whereEqualTo("relatedJournal", currentJournalId);
//        query.orderByAscending("createdAt");
//        query.fromPin();
//        query.findInBackground(new FindCallback<NewQuote>() {
//            @Override
//            public void done(List<NewQuote> list, com.parse.ParseException e) {
//
//                if (e == null) {
//
//                    for (NewQuote quote : list) {
//
//                        quotesArray.add(quote.getQuoteSID());
//
//                    }
//
//                    ArrayAdapter<Integer> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, quotesArray);
//                    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//                    quotesSpinner.setAdapter(adapter);
//
//
//                } else {
//                    Toast.makeText(context, "Something went wrong: " + e, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        });
//
//    }

    public static void populateDumpSpinner(final Context context, String currentJournalId,
                                           final ArrayList<String> dumpsArray, final Spinner dumpsSpinner) {



    }

    public static void populateFuelSpinner(final Context context, String currentJournalId,
                                           final ArrayList<String> fuelArray, final Spinner fuelSpinner) {



    }

    public static double calculateTax(double grossSale) {
        return Math.round((grossSale * 1.13) * 100.00) / 100.00;
    }

    public static void chooseTime(Context context, final Button button) {

        final Calendar c = Calendar.getInstance();
        final int hour = c.get(Calendar.HOUR_OF_DAY);
        final int minute = c.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_DARK, new TimePickerDialog.OnTimeSetListener() {
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
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle(null);
        mTimePicker.show();

    }

}