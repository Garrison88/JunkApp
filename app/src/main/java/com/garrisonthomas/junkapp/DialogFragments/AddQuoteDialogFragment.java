package com.garrisonthomas.junkapp.dialogfragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.garrisonthomas.junkapp.DialogFragmentHelper;
import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.Utils;
import com.garrisonthomas.junkapp.entryobjects.QuoteObject;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class AddQuoteDialogFragment extends DialogFragmentHelper {

    private Button saveQuote, cancelQuote, startTime, endTime;
    private TextInputLayout enterQuoteSIDWrapper, enterLowEndWrapper, enterHighEndWrapper,
            enterQuoteNotesWrapper;
    private TextInputEditText etQuoteSID, etLowEnd, etHighEnd, etQuoteNotes;
    private ImageButton choosePhoto;
    private String firebaseJournalRef;
    private int PICK_IMAGE_REQUEST = 1;
    private SharedPreferences preferences;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReferenceFromUrl("gs://junkapp-43226.appspot.com");
    private Uri quotePhotoUri = null;
    private ProgressDialog pDialog;
    public static final String TAG = "permissions";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.add_quote_layout, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.90);

        getDialog().setContentView(R.layout.add_quote_layout);

        getDialog().getWindow().setLayout(screenWidth, LinearLayout.LayoutParams.WRAP_CONTENT); //set below the setContentview

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        firebaseJournalRef = preferences.getString("firebaseRef", "none");

        enterQuoteSIDWrapper = (TextInputLayout) v.findViewById(R.id.enter_quote_sid_wrapper);
        etQuoteSID = (TextInputEditText) enterQuoteSIDWrapper.getEditText();

        enterLowEndWrapper = (TextInputLayout) v.findViewById(R.id.enter_low_end_wrapper);
        etLowEnd = (TextInputEditText) enterLowEndWrapper.getEditText();

        enterHighEndWrapper = (TextInputLayout) v.findViewById(R.id.enter_high_end_wrapper);
        etHighEnd = (TextInputEditText) enterHighEndWrapper.getEditText();

        enterQuoteNotesWrapper = (TextInputLayout) v.findViewById(R.id.enter_quote_notes_wrapper);
        etQuoteNotes = (TextInputEditText) enterQuoteNotesWrapper.getEditText();

        startTime = (Button) v.findViewById(R.id.quote_start_time);
        endTime = (Button) v.findViewById(R.id.quote_end_time);
        saveQuote = (Button) v.findViewById(R.id.btn_save_quote);
        cancelQuote = (Button) v.findViewById(R.id.btn_cancel_quote);

        choosePhoto = (ImageButton) v.findViewById(R.id.btn_select_quote_photo);

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.chooseTime(getActivity(), startTime);
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.chooseTime(getActivity(), endTime);
            }
        });

        choosePhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isStoragePermissionGranted()) {

                    Intent intent = new Intent();
// Show only images, no videos or anything else
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
// Always show the chooser (if there are multiple options available)
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

                }
            }
        });

        saveQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if ((validateEditTextLength(etQuoteSID, 4, 6))
                        && (!TextUtils.isEmpty(etLowEnd.getText()))) {

                    if (quotePhotoUri != null) {

                        pDialog = ProgressDialog.show(getActivity(), null,
                                "Uploading photo...", true);

                        StorageReference quoteSIDRef = storageRef.child("quoteImages/" +
                                String.valueOf(etQuoteSID.getText()) + "/" + String.valueOf(quotePhotoUri.getLastPathSegment()));
                        UploadTask uploadTask = quoteSIDRef.putFile(quotePhotoUri);

//                 Register observers to listen for when the download is done or if it fails
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                Toast.makeText(getActivity(), "Upload failed due to " + exception + ". Please try again",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                String downloadUrlString = taskSnapshot.getDownloadUrl().toString();

                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                Firebase fbrQuote = new Firebase(firebaseJournalRef + "quotes/" + String.valueOf(etQuoteSID.getText()));

                                QuoteObject quote = new QuoteObject();
                                quote.setQuoteSID(Integer.valueOf(etQuoteSID.getText().toString()));
                                quote.setQuoteStartTime(String.valueOf(startTime.getText()));
                                quote.setQuoteEndTime(String.valueOf(endTime.getText()));
                                quote.setLowEnd(Double.valueOf(etLowEnd.getText().toString()));
                                quote.setPhotoDownloadUrl(downloadUrlString);
                                if (!TextUtils.isEmpty(etHighEnd.getText())) {
                                    quote.setHighEnd(Double.valueOf(etHighEnd.getText().toString()));
                                } else {
                                    quote.setHighEnd(0);
                                }
                                if (!TextUtils.isEmpty(etQuoteNotes.getText())) {
                                    quote.setQuoteNotes(String.valueOf(etQuoteNotes.getText()));
                                }

                                fbrQuote.setValue(quote);

                                Toast.makeText(getActivity(), "Quote number " + etQuoteSID.getText().toString() + " saved",
                                        Toast.LENGTH_SHORT).show();

                                pDialog.dismiss();
                                dismiss();

                                //and you can convert it to string like this:

                            }
                        });
                    } else {
                        Firebase fbrQuote = new Firebase(firebaseJournalRef + "quotes/" + String.valueOf(etQuoteSID.getText()));

                        QuoteObject quote = new QuoteObject();
                        quote.setQuoteSID(Integer.valueOf(etQuoteSID.getText().toString()));
                        quote.setQuoteStartTime(String.valueOf(startTime.getText()));
                        quote.setQuoteEndTime(String.valueOf(endTime.getText()));
                        quote.setLowEnd(Double.valueOf(etLowEnd.getText().toString()));
                        if (!TextUtils.isEmpty(etHighEnd.getText())) {
                            quote.setHighEnd(Double.valueOf(etHighEnd.getText().toString()));
                        } else {
                            quote.setHighEnd(0);
                        }
                        if (!TextUtils.isEmpty(etQuoteNotes.getText())) {
                            quote.setQuoteNotes(String.valueOf(etQuoteNotes.getText()));
                        }

                        fbrQuote.setValue(quote);

                        Toast.makeText(getActivity(), "Quote number " + etQuoteSID.getText().toString() + " saved",
                                Toast.LENGTH_SHORT).show();

                        dismiss();
                    }
                } else {
                    if (!validateEditTextLength(etQuoteSID, 4, 6)) {
                        enterQuoteSIDWrapper.setErrorEnabled(true);
                        enterQuoteSIDWrapper.setError("Must be 4-6 numbers");
                    } else {
                        enterQuoteSIDWrapper.setErrorEnabled(false);
                    }
                    if (TextUtils.isEmpty(etLowEnd.getText())) {
                        enterLowEndWrapper.setErrorEnabled(true);
                        enterLowEndWrapper.setError(getString(R.string.empty_et_error_message));
                    } else {
                        enterLowEndWrapper.setErrorEnabled(false);
                    }
                }
            }
        });

        cancelQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setCancelable(false);
        return v;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            quotePhotoUri = data.getData();
            Bitmap bitmapThumb;
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), quotePhotoUri);
                // Log.d(TAG, String.valueOf(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }

            bitmapThumb = ThumbnailUtils.extractThumbnail(bitmap,
                    250, 250);
//
            choosePhoto.setImageBitmap(bitmapThumb);

//                Picasso.with(getActivity())
//                        .load(quotePhotoUri) // thumbnail url goes here
//                        .into(choosePhoto, new Callback() {
//                            @Override
//                            public void onSuccess() {
//                                Picasso.with(getActivity())
//                                        .load(quotePhotoUri) // image url goes here
//                                        .placeholder(choosePhoto.getDrawable())
//                                        .into(choosePhoto);
//                            }
//                            @Override
//                            public void onError() {
//
//                            }
//                        });

//
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == (PackageManager.PERMISSION_GRANTED)) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
        }
    }
}
