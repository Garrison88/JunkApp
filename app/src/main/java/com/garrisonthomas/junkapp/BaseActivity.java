package com.garrisonthomas.junkapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.auth.AuthUI;
import com.garrisonthomas.junkapp.entryobjects.TransferStation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    public List<String> dumpAddressArrayList = new ArrayList<>();
    public List<String> dumpInfoArrayList = new ArrayList<>();
    public List<String> dumpNameArrayList = new ArrayList<>();
    public List<Integer> dumpMinimumArrayList = new ArrayList<>();
    public List<Integer> dumpRateArrayList = new ArrayList<>();
    public List<String> dumpPhoneNumberArrayList = new ArrayList<>();

    public static String[] dumpAddressArray, dumpInfoArray, dumpNameArray, dumpPhoneNumberArray;
    public static Integer[] dumpMinimumArray,dumpRateArray;
    public static String todaysDate;
    private static final int RC_SIGN_IN = 9001;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Date date = new Date();
        SimpleDateFormat df2 = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.CANADA);
        todaysDate = df2.format(date);

        populateDumpInfoArrayLists();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // user is signed in!
                startActivity(new Intent(this, TabsViewPagerActivity.class));
                Toast.makeText(BaseActivity.this, "Welcome!", Toast.LENGTH_LONG).show();
                finish();
            }
//            else {
//                // user is not signed in. Maybe just wait for the user to press
//                // "sign in" again, or show a message
//            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        this.menu = menu;

        MenuItem loginLogout = menu.findItem(R.id.action_login_logout);

        if (auth.getCurrentUser() != null) {
            loginLogout.setTitle("Logout");
        } else {
            loginLogout.setTitle("Login");
        }

//        (auth.getCurrentUser() != null) ? loginLogout.setTitle("Logout") : loginLogout.setTitle("Login");

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

        } else if (id == R.id.action_login_logout) {

            LoginLogout();

        } else if (id == R.id.action_email_office) {

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "rcrawford@ridofittoronto.com", null));
            if (emailIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(Intent.createChooser(emailIntent, "Choose an Email client:"));
            }

        }

        return super.onOptionsItemSelected(item);
    }

    public void LoginLogout() {

        if (auth.getCurrentUser() != null) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {

                            // set menu item to "Logout"

                            MenuItem loginLogout = menu.findItem(R.id.action_login_logout);

                            loginLogout.setTitle("Logout");

                            // user is now logged out
                            Toast.makeText(BaseActivity.this, "Successfully logged out", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(BaseActivity.this, TabsViewPagerActivity.class));

                        }
                    });
        } else {
            startActivityForResult(
                    // Get an instance of AuthUI based on the default app
                    AuthUI
                            .getInstance()
                            .createSignInIntentBuilder()
                            .setTheme(R.style.CustomTheme_NoActionBar)
                            .build(),
                    RC_SIGN_IN);

        }

    }

    public void populateDumpInfoArrayLists() {

        Firebase dumpInfoRef = new Firebase("https://junkapp-43226.firebaseio.com/dumpInfo");
        dumpInfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TransferStation tStation;
                for (DataSnapshot dumpChild : dataSnapshot.getChildren()) {

                    tStation = dumpChild.getValue(TransferStation.class);

                    dumpAddressArrayList.add(tStation.getAddress());
                    dumpInfoArrayList.add(tStation.getInfo());
                    dumpMinimumArrayList.add(tStation.getMinimum());
                    dumpNameArrayList.add(tStation.getName());
                    dumpPhoneNumberArrayList.add(tStation.getPhoneNumber());
                    dumpRateArrayList.add(tStation.getRate());

                }

                dumpAddressArray = dumpAddressArrayList.toArray(new String[dumpAddressArrayList.size()]);
                dumpInfoArray = dumpInfoArrayList.toArray(new String[dumpInfoArrayList.size()]);
                dumpMinimumArray = dumpMinimumArrayList.toArray(new Integer[dumpMinimumArrayList.size()]);
                dumpNameArray = dumpNameArrayList.toArray(new String[dumpNameArrayList.size()]);
                dumpPhoneNumberArray = dumpPhoneNumberArrayList.toArray(new String[dumpPhoneNumberArrayList.size()]);
                dumpRateArray = dumpRateArrayList.toArray(new Integer[dumpRateArrayList.size()]);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

}