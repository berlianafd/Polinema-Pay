package com.example.polinemapay.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class DetailScannActivity extends AppCompatActivity{
    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;

    private TextView jnsSampah, brtSampah, harga, pSampah, tgl, waktu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailscan);

        jnsSampah = (TextView) findViewById(R.id.jenisSampah);
        harga = (TextView) findViewById(R.id.hrgSampah);
        brtSampah = (TextView) findViewById(R.id.beratSampah);
        pSampah = (TextView) findViewById(R.id.poinSampah);
        tgl = (TextView) findViewById(R.id.tanggalTS);
        waktu = (TextView) findViewById(R.id.waktuTS);

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
            String j =(String) b.get("jenisSampah");
            if(j.equals("1")){
                jnsSampah.setText("Kertas");
            } else{
                jnsSampah.setText("Plastik");
            }

            String hs =(String) b.get("hargasampah");
            String bs =(String) b.get("beratSampah");
            String ps =(String) b.get("poinSampah");

            String hargakg =  Double.toString(Double.parseDouble(hs) * 1000);
            harga.setText("Rp "+hargakg + " /kg");
            brtSampah.setText(bs + " g");
            pSampah.setText(ps+" poin");
        }
    }

    /**
     * function to save transaction in mysql db
     * */
    public void simpanTransaksiBuang(View view) {
        // Tag used to cancel the request
        String tag_string_req = "req_saveTrans";

		pDialog.setMessage("Memproses ...");
		showDialog();

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        final String id =(String) b.get("idUser");
        final String hargaSampah =(String) b.get("hargasampah");
        final String noMesin =(String) b.get("noMesin");
        final String jenisSampah =(String) b.get("jenisSampah");
        final String beratSampah =(String) b.get("beratSampah");
        final String poinSampah =(String) b.get("poinSampah");

        Log.e(TAG, "Cek : " + id + jenisSampah + noMesin + beratSampah + poinSampah);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_TRANSAKSIBUANG, new com.android.volley.Response.Listener<String>() {

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
                                "Tukar Sampahmu berhasil, poinmu telah bertambah !", Toast.LENGTH_LONG).show();
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
                params.put("noMesin", noMesin);
                params.put("jenisSampah", jenisSampah);
                params.put("beratSampah", beratSampah);
                params.put("poinSampah", poinSampah);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        Intent intent = new Intent(DetailScannActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void tombolBatal(View view) {
        Intent intent = new Intent(DetailScannActivity.this, MainActivity.class);
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
