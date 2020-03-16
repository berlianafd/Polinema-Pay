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

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class DetailScannActivity extends AppCompatActivity{
    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;


    private TextView jnsSampah, brtSampah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailscan);

        jnsSampah = (TextView) findViewById(R.id.jenisSampah);
        brtSampah = (TextView) findViewById(R.id.beratSampah);

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

            String bs =(String) b.get("beratSampah");
            brtSampah.setText(bs + " g");
        }
    }

    /**
     * function to check id profile details in mysql db
     * */
    public void simpanTransaksiBuang(View view) {
        // Tag used to cancel the request
        String tag_string_req = "req_saveTrans";

		pDialog.setMessage("Memproses ...");
		showDialog();

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        final String id =(String) b.get("id");
        final String noMesin =(String) b.get("noMesin");
        final String jenisSampah =(String) b.get("jenisSampah");
        final String beratSampah =(String) b.get("beratSampah");
        Log.e(TAG, "Cek Id : " + id + jenisSampah + noMesin + beratSampah);

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
                        JSONObject user2 = jObj.getJSONObject("user");
                        String id = user2.getString("id");
                        String totalBeratSampah = user2.getString("totalBeratSampah");
                        String totalBeratKertas = user2.getString("totalBeratKertas");
                        String totalBeratPlastik = user2.getString("totalBeratPlastik");
                        String totalPoin = user2.getString("totalPoin");
                        Log.e(TAG, "Get Info : " + id + "," + totalBeratSampah +"," + totalBeratKertas +"," + totalBeratPlastik +"," + totalPoin );
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
