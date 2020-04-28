package com.example.polinemapay.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class DetailTukarPoinActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;

    private TextView idPnjual, idPrdk, hrg, poinn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailtukarpoin);

        idPnjual = (TextView) findViewById(R.id.idPenjual);
        idPrdk = (TextView) findViewById(R.id.idProduk);
        hrg = (TextView) findViewById(R.id.idHarga);
        poinn = (TextView) findViewById(R.id.idPoinTukar);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            String iu =(String) b.get("idUser");
            String ip =(String) b.get("idPenjual");
            String iyd =(String) b.get("idYgDijual");
            String h =(String) b.get("harga");
            String p =(String) b.get("poin");

            idPnjual.setText(ip);
            idPrdk.setText(iyd);
            hrg.setText(h);
            poinn.setText(p);
        }
    }

    public void simpanTransaksiTukar(View view) {
        // Tag used to cancel the request
        String tag_string_req = "req_saveTransTukar";

        pDialog.setMessage("Memproses ...");
        showDialog();

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        final String id =(String) b.get("idUser");
        final String ip =(String) b.get("idPenjual");
        final String iyd =(String) b.get("idYgDijual");
        final String h =(String) b.get("harga");
        final String p =(String) b.get("poin");


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_TRANSAKSITUKAR, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Check save Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        JSONObject user3 = jObj.getJSONObject("user");
                        String id = user3.getString("id");
                        Log.e(TAG, "Get Info Transaction: " + id + "Sukses");
                        Toast.makeText(getApplicationContext(),
                                "Tukar Poinmu berhasil, Jangan lupa mengumpulkan poin lagi ya!", Toast.LENGTH_LONG).show();
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
                params.put("idUser", id);
                params.put("idPenjual", ip);
                params.put("idYgDijual", iyd);
                params.put("harga", h);
                params.put("poin", p);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        Intent intent = new Intent(DetailTukarPoinActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void tombolBatal(View view) {
        Intent intent = new Intent(DetailTukarPoinActivity.this, MainActivity.class);
        startActivity(intent);
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
