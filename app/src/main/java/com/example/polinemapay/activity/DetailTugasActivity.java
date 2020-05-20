package com.example.polinemapay.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.polinemapay.R;
import com.example.polinemapay.app.AppConfig;
import com.example.polinemapay.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailTugasActivity extends AppCompatActivity {
    private static final String TAG = DetailTugasActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    ImageView sms, wa, call;
    TextView nama, alamt, kec, kel, tgl, waktuu, perkiraannBS, namaAcr;
    String number="";
    Button selesai;
    LinearLayout callUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tugas);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        final String idTugas = getIntent().getStringExtra("idTugas");
        checkDetailJemputSampah(idTugas);

        sms = (ImageView) findViewById(R.id.sms_button);
        wa = (ImageView) findViewById(R.id.wa_button);
        call = (ImageView) findViewById(R.id.call_button);
        selesai = (Button) findViewById(R.id.selesai_button);
        nama = (TextView) findViewById(R.id.namaPJS);
        alamt = (TextView) findViewById(R.id.almatPJS);
        kec = (TextView) findViewById(R.id.kecamatanPJS);
        kel = (TextView) findViewById(R.id.kelurahanPJS);
        tgl = (TextView) findViewById(R.id.tanggalPJS);
        waktuu = (TextView) findViewById(R.id.waktuPJS);
        perkiraannBS = (TextView) findViewById(R.id.perkiraanPJS);
        namaAcr = (TextView) findViewById(R.id.namaAcaraPJS);
        callUser = (LinearLayout) findViewById(R.id.callUser);
        TextView tt = (TextView) findViewById(R.id.toolbarText);
        tt.setText("Tugas");

        sms.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", number);
                startActivity(smsIntent);

            }
        });

        wa.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                String url = "https://api.whatsapp.com/send?phone=" + number;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        call.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getApplication(),
                        Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    //Creating intents for making a call
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+number));
                    getApplication().startActivity(callIntent);

                }else{
                    Toast.makeText(getApplication(), "You don't assign permission.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        selesai.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                updateSelesaiPesanan(idTugas);
            }
        });

    }

    private void checkDetailJemputSampah(final String idTugas) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Loading...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DETAILJEMPUTSAMPAH, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("nama");
                        String nohp = user.getString("nohp");
                        String namaAcara = user.getString("namaAcara");
                        String alamat = user.getString("alamat");
                        String kelurahan = user.getString("kelurahan");
                        String kecamatan = user.getString("kecamatan");
                        String tanggal = user.getString("tanggal");
                        String waktu = user.getString("waktu");
                        String status = user.getString("status");
                        String perkiraanBS = user.getString("perkiraanBeratSampah");

                        number=nohp;
                        nama.setText(name);
                        namaAcr.setText(namaAcara);
                        alamt.setText(alamat);
                        kec.setText("Kecamatan "+kecamatan);
                        kel.setText("Kelurahan "+kelurahan);
                        tgl.setText(tanggal);
                        waktuu.setText(waktu + "WIB");
                        perkiraannBS.setText(perkiraanBS +"Kg");

                        if(status.equals("selesai")){
                            selesai.setVisibility(View.GONE);
                            callUser.setVisibility(View.GONE);
                        }

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
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("idTugas", idTugas);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void updateSelesaiPesanan(final String idPesanan) {
        // Tag used to cancel the request
        String tag_string_req = "req_updatestatuspesanan";

//        pDialog.setMessage("Sedang memuat...");
//        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATESELESAIJEMPUT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Check Update Status Pesanan: " + response.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        JSONObject user = jObj.getJSONObject("user");
                        String status = user.getString("status");

                        Log.e("Hasil", status + idPesanan);

                        Toast.makeText(getApplicationContext(),
                                "Pesanan Jemput Sampah telah selesai!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intent);
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
                params.put("idPesanan", idPesanan);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
