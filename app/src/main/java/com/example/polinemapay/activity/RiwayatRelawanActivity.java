package com.example.polinemapay.activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polinemapay.R;
import com.example.polinemapay.activity.RiwayatRelawan.ApplicationAdapterRiwayatR;
import com.example.polinemapay.activity.RiwayatRelawan.ApplicationRiwayatR;
import com.example.polinemapay.activity.RiwayatRelawan.FetchDataListener;
import com.example.polinemapay.activity.RiwayatRelawan.FetchDataTaskRiwayatR;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

public class RiwayatRelawanActivity extends ListActivity implements FetchDataListener {
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayatrelawan);

        initView();
    }

    private void initView() {
        TextView tt = (TextView) findViewById(R.id.toolbarText);
        tt.setText("Riwayat");
        // show progress dialog
        dialog = ProgressDialog.show(this, "", "Loading...");

        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        String idUser =(String) b.get("idUser");

        String url = "https://www.polinema-pay.online/android/ListRiwayatRelawan.php?idUser=" + idUser;
        FetchDataTaskRiwayatR task = new FetchDataTaskRiwayatR(this);
        task.execute(url);
    }

    @Override
    public void onFetchComplete(List<ApplicationRiwayatR> data) {
        // dismiss the progress dialog
        if(dialog != null)  dialog.dismiss();
        // create new adapter
        ApplicationAdapterRiwayatR adapter = new ApplicationAdapterRiwayatR(this, data);
        // set the adapter to list
        setListAdapter(adapter);
    }

    @Override
    public void onFetchFailure(String msg) {
        // dismiss the progress dialog
        if(dialog != null)  dialog.dismiss();
        // show failure message
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}