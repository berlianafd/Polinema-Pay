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
            String fitur = splited[3];

            if (fitur.equals("TS")) {

//            Mengambil data id dan harga sampah dari Main
                Intent iin = getIntent();
                Bundle b = iin.getExtras();
                String idUser = (String) b.get("idUser");

                if (jenisSampah.equals("1")) {
                    String hargasampah = (String) b.get("hargakertas");
                    int poinInt = (int) (Double.parseDouble(beratSampah) * Double.parseDouble(hargasampah));
                    String poin = Integer.toString(poinInt);

                    //            Mengirim data ke Detail
                    Intent ii = new Intent(ScannActivity.this, DetailScannActivity.class);
                    ii.putExtra("idUser", idUser);
                    ii.putExtra("hargasampah", hargasampah);
                    ii.putExtra("noMesin", noMesin);
                    ii.putExtra("jenisSampah", jenisSampah);
                    ii.putExtra("beratSampah", beratSampah);
                    ii.putExtra("poinSampah", poin);
                    startActivity(ii);
                } else if (jenisSampah.equals("2")) {
                    String hargasampah = (String) b.get("hargaplastik");
                    String poin = Double.toString(Double.parseDouble(beratSampah) * Double.parseDouble(hargasampah));
                    //            Mengirim data ke Detail
                    Intent ii = new Intent(ScannActivity.this, DetailScannActivity.class);
                    ii.putExtra("idUser", idUser);
                    ii.putExtra("hargasampah", hargasampah);
                    ii.putExtra("noMesin", noMesin);
                    ii.putExtra("jenisSampah", jenisSampah);
                    ii.putExtra("beratSampah", beratSampah);
                    ii.putExtra("poinSampah", poin);
                    startActivity(ii);
                } else {
                    Toast.makeText(getApplicationContext(), "Kode transaksi tidak ditemukan!", Toast.LENGTH_LONG).show();
                }

            } else{
                Toast.makeText(getApplicationContext(), "Kode transaksi tidak ditemukan!", Toast.LENGTH_LONG).show();
            }

        }
    }
}
