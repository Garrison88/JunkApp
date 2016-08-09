package com.garrisonthomas.junkapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    public String todaysDate;

    Menu menu;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Date date = new Date();
        SimpleDateFormat df2 = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.CANADA);
        todaysDate = df2.format(date);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            if (resultCode == RESULT_OK) {
                // user is signed in!
                startActivity(new Intent(this, TabsViewPagerActivity.class));
                Toast.makeText(BaseActivity.this, "Welcome!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                // user is not signed in. Maybe just wait for the user to press
                // "sign in" again, or show a message
            }
        }
    }

    public ProgressDialog mProgressDialog;

    public void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(message);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        this.menu = menu;

        MenuItem loginLogout = menu.findItem(R.id.action_login_logout);

        if (user != null) {
            loginLogout.setTitle("Logout");
        } else {
            loginLogout.setTitle("Login");
        }

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

        if (user != null) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {

                            // set menu item to "Sign Out"

                            MenuItem loginLogout = menu.findItem(R.id.action_login_logout);

                            loginLogout.setTitle("Logout");

                            // user is now signed out
                            Toast.makeText(BaseActivity.this, "Successfully logged out", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(BaseActivity.this, TabsViewPagerActivity.class));

                        }
                    });
        } else {
            startActivityForResult(
                    // Get an instance of AuthUI based on the default app
                    AuthUI.getInstance().createSignInIntentBuilder().build(),
                    123);
        }

    }

}