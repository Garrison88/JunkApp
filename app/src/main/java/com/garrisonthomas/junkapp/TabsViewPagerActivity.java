package com.garrisonthomas.junkapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.garrisonthomas.junkapp.tabfragments.CalcFragment;
import com.garrisonthomas.junkapp.tabfragments.DumpFragment;

import java.util.ArrayList;
import java.util.List;

public class TabsViewPagerActivity extends BaseActivity {

    private TabLayout tabLayout;
    private String currentJournal;
    private SharedPreferences preferences;
    private ViewPager viewPager;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.tabs_viewpager_layout);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        currentJournal = preferences.getString("currentJournalRef", null);
//        journalAvailable = preferences.getBoolean("journalAvailable", false);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        setupViewPager(viewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (auth.getCurrentUser() != null) {
            toolbar.setSubtitle(auth.getCurrentUser().getEmail());
        }

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                invalidateOptionsMenu();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void notifyJournalChanged() {

        viewPager.getAdapter().notifyDataSetChanged();
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_assignment_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_keyboard_white_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_local_shipping_white_24dp);
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new JournalTabFragment(), "Journal");
        adapter.addFragment(new CalcFragment(), "Calculator");
        adapter.addFragment(new DumpFragment(), "Dumps");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //This will set tabs with titles
            return mFragmentTitleList.get(position);
            //This will set tabs with icons
//            return null;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem loginLogout = menu.findItem(R.id.action_login_logout);
        MenuItem delete = menu.findItem(R.id.action_delete_journal);
        MenuItem archive = menu.findItem(R.id.action_archive_journal);

        loginLogout.setTitle((auth.getCurrentUser() != null) ? "Logout" : "Login");

        if (tabLayout.getSelectedTabPosition() == 0 && currentJournal != null) {
            delete.setVisible(true);
            archive.setVisible(true);
        } else {
            delete.setVisible(false);
            archive.setVisible(false);
        }
        return true;
    }
}