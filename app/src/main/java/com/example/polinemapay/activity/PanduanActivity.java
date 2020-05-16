package com.example.polinemapay.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.polinemapay.R;
import com.example.polinemapay.helper.SQLiteHandler;

import java.util.HashMap;

public class PanduanActivity extends AppCompatActivity {
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panduan);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        TextView tt = (TextView) findViewById(R.id.toolbarText);
        tt.setText("Panduan");
        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        String level = user.get("level");

        CardView Reg = (CardView) findViewById(R.id.PanReg);
        CardView TS = (CardView) findViewById(R.id.PanBuangSampah);
        CardView JS = (CardView) findViewById(R.id.PanJemSampah);
        CardView TP = (CardView) findViewById(R.id.PanTukPoin);
        CardView MR = (CardView) findViewById(R.id.PanRlwn);
        CardView Trans = (CardView) findViewById(R.id.PanTrans);
        CardView TPP = (CardView) findViewById(R.id.PanTukPoinPenjual);
        CardView PSN = (CardView) findViewById(R.id.PanPesanan);
        CardView TGS = (CardView) findViewById(R.id.PanTugass);

        final LinearLayout IsReg = (LinearLayout) findViewById(R.id.isiReg);
        final LinearLayout IsTS = (LinearLayout) findViewById(R.id.isiBuangSampah);
        final LinearLayout IsJS = (LinearLayout) findViewById(R.id.isiJemSampah);
        final LinearLayout IsTP = (LinearLayout) findViewById(R.id.isiTukPoin);
        final LinearLayout IsMR = (LinearLayout) findViewById(R.id.isiRlwn);
        final LinearLayout IsTrans = (LinearLayout) findViewById(R.id.isiTrans);
        final LinearLayout IsTPP = (LinearLayout) findViewById(R.id.isiTukPoinPenjual);
        final LinearLayout IsPSN = (LinearLayout) findViewById(R.id.isiPesanan);
        final LinearLayout IsTGS = (LinearLayout) findViewById(R.id.isiTugass);

        if(level.equals("Relawan")){
            TS.setVisibility(View.GONE);
            JS.setVisibility(View.GONE);
            TP.setVisibility(View.GONE);
            MR.setVisibility(View.GONE);
            Trans.setVisibility(View.GONE);
            TPP.setVisibility(View.GONE);
            PSN.setVisibility(View.VISIBLE);
            TGS.setVisibility(View.VISIBLE);

        } else if (level.equals("User")){
            TS.setVisibility(View.VISIBLE);
            JS.setVisibility(View.VISIBLE);
            TP.setVisibility(View.VISIBLE);
            MR.setVisibility(View.VISIBLE);
            Trans.setVisibility(View.GONE);
            TPP.setVisibility(View.GONE);
            PSN.setVisibility(View.GONE);
            TGS.setVisibility(View.GONE);
        } else {
            TS.setVisibility(View.GONE);
            JS.setVisibility(View.GONE);
            TP.setVisibility(View.GONE);
            MR.setVisibility(View.VISIBLE);
            Trans.setVisibility(View.VISIBLE);
            TPP.setVisibility(View.VISIBLE);
            PSN.setVisibility(View.GONE);
            TGS.setVisibility(View.GONE);
        }

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
        Trans.setOnClickListener(new View.OnClickListener() {
            int buka=0;
            public void onClick(View view) {
                if(buka==0){
                    IsTrans.setVisibility(View.VISIBLE);
                    buka = 1;
                } else {
                    IsTrans.setVisibility(View.GONE);
                    buka = 0;
                }
            }
        });
        TPP.setOnClickListener(new View.OnClickListener() {
            int buka=0;
            public void onClick(View view) {
                if(buka==0){
                    IsTPP.setVisibility(View.VISIBLE);
                    buka = 1;
                } else {
                    IsTPP.setVisibility(View.GONE);
                    buka = 0;
                }
            }
        });
        PSN.setOnClickListener(new View.OnClickListener() {
            int buka=0;
            public void onClick(View view) {
                if(buka==0){
                    IsPSN.setVisibility(View.VISIBLE);
                    buka = 1;
                } else {
                    IsPSN.setVisibility(View.GONE);
                    buka = 0;
                }
            }
        });
        TGS.setOnClickListener(new View.OnClickListener() {
            int buka=0;
            public void onClick(View view) {
                if(buka==0){
                    IsTGS.setVisibility(View.VISIBLE);
                    buka = 1;
                } else {
                    IsTGS.setVisibility(View.GONE);
                    buka = 0;
                }
            }
        });
    }
}
