package com.example.polinemapay.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.polinemapay.R;
import com.example.polinemapay.app.AppConfig;
import com.example.polinemapay.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailRiwayatTPActivity extends AppCompatActivity {
    private static final String TAG = DetailRiwayatTPActivity.class.getSimpleName();
    private ProgressDialog pDialog;

    private TextView idproduk, namapnjual, harga, poin, tgl, waktu;
    RelativeLayout support;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_riwayat_tp);

        idproduk = (TextView) findViewById(R.id.idProduk);
        harga = (TextView) findViewById(R.id.idHarga);
        namapnjual = (TextView) findViewById(R.id.idPenjual);
        poin = (TextView) findViewById(R.id.idPoinTukar);
        tgl = (TextView) findViewById(R.id.tanggalTP);
        waktu = (TextView) findViewById(R.id.waktuTP);
        support = (RelativeLayout) findViewById(R.id.button_support);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {

            String tgll =(String) b.get("tgl");
            String jamm =(String) b.get("jam");
            tgl.setText(tgll);
            waktu.setText(jamm);
            checkRiwayat(tgll, jamm);

        }
        support.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(DetailRiwayatTPActivity.this, BantuanAcitivity.class);
                startActivity(intent2);
            }
        }));
    }

    public void checkRiwayat(final String tanggal, final String waktu) {
        // Tag used to cancel the request
        String tag_string_req = "req_saveTrans";

        pDialog.setMessage("Memproses ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DETAILTUKARPOIN, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Check save Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        JSONObject user = jObj.getJSONObject("user");
                        String hr = user.getString("harga");
                        String p = user.getString("poin");
                        String np = user.getString("namaPenjual");
                        String nk = user.getString("namaKategori");
                        Log.e(TAG, "Get Info Transaction: Sukses");

                        harga.setText(hr);
                        poin.setText(p);
                        namapnjual.setText(np);
                        idproduk.setText(nk);

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
                Log.e(TAG, "Get Proccess Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("tgl", tanggal);
                params.put("jam", waktu);

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
