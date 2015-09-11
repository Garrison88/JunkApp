//package com.garrisonthomas.junkapp.DialogFragments;
//
//import android.app.DialogFragment;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Spinner;
//
//import com.garrisonthomas.junkapp.DailyJournal;
//import com.garrisonthomas.junkapp.DateHelper;
//import com.garrisonthomas.junkapp.R;
//import com.parse.FindCallback;
//import com.parse.ParseQuery;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ViewJobsDialogFragment extends DialogFragment {
//
//    Spinner selectJobSpinner;
//    String todaysDate;
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        final View v = inflater.inflate(R.layout.view_jobs_layout, container, false);
//
//        selectJobSpinner = (Spinner) v.findViewById(R.id.view_job_spinner);
//
//        todaysDate = DateHelper.getCurrentDate();
//
////        final ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.job_pay_type, android.R.layout.simple_spinner_item);
////        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////        selectJobSpinner.setAdapter(adapter);
////        selectJobSpinner.setSelection(0);
////
////        ParseQuery<DailyJournal> query = ParseQuery.getQuery("DailyJournal");
////        query.whereEqualTo("date", todaysDate);
////        query.include("jobs.ssid");
////        query.findInBackground(new FindCallback<DailyJournal>() {
////            @Override
////            public void done(List<DailyJournal> list, com.parse.ParseException e) {
////
////                if (e == null) {
////
////                    for (DailyJournal newJournal : list) {
////
////                        newJournal.setCrew(newJournal.getCrew());
////                        newJournal.setTruckNumber(newJournal.getTruckNumber());
////
//////                        jobsArray.add(newJournal);
////                        adapter.add(newJournal);
////
////                    }
////
//////                    jobsArray.toArray();
////
////                }
////
////            }
////        });
//
////        adapter.add(jobsArray);
//
//        selectJobSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
//
////                payTypeString = payTypeArray[position];
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//
//        });
//
//        return v;
//    }
//}
//
