package com.example.polinemapay.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.polinemapay.R;
import com.example.polinemapay.app.AppConfig;
import com.example.polinemapay.app.AppController;
import com.example.polinemapay.helper.SQLiteHandler;
import com.example.polinemapay.helper.SessionManager;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final String TAG = MainActivity.class.getSimpleName();
    private SQLiteHandler db;

    private ZXingScannerView ScannerView;
    private String noMesin, jenisSampah, beratSampah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        //Init
        ScannerView = (ZXingScannerView)findViewById(R.id.zxscan);
        //Request permission
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        ScannerView.setResultHandler(ScannActivity.this);
                        ScannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(ScannActivity.this, "You mist accept this permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();
    }

    @Override
    protected void onDestroy() {
        ScannerView.stopCamera();
        super.onDestroy();
    }

    //    idTransaksiSampah, jam, tanggal, noMesin, jenisSampah, beratSampah
    @Override
    public void handleResult(Result rawResult) {

        if (rawResult.getText().toString().length() >= 0) {
            // SqLite database handler
            db = new SQLiteHandler(getApplicationContext());

            // Fetching user details from SQLite
            HashMap<String, String> user = db.getUserDetails();
            String name = user.get("name");
            String nohp = user.get("nohp");

            String result = rawResult.getText();
            String[] splited = result.split("\\s+");

            noMesin = splited[0];
            jenisSampah = splited[1];
            beratSampah = splited[2];

//            Mengambil data id dan harga sampah dari Main
            Intent iin= getIntent();
            Bundle b = iin.getExtras();
            String idUser =(String) b.get("idUser");

            if(jenisSampah.equals("1")){
                 String hargasampah =(String) b.get("hargakertas");
                 String poin = Double.toString(Double.parseDouble(beratSampah)*Double.parseDouble(hargasampah));

                //            Mengirim data ke Detail
                Intent ii=new Intent(ScannActivity.this, DetailScannActivity.class);
                ii.putExtra("idUser", idUser);
                ii.putExtra("hargasampah", hargasampah);
                ii.putExtra("noMesin",noMesin);
                ii.putExtra("jenisSampah",jenisSampah);
                ii.putExtra("beratSampah",beratSampah);
                ii.putExtra("poinSampah",poin);
                startActivity(ii);
            }else{
                 String hargasampah =(String) b.get("hargaplastik");
                 String poin = Double.toString(Double.parseDouble(beratSampah)*Double.parseDouble(hargasampah));
                //            Mengirim data ke Detail
                Intent ii=new Intent(ScannActivity.this, DetailScannActivity.class);
                ii.putExtra("idUser", idUser);
                ii.putExtra("hargasampah", hargasampah);
                ii.putExtra("noMesin",noMesin);
                ii.putExtra("jenisSampah",jenisSampah);
                ii.putExtra("beratSampah",beratSampah);
                ii.putExtra("poinSampah",poin);
                startActivity(ii);
            }


        }
    }

    /**
     * function to check id profile details in mysql db
     * */
    private void checkUserId(final String nohp, final String name) {
        // Tag used to cancel the request
        String tag_string_req = "req_checkIdUser";

//		pDialog.setMessage("Checkin in ...");
//		showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CEKIDUSER, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Check Id Response: " + response.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        JSONObject user = jObj.getJSONObject("user");
                        String id = user.getString("id");
                        Log.e(TAG, "Get Id : " + id + jenisSampah + noMesin + beratSampah);



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
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Get Id Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("nohp", nohp);
                params.put("name", name);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
