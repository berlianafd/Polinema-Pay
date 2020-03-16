package com.example.polinemapay.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.polinemapay.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class JemputActivity extends AppCompatActivity {

    private Spinner spinnerkec, spinnerkel;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    EditText txt_tgl, txt_jam;

    Button GetImageFromGalleryButton;
    ImageView ShowSelectedImage;
    EditText imageName;
    Bitmap FixBitmap;
    String ImageTag = "image_tag" ;
    String ImageName = "image_data" ;
    ProgressDialog progressDialog ;
    ByteArrayOutputStream byteArrayOutputStream ;
    byte[] byteArray ;
    String ConvertImage ;
    String GetImageNameFromEditText;
    HttpURLConnection httpURLConnection ;
    URL url;
    OutputStream outputStream;
    BufferedWriter bufferedWriter ;
    int RC ;
    BufferedReader bufferedReader ;
    StringBuilder stringBuilder;
    boolean check = true;
    private int GALLERY = 1, CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jemput);

        spinnerkec = (Spinner) findViewById(R.id.spinnerkec);
        txt_tgl = (EditText) findViewById(R.id.txt_tgl);
        txt_jam = (EditText) findViewById(R.id.txt_jam);
        GetImageFromGalleryButton = (Button)findViewById(R.id.buttonSelect);
        ShowSelectedImage = (ImageView)findViewById(R.id.imageView);

        byteArrayOutputStream = new ByteArrayOutputStream();

        GetImageFromGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPictureDialog();


            }
        });

        if (ContextCompat.checkSelfPermission(JemputActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                        5);
            }
        }

        addItemsOnSpinner0();
        spinnerkec.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        txt_tgl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(JemputActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        txt_jam.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(JemputActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedHour > 0 && selectedHour <10 ){
                            if (selectedMinute>0 && selectedMinute<10){
                                txt_jam.setText("0"+ selectedHour + ":" + "0"+ selectedMinute);
                            } else {
                                txt_jam.setText("0"+ selectedHour + ":" + selectedMinute);
                            }
                        } else {
                            if (selectedMinute>0 && selectedMinute<10){
                                txt_jam.setText(selectedHour + ":" + "0"+ selectedMinute);
                            } else {
                                txt_jam.setText(selectedHour + ":" + selectedMinute);
                            }
                        }
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

//        btn_get_datetime.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(JemputActivity.this,
//                        "Tanggal : " + txt_tgl.getText().toString() + "\n" +
//                                "Jam : " + txt_jam.getText().toString()
//                        , Toast.LENGTH_SHORT
//                ).show();
//            }
//        });
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Photo Gallery",
                "Camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    FixBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    // String path = saveImage(bitmap);
                    //Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    ShowSelectedImage.setImageBitmap(FixBitmap);
//                    UploadImageOnServerButton.setVisibility(View.VISIBLE);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(JemputActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            FixBitmap = (Bitmap) data.getExtras().get("data");
            ShowSelectedImage.setImageBitmap(FixBitmap);
//            UploadImageOnServerButton.setVisibility(View.VISIBLE);
            //  saveImage(thumbnail);
            //Toast.makeText(ShadiRegistrationPart5.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        txt_tgl.setText(sdf.format(myCalendar.getTime()));
    }

    private void addItemsOnSpinner1() {
        spinnerkel = (Spinner) findViewById(R.id.spinnerkel);
        List<String> list = new ArrayList<String>();
        list.add("Arjosari");
        list.add("Balearjosari");
        list.add("Blimbing");
        list.add("Bunulrejo");
        list.add("Jodipan");
        list.add("Kesatrian");
        list.add("Pandanwangi");
        list.add("Polehan");
        list.add("Polowijen");
        list.add("Purwantoro");
        list.add("Purwodadi");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerkel.setAdapter(dataAdapter);
    }

    private void addItemsOnSpinner2() {
        spinnerkel = (Spinner) findViewById(R.id.spinnerkel);
        List<String> list = new ArrayList<String>();
        list.add("Arjowinangun");
        list.add("Bumiayu");
        list.add("Buring");
        list.add("Cemorokandang");
        list.add("Kedungkandang");
        list.add("Kotalama");
        list.add("Lesanpuro");
        list.add("Madyopuro");
        list.add("Mergosono");
        list.add("Sawojajar");
        list.add("Tlogowaru");
        list.add("Wonokoyo");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerkel.setAdapter(dataAdapter);
    }

    private void addItemsOnSpinner3() {
        spinnerkel = (Spinner) findViewById(R.id.spinnerkel);
        List<String> list = new ArrayList<String>();
        list.add("Bareng");
        list.add("Gadingasri");
        list.add("Kasin");
        list.add("Kauman");
        list.add("Kiduldalem");
        list.add("Klojen");
        list.add("Oro-oro Dowo");
        list.add("Penanggungan");
        list.add("Rampal Celaket");
        list.add("Samaan");
        list.add("Sukoharjo");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerkel.setAdapter(dataAdapter);
    }

    private void addItemsOnSpinner4() {
        spinnerkel = (Spinner) findViewById(R.id.spinnerkel);
        List<String> list = new ArrayList<String>();
        list.add("Dinoyo");
        list.add("Jatimulyo");
        list.add("Ketawanggede");
        list.add("Lowokwaru");
        list.add("Merjosari");
        list.add("Mojolangu");
        list.add("Sumbersari");
        list.add("Tasikmadu");
        list.add("Tlogomas");
        list.add("Tulusrejo");
        list.add("Tunggulwulung");
        list.add("Tunjungsekar");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerkel.setAdapter(dataAdapter);
    }

    private void addItemsOnSpinner5() {
        spinnerkel = (Spinner) findViewById(R.id.spinnerkel);
        List<String> list = new ArrayList<String>();
        list.add("Bakalankrajan");
        list.add("Bandulan");
        list.add("Bandungrejosari");
        list.add("Ciptomulyo");
        list.add("Gadang");
        list.add("Karangbesuki");
        list.add("Kebonsari");
        list.add("Mulyorejo");
        list.add("Pisangcandi");
        list.add("Sukun");
        list.add("Tanjungrejo");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerkel.setAdapter(dataAdapter);
    }

    private void addItemsOnSpinner0() {
        spinnerkel = (Spinner) findViewById(R.id.spinnerkel);
        spinnerkel.setEnabled(false);
        List<String> list = new ArrayList<String>();
        list.add("Pilih Kelurahan");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerkel.setAdapter(dataAdapter);
    }

    private class CustomOnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if (parent.getItemAtPosition(pos).toString().equals("Blimbing")) {
                spinnerkel.setEnabled(true);
                addItemsOnSpinner1();
            } else if (parent.getItemAtPosition(pos).toString().equals("Kedungkandang")) {
                spinnerkel.setEnabled(true);
                addItemsOnSpinner2();
            } else if (parent.getItemAtPosition(pos).toString().equals("Klojen")) {
                spinnerkel.setEnabled(true);
                addItemsOnSpinner3();
            }else if (parent.getItemAtPosition(pos).toString().equals("Lowokwaru")) {
                spinnerkel.setEnabled(true);
                addItemsOnSpinner4();
            }else if (parent.getItemAtPosition(pos).toString().equals("Sukun")) {
                spinnerkel.setEnabled(true);
                addItemsOnSpinner5();
            } else {
                addItemsOnSpinner0();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera

            }
            else {

                Toast.makeText(JemputActivity.this, "Unable to use Camera..Please Allow us to use Camera", Toast.LENGTH_LONG).show();

            }
        }
    }
}
