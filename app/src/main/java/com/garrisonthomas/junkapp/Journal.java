//package com.garrisonthomas.junkapp;
//
//import android.content.Context;
//import android.content.Intent;
//import android.text.TextUtils;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.parse.FindCallback;
//import com.parse.ParseException;
//import com.parse.ParseObject;
//import com.parse.ParseQuery;
//
//import java.util.List;
//
///**
// * Created by Garrison Thomas on 2015-08-27.
// */
//public class Journal {
//
//    Context context;
//    String crew;
//    String truckNumber;
//
//    public String getCrew() {
//        return crew;
//    }
//
//    public String getTruckNumber() {
//        return truckNumber;
//    }
//
//    public Journal(String crew, String truckNumber) {
//
//        this.crew = crew;
//        this.truckNumber = truckNumber;
//    }
//
//    public void create(String crew, String truckNumber) {
//
//        ParseObject newJournal = new DailyJournal();
//        newJournal.put("date", DateHelper.getCurrentDate());
//        newJournal.put("crew", crew);
//        newJournal.put("truckNumber", truckNumber);
//        newJournal.pinInBackground();
//        newJournal.saveEventually();
//        Intent intent = new Intent(context, CurrentJournal.class);
//        intent.putExtra("EXTRA_CREW", crew);
//        intent.putExtra("EXTRA_TRUCK_NUMBER", truckNumber);
//        context.startActivity(intent);
//
//    }
//
//    public void addJob(final int ssid, final double grossSale, final double netSale, final String payType, final int receiptNumber) {
//
//
//        ParseQuery<DailyJournal> query = ParseQuery.getQuery("DailyJournal");
//        query.whereEqualTo("date", DateHelper.getCurrentDate());
//        query.findInBackground(new FindCallback<DailyJournal>() {
//            public void done(List<DailyJournal> list, ParseException e) {
//                if (e == null) {
//
//                    for (DailyJournal newJournal : list) {
//
//                        final ParseObject newJob = new NewJob();
//                        newJob.put("ssid", ssid);
//                        newJob.put("grossSale", grossSale);
//                        newJob.put("netSale", netSale);
//                        newJob.put("receiptNumber", receiptNumber);
//                        newJob.put("payType", payType);
//
//                        newJournal.add("jobs", newJob);
//
//                        newJob.pinInBackground();
//                        newJob.saveEventually();
//                        newJournal.pinInBackground();
//                        newJournal.saveEventually();
//
//                    }
//
//                } else {
//                    Log.d("score", "Error: " + e.getMessage());
//                }
//            }
//        });
//
////        Toast.makeText(context, "Job number " + String.valueOf(ssid) +
////                " saved", Toast.LENGTH_SHORT).show();
//    }
//
//}