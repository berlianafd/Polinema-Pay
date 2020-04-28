package com.example.polinemapay.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.polinemapay.R;

public class DetailTugasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tugas);

        String idTugas = getIntent().getStringExtra("idTugas");
    }
}
