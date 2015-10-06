package com.garrisonthomas.junkapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    private int TAKE_PHOTO_CODE = 0;
    private int count = 0;
    String todaysDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Date date = new Date();
        SimpleDateFormat df2 = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.CANADA);
        todaysDate = df2.format(date);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

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


        } else if (id == R.id.action_take_photo) {

            //here,we are making a folder named picFolder to store pics taken by the camera using this application
            final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/JunkPics/";
            File newdir = new File(dir);
            newdir.mkdirs();

            // here,counter will be incremented each time,and the picture taken by camera will be stored as 1.jpg,2.jpg and likewise.
            count++;
            String file = dir + count + ".jpg";
            File newfile = new File(file);
            try {
                newfile.createNewFile();
            } catch (IOException e) {
            }

            Uri outputFileUri = Uri.fromFile(newfile);


            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
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

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(v);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }

    public void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

}