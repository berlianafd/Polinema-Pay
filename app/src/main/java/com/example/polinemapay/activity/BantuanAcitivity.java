package com.example.polinemapay.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.polinemapay.R;

public class BantuanAcitivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bantuan_acitivity);

        CardView send = (CardView) findViewById(R.id.sendEmail);
        TextView tt = (TextView) findViewById(R.id.toolbarText);
        tt.setText("Bantuan");

        send.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "cs@polinema-pay.online", null));
                startActivity(intent);
                finish();

            }
        });
    }
}
