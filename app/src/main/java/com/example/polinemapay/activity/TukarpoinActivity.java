package com.example.polinemapay.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.polinemapay.R;
import com.example.polinemapay.app.AppConfig;
import com.example.polinemapay.app.AppController;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class TukarpoinActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private static final String TAG = TukarpoinActivity.class.getSimpleName();

    private ZXingScannerView ScannerView;
    private TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tukarpoin);

        //Init
        ScannerView = (ZXingScannerView)findViewById(R.id.zxscan);
        txtResult = (TextView)findViewById(R.id.txt_result);
        //Request permission
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        ScannerView.setResultHandler(TukarpoinActivity.this);
                        ScannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(TukarpoinActivity.this, "You mist accept this permission", Toast.LENGTH_SHORT).show();
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

    @Override
    public void handleResult(Result rawResult) {
        txtResult.setText(rawResult.getText());
        if (rawResult.getText().toString().length() >= 0) {
            String result = rawResult.getText();
            String[] splited = result.split("\\s+");

            String idPenjual = splited[0];
            String idYgDijual = splited[1];
            String harga = splited[2];

            //            Mengambil data id dan harga sampah dari Main
            Intent iin= getIntent();
            Bundle b = iin.getExtras();
            String idUser =(String) b.get("idUser");
            String konversi =(String) b.get("konversi");

            Double konv = Double.parseDouble(konversi);

            String poin =  Integer.toString(Integer.parseInt(harga)* (konv.intValue()));

            //            Mengirim data ke Detail
            Intent ii=new Intent(TukarpoinActivity.this, DetailTukarPoinActivity.class);
            ii.putExtra("idUser", idUser);
            ii.putExtra("idPenjual", idPenjual);
            ii.putExtra("idYgDijual", idYgDijual);
            ii.putExtra("harga",harga);
            ii.putExtra("poin",poin);
            startActivity(ii);
        }
    }
}
