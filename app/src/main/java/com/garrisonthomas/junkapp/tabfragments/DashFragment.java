//package com.garrisonthomas.junkapp.tabfragments;
//
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//
//import com.garrisonthomas.junkapp.R;
//import com.google.firebase.auth.FirebaseAuth;
//
//public class DashFragment extends Fragment {
//
//    private Button openJournal;
//    private String currentJournalRef;
//    private SharedPreferences preferences;
//    private FirebaseAuth auth = FirebaseAuth.getInstance();
//    private FragmentManager manager;
////    private CreateJournalDialogFragment djFragment;
//
//    public DashFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        manager = getActivity().getSupportFragmentManager();
////        djFragment = new CreateJournalDialogFragment();
//
//        preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
//        currentJournalRef = preferences.getString("currentJournalRef", null);
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        final View v = inflater.inflate(R.layout.tab1_journal_layout, container, false);
//
//        openJournal = (Button) v.findViewById(R.id.btn_open_journal);
//
////        final View coordinatorLayoutView = v.findViewById(R.id.snackbar_create_journal);
//
//        //handle snackbar clicks for create journal
////        final View.OnClickListener createJournalClickListener = new View.OnClickListener() {
////            public void onClick(View v) {
////
////                djFragment.show(manager, "Dialog");
////
////            }
////        };
//
//
//
//        return v;
//
//    }
//
////    @Override
////    public void onResume() {
////        super.onResume();
////
////        preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
////        currentJournalRef = preferences.getString("currentJournalRef", null);
////
////        if (currentJournalRef == null) {
////            openJournal.setText("Create Journal");
////        } else {
////            openJournal.setText("Open Journal");
////        }
////
////    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//
//        super.onSaveInstanceState(outState);
//        outState.putString("tab", "DashFragment"); //save the tab selected
//
//    }
//
//
//
//}