package com.example.polinemapay.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.polinemapay.R;
import com.example.polinemapay.app.AppConfig;
import com.example.polinemapay.app.AppController;
import com.example.polinemapay.helper.SQLiteHandler;
import com.example.polinemapay.helper.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import static android.icu.text.RelativeDateTimeFormatter.AbsoluteUnit.NOW;


public class ProfilActivity extends AppCompatActivity {
    private static final String TAG = ProfilActivity.class.getSimpleName();
    String GetImageNameFromEditText, namee, nohpp, image_pathh, image_namee;
    String ImageTag = "image_tag";
    String ImageName = "image_data";

    Dialog myDialog;

    FloatingActionButton GetImageFromGalleryButton;
    ImageView ShowSelectedImage;
    Bitmap FixBitmap;
    ByteArrayOutputStream byteArrayOutputStream ;
    private int GALLERY = 1, CAMERA = 2;

    ProgressDialog progressDialog;
    byte[] byteArray ;

    HttpURLConnection httpURLConnection ;
    URL url;
    OutputStream outputStream;
    BufferedWriter bufferedWriter ;
    int RC ;
    BufferedReader bufferedReader ;
    StringBuilder stringBuilder;
    boolean check = true;
    String ConvertImage, idUser ;

    private TextView txtName;
    private TextView txtnohp;
    private String nama;
    private String notelpp;

    private SQLiteHandler db;
    private SessionManager session;
    private EditText imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        myDialog = new Dialog(this);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageName = (EditText) findViewById(R.id.imageName);
        GetImageFromGalleryButton = (FloatingActionButton) findViewById(R.id.buttonSelect);
        ShowSelectedImage = (ImageView)findViewById(R.id.imageView);

        txtName = (TextView) findViewById(R.id.namaProfil);
        txtnohp = (TextView) findViewById(R.id.nohpProfil);
        TextView tt = (TextView) findViewById(R.id.toolbarText);
        tt.setText("Profil");

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        byteArrayOutputStream = new ByteArrayOutputStream();

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        idUser =(String) b.get("idUser");

        checkUserId();

        GetImageFromGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(ProfilActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                                5);
                    }
                }

                showPictureDialog();
            }
        });
    }

    public void ShowPopupName(View v) {
        final EditText  updtNama;
        Button update;
        myDialog.setContentView(R.layout.popup_nameprofil);

        updtNama = (EditText) myDialog.findViewById(R.id.updateNamaProfil);
        update = (Button) myDialog.findViewById(R.id.btnUpdateNameProfil);

        updtNama.setText(namee);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNamaProfil(updtNama.getText().toString());
                checkUserId();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void ShowPopupNohp(View v) {
        final EditText updtNohp;
        Button update;
        myDialog.setContentView(R.layout.popup_nohpprofil);

        updtNohp = (EditText) myDialog.findViewById(R.id.updateNohpProfil);
        update = (Button) myDialog.findViewById(R.id.btnUpdateNohpProfil);

        updtNohp.setText(nohpp);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNohpProfil(updtNohp.getText().toString());
                checkUserId();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void ShowPopupPoto(View v) {
        final ImageView img;

        myDialog.setContentView(R.layout.popup_fotoprofil);

        img = (ImageView) myDialog.findViewById(R.id.potoview);

        byte[] rawImage = Base64.decode(image_pathh, Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length);
        img.setImageBitmap(bmp);

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void uploadToServer() {
        FixBitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
        byteArray = byteArrayOutputStream.toByteArray();
        ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(ProfilActivity.this,"Sedang Memproses...","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {
                super.onPostExecute(string1);
                progressDialog.dismiss();
                Toast.makeText(ProfilActivity.this,string1,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                ProfilActivity.ImageProcessClass imageProcessClass = new ProfilActivity.ImageProcessClass();
                HashMap<String,String> HashMapParams = new HashMap<String,String>();
                HashMapParams.put("idUser", idUser);
                HashMapParams.put(ImageTag, GetImageNameFromEditText);
                HashMapParams.put(ImageName, ConvertImage);
                String FinalData = imageProcessClass.ImageHttpRequest("https://www.polinema-pay.online/android/ProfilUser.php", HashMapParams);
                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass{
        public String ImageHttpRequest(String requestURL,HashMap<String, String>PData) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                url = new URL(requestURL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(20000);
                httpURLConnection.setConnectTimeout(20000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                outputStream = httpURLConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(
                        new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.write(bufferedWriterDataFN(PData));
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                RC = httpURLConnection.getResponseCode();
                if (RC == HttpsURLConnection.HTTP_OK) {
                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    stringBuilder = new StringBuilder();
                    String RC2;
                    while ((RC2 = bufferedReader.readLine()) != null){
                        stringBuilder.append(RC2);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {
            stringBuilder = new StringBuilder();
            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilder.append("&");
                stringBuilder.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
                stringBuilder.append("=");
                stringBuilder.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }
            return stringBuilder.toString();
        }
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
                    ShowSelectedImage.setImageBitmap(FixBitmap);
                    uploadToServer();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfilActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            FixBitmap = (Bitmap) data.getExtras().get("data");
            ShowSelectedImage.setImageBitmap(FixBitmap);
            uploadToServer();
        }
    }

    private void checkUserId() {
        // Tag used to cancel the request
        String tag_string_req = "req_checkUserProfile";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CEKUSERPROFILE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Check User Profile Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        JSONObject user = jObj.getJSONObject("user");
                        idUser = user.getString("id");
                        namee = user.getString("nama");
                        nohpp = user.getString("nohp");
                        image_pathh = user.getString("image_path");
                        image_namee = user.getString("image_name");
                        Log.e(TAG, "Get Id : " + idUser);

                        byte[] rawImage = Base64.decode(image_pathh, Base64.DEFAULT);
                        Bitmap bmp = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length);
                        ShowSelectedImage.setImageBitmap(bmp);

                        // Displaying the user details on the screen
                        txtName.setText(namee);
                        txtnohp.setText(nohpp);

                        //        Set nama foto profil
                        ProfilActivity tgl = new ProfilActivity();
                        nama = txtName.getText().toString();
                        String nm = nama.replaceAll("\\s+", "-");
                        imageName.setText(idUser + "_" + nm +  "_" + tgl.getTanggal() +  "_" + tgl.getWaktu());
                        GetImageNameFromEditText = imageName.getText().toString();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Get Id Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
//				hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("idUser", idUser);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void updateNamaProfil(final String nama) {
        // Tag used to cancel the request
        String tag_string_req = "req_updateNamaProfil";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATENAMAPROFIL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Check update name Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        JSONObject user = jObj.getJSONObject("user");

                        // Displaying the user details on the screen
                        txtName.setText(namee);

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Get Id Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
//				hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("idUser", idUser);
                params.put("name", nama);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void updateNohpProfil(final String nohp) {
        // Tag used to cancel the request
        String tag_string_req = "req_updateNohpProfil";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATENOHPPROFIL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Check Id Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        JSONObject user = jObj.getJSONObject("user");

                        // Displaying the user details on the screen
                        txtnohp.setText(nohpp);

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
//                        Toast.makeText(getApplicationContext(),
//                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Get Id Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
//				hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("idUser", idUser);
                params.put("nohp", nohp);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private String getTanggal() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getWaktu() {
        DateFormat dateFormat = new SimpleDateFormat("HH-mm-ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(ProfilActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
