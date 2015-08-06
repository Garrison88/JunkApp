package com.garrisonthomas.junkapp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

/**
 * The <code>TabsViewPagerFragmentActivity</code> class implements the Fragment activity that maintains a TabHost using a ViewPager.
 *
 * @author mwho
 */
public class TabsViewPagerFragmentActivity extends AppCompatActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

    private TabHost mTabHost;
    private ViewPager mViewPager;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabsViewPagerFragmentActivity.TabInfo>();
    private PagerAdapter mPagerAdapter;

    int TAKE_PHOTO_CODE = 0;
    public static int count=0;

    /**
     * @author mwho
     *         Maintains extrinsic info of a tab's construct
     */
    private class TabInfo {
        private String tag;
        private Class<?> clss;
        private Bundle args;
        private Fragment fragment;

        TabInfo(String tag, Class<?> clazz, Bundle args) {
            this.tag = tag;
            this.clss = clazz;
            this.args = args;
        }

    }

    /**
     * A simple factory that returns dummy views to the Tabhost
     *
     * @author mwho
     */
    class TabFactory implements TabContentFactory {

        private final Context mContext;

        /**
         * @param context
         */
        public TabFactory(Context context) {
            mContext = context;
        }

        /**
         * (non-Javadoc)
         *
         * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
         */
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }

    }

    /**
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout
        setContentView(R.layout.tabs_viewpager_layout);
        // Initialise the TabHost
        this.initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }
        // Intialise ViewPager
        this.intialiseViewPager();

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_call_office) {

            Uri number = Uri.parse("tel:18007436348");
            Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
            startActivity(callIntent);

        } else if (id == R.id.menu_about_developer) {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.developer_website)));
            startActivity(browserIntent);

        } else if (id == R.id.action_settings) {

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.action_email_office) {

            Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "rcrawford@ridofittoronto.com", null));
            startActivity(Intent.createChooser(i, "Choose an Email client :"));

        } else if (id == R.id.action_take_photo) {

            //here,we are making a folder named picFolder to store pics taken by the camera using this application
            final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/JunkPics/";
            File newdir = new File(dir);
            newdir.mkdirs();

                    // here,counter will be incremented each time,and the picture taken by camera will be stored as 1.jpg,2.jpg and likewise.
                    count++;
                    String file = dir+count+".jpg";
                    File newfile = new File(file);
                    try {
                        newfile.createNewFile();
                    } catch (IOException e) {}

                    Uri outputFileUri = Uri.fromFile(newfile);

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                    startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
                }



        return super.onOptionsItemSelected(item);
    }

    /**
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
     */
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
    }

    /**
     * Initialise ViewPager
     */
    private void intialiseViewPager() {

        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, Tab1DashFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, Tab2CalcFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, Tab3DumpFragment.class.getName()));
        this.mPagerAdapter = new PagerAdapter(super.getSupportFragmentManager(), fragments);
        //
        this.mViewPager = (ViewPager) super.findViewById(R.id.viewpager);
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.addOnPageChangeListener(this);
    }

    /**
     * Initialise the Tab Host
     */
    private void initialiseTabHost(Bundle args) {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        TabInfo tabInfo = null;
        TabsViewPagerFragmentActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab1").setIndicator("Dashboard"), (tabInfo = new TabInfo("Tab1", Tab1DashFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        TabsViewPagerFragmentActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab2").setIndicator("Calculator"), (tabInfo = new TabInfo("Tab2", Tab2CalcFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        TabsViewPagerFragmentActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab3").setIndicator("Dumps"), (tabInfo = new TabInfo("Tab3", Tab3DumpFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        // Default to first tab
        //this.onTabChanged("Tab1");
        //
        mTabHost.setOnTabChangedListener(this);
    }

    /**
     * Add Tab content to the Tabhost
     *
     * @param activity
     * @param tabHost
     * @param tabSpec
     */
    private static void AddTab(TabsViewPagerFragmentActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        // Attach a Tab view factory to the spec
        tabSpec.setContent(activity.new TabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    /**
     * (non-Javadoc)
     *
     * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
     */
    public void onTabChanged(String tag) {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        TabInfo newTab = this.mapTabInfo.get(tag);
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos, true);
    }

    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)
     */
    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected(int)
     */
    @Override
    public void onPageSelected(int position) {
        // TODO Auto-generated method stub
        this.mTabHost.setCurrentTab(position);
    }

    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrollStateChanged(int)
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        // TODO Auto-generated method stub

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Log.d("CameraDemo", "Pic saved");


        }
    }



    public void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

}
