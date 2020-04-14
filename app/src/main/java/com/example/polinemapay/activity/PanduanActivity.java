package com.example.polinemapay.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.polinemapay.R;

public class PanduanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panduan);

        TextView tt = (TextView) findViewById(R.id.toolbarText);
        tt.setText("Panduan");

        CardView Reg = (CardView) findViewById(R.id.PanReg);
        CardView TS = (CardView) findViewById(R.id.PanBuangSampah);
        CardView JS = (CardView) findViewById(R.id.PanJemSampah);
        CardView TP = (CardView) findViewById(R.id.PanTukPoin);
        CardView MR = (CardView) findViewById(R.id.PanRlwn);

        final LinearLayout IsReg = (LinearLayout) findViewById(R.id.isiReg);
        final LinearLayout IsTS = (LinearLayout) findViewById(R.id.isiBuangSampah);
        final LinearLayout IsJS = (LinearLayout) findViewById(R.id.isiJemSampah);
        final LinearLayout IsTP = (LinearLayout) findViewById(R.id.isiTukPoin);
        final LinearLayout IsMR = (LinearLayout) findViewById(R.id.isiRlwn);

        Reg.setOnClickListener(new View.OnClickListener() {
        int buka=0;
            public void onClick(View view) {
               if(buka==0){
                   IsReg.setVisibility(View.VISIBLE);
                   buka = 1;
               } else {
                   IsReg.setVisibility(View.GONE);
                   buka = 0;
               }
            }
        });

        TS.setOnClickListener(new View.OnClickListener() {
            int buka=0;
            public void onClick(View view) {
                if(buka==0){
                    IsTS.setVisibility(View.VISIBLE);
                    buka = 1;
                } else {
                    IsTS.setVisibility(View.GONE);
                    buka = 0;
                }
            }
        });

        JS.setOnClickListener(new View.OnClickListener() {
            int buka=0;
            public void onClick(View view) {
                if(buka==0){
                    IsJS.setVisibility(View.VISIBLE);
                    buka = 1;
                } else {
                    IsJS.setVisibility(View.GONE);
                    buka = 0;
                }
            }
        });

        TP.setOnClickListener(new View.OnClickListener() {
            int buka=0;
            public void onClick(View view) {
                if(buka==0){
                    IsTP.setVisibility(View.VISIBLE);
                    buka = 1;
                } else {
                    IsTP.setVisibility(View.GONE);
                    buka = 0;
                }
            }
        });

        MR.setOnClickListener(new View.OnClickListener() {
            int buka=0;
            public void onClick(View view) {
                if(buka==0){
                    IsMR.setVisibility(View.VISIBLE);
                    buka = 1;
                } else {
                    IsMR.setVisibility(View.GONE);
                    buka = 0;
                }
            }
        });
    }
}
