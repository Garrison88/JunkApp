package com.garrisonthomas.junkapp.DialogFragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.garrisonthomas.junkapp.ParseObjects.DailyJournal;
import com.garrisonthomas.junkapp.ParseObjects.NewJob;
import com.garrisonthomas.junkapp.R;
import com.garrisonthomas.junkapp.Utils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class EndOfDayDialogFragment extends DialogFragment {

    SharedPreferences preferences;
    ProgressBar publishPbar;
    EditText endOfDayNotes;
    Button cancel, archive, dEndTime, nEndTime;
    ImageButton addPhoto;
    String currentJournalId, todaysDate, todaysTruck;
    private int PICK_IMAGE_REQUEST;
    byte[] image;
//    ImageView endDayImage;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Archive Journal");
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.end_of_day_layout, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        publishPbar = (ProgressBar) v.findViewById(R.id.publish_pbar);
        endOfDayNotes = (EditText) v.findViewById(R.id.end_day_notes);
        cancel = (Button) v.findViewById(R.id.cancel_publish_dialog);
        archive = (Button) v.findViewById(R.id.archive_journal);
        addPhoto = (ImageButton) v.findViewById(R.id.end_day_photo);
        dEndTime = (Button) v.findViewById(R.id.driver_end_time);
        dEndTime.setTransformationMethod(null);
        nEndTime = (Button) v.findViewById(R.id.nav_end_time);
        nEndTime.setTransformationMethod(null);

        todaysDate = preferences.getString("todaysDate", "noDate");
        todaysTruck = preferences.getString("truck", "noTruck");

        final Calendar c = Calendar.getInstance();
        final int hour = c.get(Calendar.HOUR_OF_DAY);
        final int minute = c.get(Calendar.MINUTE);

        PICK_IMAGE_REQUEST = 1;

        currentJournalId = preferences.getString("universalJournalId", "none");

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
// Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                // Locate the image in res > drawable-hdpi
//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
//                        R.drawable.fab_add);
//                // Convert it to byte
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                // Compress image to lower quality scale 1 - 100
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                byte[] image = stream.toByteArray();

//                // Create the ParseFile
//                ParseFile file = new ParseFile("androidbegin.png", image);
//                // Upload the image into Parse Cloud
//                file.saveInBackground();
//
//                // Create a New Class called "ImageUpload" in Parse
//                ParseObject imgupload = new ParseObject("ImageUpload");
//
//                // Create a column named "ImageName" and set the string
//                imgupload.put("ImageName", "AndroidBegin Logo");
//
//                // Create a column named "ImageFile" and insert the image
//                imgupload.put("ImageFile", file);
//
//                // Create the class and the columns
//                imgupload.saveInBackground();
//
//                // Show a simple toast message
//                Toast.makeText(getActivity(), "Image Uploaded",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        dEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(getActivity(), AlertDialog.BUTTON_NEUTRAL,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hour, int minute) {
                                String aMpM = "a.m.";
                                if (hour > 12) {
                                    hour -= 12;
                                    aMpM = "p.m.";
                                }
                                if (minute == 0) {
                                    dEndTime.setText(hour + ":" + minute + "0 " + aMpM);
                                } else {
                                    dEndTime.setText(hour + ":" + minute + " " + aMpM);
                                }
                            }
                        }, hour, minute, false);
                tpd.show();
            }
        });

        nEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(getActivity(), AlertDialog.BUTTON_NEUTRAL,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hour, int minute) {
                                String aMpM = "a.m.";
                                if(hour > 12)
                                {
                                    hour -= 12;
                                    aMpM = "p.m.";
                                }
                                if (minute == 0) {
                                    nEndTime.setText(hour + ":" + minute + "0 " + aMpM);
                                } else {
                                    nEndTime.setText(hour + ":" + minute + " " + aMpM);
                                }
                            }
                        }, hour, minute, false);
                tpd.show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String DET = dEndTime.getText().toString();
                final String NET = nEndTime.getText().toString();

                if (!DET.equals("DRIVER OUT") && !NET.equals("NAVIGATOR OUT")) {
                    AlertDialog diaBox = confirmJournalArchive();
                    diaBox.show();
                } else {
                    Toast.makeText(getActivity(), "Please clock out first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setCancelable(false);
        return v;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 15, stream);
                image = stream.toByteArray();

                addPhoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private AlertDialog confirmJournalArchive() {

        return new AlertDialog.Builder(getActivity())
                .setTitle("Archive Journal?")
                .setMessage("You will no longer be able to view or edit this journal")
                .setIcon(R.drawable.ic_publish_black_24px)

                .setPositiveButton("archive", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        dialog.dismiss();
                        archiveJournal();

                    }

                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
    }

    public void archiveJournal() {

        final String DET = dEndTime.getText().toString();
        final String NET = nEndTime.getText().toString();
//        final ParseFile file = new ParseFile(todaysDate+"_"+todaysTruck+".jpeg", image);

        publishPbar.setVisibility(View.VISIBLE);
        archive.setVisibility(View.GONE);

        final ParseQuery<DailyJournal> djQuery = ParseQuery.getQuery(DailyJournal.class);
        djQuery.whereEqualTo("objectId", currentJournalId);
        djQuery.setLimit(1);
        djQuery.findInBackground(new FindCallback<DailyJournal>() {
            @Override
            public void done(List<DailyJournal> list, ParseException e) {

                if (e == null) {

                    for (final DailyJournal dj : list) {

                        String endNotes = endOfDayNotes.getText().toString();
                        dj.setEndOfDayNotes(endNotes);
                        dj.setDriverEndTime(DET);
                        dj.setNavEndTime(NET);
//                        dj.setEndPhoto(file);

                        ParseQuery<NewJob> njQuery = ParseQuery.getQuery(NewJob.class);
                        njQuery.whereEqualTo("relatedJournal", currentJournalId);
                        njQuery.fromPin();
                        njQuery.findInBackground(new FindCallback<NewJob>() {
                            @Override
                            public void done(List<NewJob> list, ParseException e) {

                                if (e == null) {

                                    double total = 0.0;

                                    for (NewJob nj : list) {

                                        total = total + nj.getGrossSale();
                                        try {
                                            nj.unpin();
                                        } catch (ParseException e1) {
                                            e1.printStackTrace();
                                        }

                                    }

                                    int intTotal = (int) ((total / 1400) * 100);
                                    dj.setPercentOfGoal(intTotal);

                                    dj.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {

                                            if (e == null) {
                                                SharedPreferences.Editor editor = preferences.edit();
                                                editor.putString("universalJournalId", "none");
                                                editor.putString("driver", "noDriver");
                                                editor.putString("navigator", "noNavigator");
                                                editor.putString("truck", "noTruck");
                                                editor.putString("date", "noDate");
                                                editor.apply();
                                                Utils.hideKeyboard(getView(), getActivity());
                                                Toast.makeText(getActivity(), "Journal successfully archived",
                                                        Toast.LENGTH_SHORT).show();
                                                getActivity().finish();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Utils.showKeyboardInDialog(getDialog());
    }
}