package com.garrisonthomas.junkapp.dialogfragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.garrisonthomas.junkapp.DialogFragmentHelper;
import com.garrisonthomas.junkapp.R;

public class DumpTabHost extends DialogFragmentHelper {

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState)
    {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dump_tab_host, container);

        // tab slider
        sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.90);

        getDialog().setContentView(R.layout.dump_tab_host);

        getDialog().getWindow().setLayout(screenWidth, LinearLayout.LayoutParams.WRAP_CONTENT); //set below the setContentview

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager)view.findViewById(R.id.pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        return view;
    }

    /**
     * Used for tab paging...
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                // find first fragment...

                GarbageDumpFragment GDF = new GarbageDumpFragment();
//                Bundle GDFBundle = new Bundle();
//                GDFBundle.p("jobSpinnerSID", selectedJobSID);
//                GDF.setArguments(GDFBundle);
                return GDF;
            }
            if (position == 1) {
                // find second fragment...

                RebateDumpFragment RDF = new RebateDumpFragment();
                return RDF;
            }
//            } else if (position == 2) {
//                // find first fragment...
//                GarbageDumpFragment ADDF3 = new GarbageDumpFragment();
//                return ADDF3;
//            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Garbage";
                case 1:
                    return "Rebate";
//                case 2:
//                    return "Third Tab";
            }
            return null;
        }
    }
}
