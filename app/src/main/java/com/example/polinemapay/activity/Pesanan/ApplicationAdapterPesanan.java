package com.example.polinemapay.activity.Pesanan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.polinemapay.R;
import com.example.polinemapay.activity.MainActivity;
import com.example.polinemapay.activity.ProfilActivity;
import com.example.polinemapay.app.AppConfig;
import com.example.polinemapay.app.AppController;
import com.example.polinemapay.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ApplicationAdapterPesanan extends ArrayAdapter<ApplicationPesanan> {
    private static final String TAG = ApplicationAdapterPesanan.class.getSimpleName();
    private List<ApplicationPesanan> items;
    private SQLiteHandler db;
    String idUser;
    private ProgressDialog dialog;

    public ApplicationAdapterPesanan(Context context, List<ApplicationPesanan> items) {
        super(context, R.layout.content_main_pesanan, items);
        this.items = items;
        // SqLite database handler
        db = new SQLiteHandler(getContext());

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        idUser = user.get("id");
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View v = convertView;

        if(v == null) {
            LayoutInflater li = LayoutInflater.from(getContext());
            v = li.inflate(R.layout.content_main_pesanan, null);
        }

        ApplicationPesanan app = items.get(position);

        if(app != null) {
            final TextView idPesanann = (TextView)v.findViewById(R.id.idPesanan);
            TextView namaacara = (TextView)v.findViewById(R.id.namaAcaraPesanan);
            TextView tglacara = (TextView)v.findViewById(R.id.tanggalJemputPesanan);
            TextView wktacara = (TextView)v.findViewById(R.id.waktuJemputPesanan);
            TextView tmptacara = (TextView)v.findViewById(R.id.alamatJemputPesanan);
            TextView perkiraanBS = (TextView)v.findViewById(R.id.perkiraanBS);
            Button buttonTerima = (Button) v.findViewById(R.id.terimaPesanan);

            if(idPesanann != null) idPesanann.setText(app.getIdPesanan());
            if(namaacara != null) namaacara.setText(app.getNamaAcara());
            if(tglacara != null) tglacara.setText(app.getTanggalJemput());
            if(wktacara != null) wktacara.setText(app.getWaktuJemput());
            if(tmptacara != null) tmptacara.setText(app.getAlamatJemput());
            if(perkiraanBS != null) perkiraanBS.setText(app.getPerkiraanBeratSampah()+" Kg");

            buttonTerima.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ((ListView) parent).performItemClick(convertView, position, 0); // Let the event be handled in onItemClick()
//                    Toast.makeText(getContext(), items.get(position).getIdPesanan() + "," + idUser, Toast.LENGTH_LONG).show();
                    updateStatusPesanan(items.get(position).getIdPesanan());

                }
            }));
        }
        return v;
    }

    private void updateStatusPesanan(final String idPesanan) {
        // Tag used to cancel the request
        String tag_string_req = "req_updatestatuspesanan";

//        pDialog.setMessage("Sedang memuat...");
//        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATESTATUSJEMPUT, new Response.Listener<String>() {

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
                        String idPesanan = user.getString("idPesanan");

                        Log.e("Hasil", status + idPesanan);

                        Toast.makeText(getContext(),
                                "Pesanan Jemput Sampah berhasil diterima!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(intent);
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Get Id Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
//				hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("idPesanan", idPesanan);
                params.put("idUser", idUser);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}