package com.garrisonthomas.junkapp.dialogfragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class AddQuoteDialogFragment extends DialogFragmentHelper {

    private static EditText etQuoteSID, etLowEnd, etHighEnd, etQuoteNotes;
    private static Button saveQuote, cancelQuote, startTime, endTime;
    private static ImageButton choosePhoto;
    private static String firebaseJournalRef, imageSource;
    private SharedPreferences preferences;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReferenceFromUrl("gs://junkapp-43226.appspot.com");
    private Uri quotePhotoUri;
    private ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.add_quote_layout, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        firebaseJournalRef = preferences.getString("firebaseRef", "none");

        etQuoteSID = (EditText) v.findViewById(R.id.et_quote_sid);
        etLowEnd = (EditText) v.findViewById(R.id.et_low_end);
        etHighEnd = (EditText) v.findViewById(R.id.et_high_end);
        etQuoteNotes = (EditText) v.findViewById(R.id.et_quote_notes);

        startTime = (Button) v.findViewById(R.id.quote_start_time);
        endTime = (Button) v.findViewById(R.id.quote_end_time);
        saveQuote = (Button) v.findViewById(R.id.btn_save_quote);
        cancelQuote = (Button) v.findViewById(R.id.btn_cancel_quote);
        choosePhoto = (ImageButton) v.findViewById(R.id.btn_select_quote_photos);

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
            public void onClick(View view) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("image/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, 123);
            }
        });

        saveQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pDialog = ProgressDialog.show(getActivity(), null,
                        "Uploading photo...", true);

                // Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
                StorageReference quoteSIDRef = storageRef.child("quoteImages/" +
                        String.valueOf(etQuoteSID.getText()) + "/" + quotePhotoUri.getLastPathSegment());
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
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        String downloadUrlString = downloadUrl.toString();

                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
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

                        pDialog.dismiss();
                        dismiss();

                        //and you can convert it to string like this:

                    }
                });
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

        quotePhotoUri = data.getData();
        imageSource = quotePhotoUri.getPath();
        choosePhoto.setImageURI(quotePhotoUri);

    }
}
