package com.example.polinemapay.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.example.polinemapay.R;
import com.example.polinemapay.app.AppConfig;
import com.example.polinemapay.app.AppController;
import com.example.polinemapay.helper.SQLiteHandler;
import com.example.polinemapay.helper.SessionManager;

public class RegisterActivity extends Activity {

    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputNohp;
    private TextView inputImei;
    private RadioGroup level;
    private RadioButton levelUser, levelSukarelawan;


    String IMEINumber, name, kodeLevel, imei, nohp;
    private static final int REQUEST_CODE = 101;
    TelephonyManager telephonyManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFullName = (EditText) findViewById(R.id.name);
        inputNohp = (EditText) findViewById(R.id.nohp);
        inputImei = (TextView) findViewById(R.id.imei);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        level = (RadioGroup) findViewById(R.id.level);
        levelUser = (RadioButton) findViewById(R.id.user);
        levelSukarelawan = (RadioButton) findViewById(R.id.sukarelawan);

        inputImei.setVisibility(View.GONE);




        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //ambil IMEI
                telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE);
                    return;
                }
                IMEINumber = telephonyManager.getDeviceId();

                inputImei.setText(IMEINumber);

                int selectedId = level.getCheckedRadioButtonId();
                kodeLevel="level";

                if (selectedId == levelUser.getId()){
                    kodeLevel = levelUser.getText().toString();

                } else if (selectedId == levelSukarelawan.getId()) {
                    kodeLevel = levelSukarelawan.getText().toString();
                }

                name = inputFullName.getText().toString().trim();
                nohp = "+62"+inputNohp.getText().toString().trim();
                imei = inputImei.getText().toString().trim();

                if (!name.isEmpty() && !nohp.isEmpty() && !imei.isEmpty() && selectedId!=-1) {
                    // Launch main activity
                    Intent intent = new Intent(RegisterActivity.this,
                            VerifyPhoneRegisterActivity.class);
                    intent.putExtra("mobile", nohp);
                    intent.putExtra("name", name);
                    intent.putExtra("imei", imei);
                    intent.putExtra("kodeLevel", kodeLevel);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Tidak boleh ada yang kosong!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(RegisterActivity.this, "Permission granted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Permission denied.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
