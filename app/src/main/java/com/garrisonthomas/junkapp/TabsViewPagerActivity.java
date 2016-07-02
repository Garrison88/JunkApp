package com.garrisonthomas.junkapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.garrisonthomas.junkapp.tabfragments.Tab1DashFragment;
import com.garrisonthomas.junkapp.tabfragments.Tab2CalcFragment;
import com.garrisonthomas.junkapp.tabfragments.Tab3DumpFragment;

import java.util.ArrayList;
import java.util.List;

public class TabsViewPagerActivity extends BaseActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
//    private IconicsDrawable journalIcon = new IconicsDrawable(this)
//            .icon(Octicons.Icon.oct_repo)
//            .color(Color.BLACK)
//            .sizeDp(24);

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.tabs_viewpager_layout);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
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
        adapter.addFragment(new Tab1DashFragment(), "Journal");
        adapter.addFragment(new Tab2CalcFragment(), "Calculator");
        adapter.addFragment(new Tab3DumpFragment(), "Dumps");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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

        public void addFragment(Fragment fragment, String title) {
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
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_delete_journal).setVisible(false);
        menu.findItem(R.id.action_publish_journal).setVisible(false);
        return true;
    }
}