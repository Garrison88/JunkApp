package com.garrisonthomas.junkapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    private int TAKE_PHOTO_CODE = 0;
    private int count = 0;
    public String todaysDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));

        Date date = new Date();
        SimpleDateFormat df2 = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.CANADA);
        todaysDate = df2.format(date);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_call_office) {

            Uri number = Uri.parse(getString(R.string.office_phone_number_uri));
            Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
            startActivity(callIntent);

        } else if (id == R.id.action_about_developer) {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.developer_website)));
            startActivity(browserIntent);

        } else if (id == R.id.action_settings) {

            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);

        } else if (id == R.id.action_email_office) {

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "rcrawford@ridofittoronto.com", null));
            if (emailIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Log.d("CameraDemo", "Pic saved");
            Toast.makeText(BaseActivity.this, "Photo saved to device", Toast.LENGTH_SHORT).show();

        }
    }

    public Boolean isInternetAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}