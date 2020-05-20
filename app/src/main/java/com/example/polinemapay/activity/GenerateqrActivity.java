package com.example.polinemapay.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polinemapay.R;
import com.example.polinemapay.activity.Generateqrcode.Category;
import com.example.polinemapay.activity.Generateqrcode.ServiceHandler;
import com.google.zxing.WriterException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidx.appcompat.app.AppCompatActivity;

public class GenerateqrActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String TAG = "GenerateQRCode";
    EditText edtValue;
    ImageView qrImage;
    Button start, save;
    String inputValue, selectKategori;
    String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    private Spinner spinnerKtgr;
    // array list for spinner adapter
    private ArrayList<Category> categoriesList;
    ProgressDialog pDialog;

    Dialog myDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generateqr);
        myDialog = new Dialog(this);

        qrImage = (ImageView) findViewById(R.id.QR_Image);
        edtValue = (EditText) findViewById(R.id.edt_value);
        spinnerKtgr = (Spinner) findViewById(R.id.spinKategori);
        start = (Button) findViewById(R.id.start);
        TextView tt = (TextView) findViewById(R.id.toolbarText);
        tt.setText("Transaksi");

        categoriesList = new ArrayList<Category>();

        // spinner item select listener
        spinnerKtgr.setOnItemSelectedListener(this);

        new GetCategories().execute();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iin = getIntent();
                Bundle b = iin.getExtras();
                String idUser = (String) b.get("idUser");

                inputValue = idUser +" "+selectKategori+" "+edtValue.getText().toString().trim()+" TP";
                if (inputValue.length() > 0) {
                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;

                    qrgEncoder = new QRGEncoder(
                            inputValue, null,
                            QRGContents.Type.TEXT,
                            smallerDimension);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        qrImage.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        Log.v(TAG, e.toString());
                    }
                } else {
                    edtValue.setError("Required");
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String result = adapterView.getItemAtPosition(i).toString();
        String[] splited = result.split("\\s+");

        selectKategori = splited[0];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void ShowPopupTambahKategori(View v) {
        final EditText  txtCategory;
        Button btnAddNewCategory;
        myDialog.setContentView(R.layout.popuptambahkategori);


        txtCategory = (EditText) myDialog.findViewById(R.id.editTambahKategori);
        btnAddNewCategory = (Button) myDialog.findViewById(R.id.btnAddNewCategory);

        btnAddNewCategory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (txtCategory.getText().toString().trim().length() > 0) {

                    // new category name
                    String newCategory = txtCategory.getText().toString();

                    // Call Async task to create new category
                    new AddNewCategory().execute(newCategory);
                    myDialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Masukkan Kategori!", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    /**
     * Async task to get all food categories
     * */
    private class GetCategories extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(GenerateqrActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler jsonParser = new ServiceHandler();

            Intent iin = getIntent();
            Bundle b = iin.getExtras();
            String idUser = (String) b.get("idUser");
            String URL_CATEGORIES = "https://www.polinema-pay.online/android/GetKategori.php?idUser=" + idUser;
            String json = jsonParser.makeServiceCall(URL_CATEGORIES, ServiceHandler.GET);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        categoriesList.clear();
                        JSONArray categories = jsonObj
                                .getJSONArray("categories");

                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject catObj = (JSONObject) categories.get(i);
                            Category cat = new Category(catObj.getInt("idKategori"),
                                    catObj.getString("namaKategori"));
                            categoriesList.add(cat);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            populateSpinner();
        }

    }

    /**
     * Adding spinner data
     * */
    private void populateSpinner() {
        List<String> lables = new ArrayList<String>();

//        txtCategory.setText("");

        for (int i = 0; i < categoriesList.size(); i++) {
            lables.add(categoriesList.get(i).getId() + " - " +categoriesList.get(i).getName());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerKtgr.setAdapter(spinnerAdapter);
    }

    /**
     * Async task to create a new food category
     * */
    private class AddNewCategory extends AsyncTask<String, Void, Void> {

        boolean isNewCategoryCreated = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(GenerateqrActivity.this);
            pDialog.setMessage("Menambah Kategori...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(String... arg) {

            String newCategory = arg[0];

            // Preparing post params
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", newCategory));

            Intent iin = getIntent();
            Bundle b = iin.getExtras();
            String idUser = (String) b.get("idUser");
            String URL_NEW_CATEGORY = "https://www.polinema-pay.online/android/NewKategori.php?idUser=" + idUser;
            ServiceHandler serviceClient = new ServiceHandler();

            String json = serviceClient.makeServiceCall(URL_NEW_CATEGORY,
                    ServiceHandler.POST, params);

            Log.d("Create Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    boolean error = jsonObj.getBoolean("error");
                    // checking for error node in json
                    if (!error) {
                        // new category created successfully
                        isNewCategoryCreated = true;
                    } else {
                        Log.e("Tambah Kategori Error: ", "> " + jsonObj.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (isNewCategoryCreated) {
                Toast.makeText(getApplicationContext(),
                        "Kategori berhasil ditambah!", Toast.LENGTH_SHORT)
                        .show();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // fetching all categories
                        new GetCategories().execute();
                    }
                });
            }
        }

    }
}
