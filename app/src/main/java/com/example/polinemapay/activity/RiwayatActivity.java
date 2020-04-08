package com.example.polinemapay.activity;

import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polinemapay.R;
import com.example.polinemapay.activity.Riwayat.ApplicationAdapterRiwayat;
import com.example.polinemapay.activity.Riwayat.ApplicationRiwayat;
import com.example.polinemapay.activity.Riwayat.FetchDataListener;
import com.example.polinemapay.activity.Riwayat.FetchDataTaskRiwayat;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

public class RiwayatActivity extends ListActivity implements FetchDataListener {
    private ProgressDialog dialog;

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);
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

        String url = "http://192.168.43.10:8080/simple_api/ListRiwayat.php?idUser=" + idUser;
        FetchDataTaskRiwayat task = new FetchDataTaskRiwayat(this);
        task.execute(url);
    }

    @Override
    public void onFetchComplete(List<ApplicationRiwayat> data) {
        // dismiss the progress dialog
        if(dialog != null)  dialog.dismiss();
        // create new adapter
        ApplicationAdapterRiwayat adapter = new ApplicationAdapterRiwayat(this, data);
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