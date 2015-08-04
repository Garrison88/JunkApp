//package com.garrisonthomas.junkapp;
//
//import android.content.Context;
//import android.content.Intent;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v7.app.ActionBarActivity;
//import android.support.v7.app.AppCompatActivity;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.EditText;
//import android.widget.Toolbar;
//
//public class BaseActivity extends AppCompatActivity {
//
//    Toolbar mToolbar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//
//        //TODO: add Facebook integration
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.menu_logout_button) {
//
//        } else if (id == R.id.menu_about_developer) {
//
////            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.developer_website)));
////            startActivity(browserIntent);
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    public Boolean isInternetAvailable() {
//        try {
//            ConnectivityManager connectivityManager = (ConnectivityManager) this
//                    .getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo activeNetworkInfo = connectivityManager
//                    .getActiveNetworkInfo();
//            if (activeNetworkInfo != null
//                    && activeNetworkInfo.isConnectedOrConnecting()) {
//                return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//        return false;
//    }
//
//    public void setupUI(View view) {
//
//        //Set up touch listener for non-text box views to hide keyboard.
//        if (!(view instanceof EditText)) {
//
//            view.setOnTouchListener(new View.OnTouchListener() {
//
//                public boolean onTouch(View v, MotionEvent event) {
//                    hideKeyboard(v);
//                    return false;
//                }
//
//            });
//        }
//
//        //If a layout container, iterate over children and seed recursion.
//        if (view instanceof ViewGroup) {
//
//            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
//
//                View innerView = ((ViewGroup) view).getChildAt(i);
//
//                setupUI(innerView);
//            }
//        }
//    }
//
//    public void hideKeyboard(View v) {
//        InputMethodManager imm = (InputMethodManager) getSystemService(
//                Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//    }
//
//}