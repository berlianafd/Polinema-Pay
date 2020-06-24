package com.example.polinemapay.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.polinemapay.R;
import com.example.polinemapay.app.AppConfig;
import com.example.polinemapay.app.AppController;
import com.example.polinemapay.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetailTukarPoinActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;

    private TextView idPnjual, idPrdk, hrg, poinn, tgl, waktu, poinUser;
    Button bayar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailtukarpoin);

        poinUser = (TextView) findViewById(R.id.poinsaatini);
        idPnjual = (TextView) findViewById(R.id.idPenjual);
        idPrdk = (TextView) findViewById(R.id.idProduk);
        hrg = (TextView) findViewById(R.id.idHarga);
        poinn = (TextView) findViewById(R.id.idPoinTukar);
        tgl = (TextView) findViewById(R.id.tanggalTP);
        waktu = (TextView) findViewById(R.id.waktuTP);
        bayar = (Button) findViewById(R.id.btnBayar);

//        set tgl dan waktu
        Date HariSekarang = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("dd-MM-yyyy");
        SimpleDateFormat fw = new SimpleDateFormat ("hh:mm:ss");
        tgl.setText(ft.format(HariSekarang));
        waktu.setText(fw.format(HariSekarang));

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            String iu =(String) b.get("idUser");
            String pu =(String) b.get("poinUser");
            String ip =(String) b.get("idPenjual");
            String iyd =(String) b.get("idYgDijual");
            String npj =(String) b.get("namaPenjual");
            String npk =(String) b.get("namaProduk");
            String h =(String) b.get("harga");
            String p =(String) b.get("poin");

            idPnjual.setText(npj);
            idPrdk.setText(npk);
            poinUser.setText(pu);
            hrg.setText(h);
            poinn.setText(p);
        }

        bayar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
               if (Integer.parseInt(poinn.getText().toString())>Integer.parseInt(poinUser.getText().toString())){
                    Toast.makeText(getApplicationContext(),
                            "Poin Anda tidak mencukupi untuk transaksi ini!", Toast.LENGTH_LONG).show();
                }else{
                    simpanTransaksiTukar(waktu.getText().toString());
                }
            }

        });
    }

    public void simpanTransaksiTukar(final String waktuu) {
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

                SessionManager sesion  = new SessionManager(getApplicationContext());
                params.put("idUser", id);
                params.put("idPenjual", ip);
                params.put("idYgDijual", iyd);
                params.put("harga", h);
                params.put("poin", p);
                params.put("jam", waktuu);
                params.put("jwtToken", sesion.getSessionJwtToken());

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

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
