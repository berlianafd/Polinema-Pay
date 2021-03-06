package com.example.polinemapay.activity;

import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polinemapay.R;
import com.example.polinemapay.activity.Riwayat.ApplicationAdapterRiwayat;
import com.example.polinemapay.activity.Riwayat.ApplicationRiwayat;
import com.example.polinemapay.activity.Riwayat.FetchDataListener;
import com.example.polinemapay.activity.Riwayat.FetchDataTaskRiwayat;
import com.example.polinemapay.helper.SessionManager;
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

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        String level = (String) b.get("level");

        if(level.equals("User")) {
            Button rwytJS = (Button) findViewById(R.id.riwayatJS);
            rwytJS.setVisibility(View.VISIBLE);

            rwytJS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent iin = getIntent();
                    Bundle b = iin.getExtras();
                    String idUser = (String) b.get("idUser");

                    Intent intent = new Intent(RiwayatActivity.this, RiwayatJemputSampahActivity.class);
                    intent.putExtra("idUser", idUser);
                    startActivity(intent);
                }
            });
        }
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
        String level =(String) b.get("level");
        SessionManager session = new SessionManager(getApplicationContext());
        System.out.println("id user" +idUser);
        System.out.println("jwt_token session "+session.getSessionJwtToken());
        String url = "https://www.polinema-pay.online/android/ListRiwayat.php?idUser=" + idUser+"&jwtToken="+session.getSessionJwtToken();
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