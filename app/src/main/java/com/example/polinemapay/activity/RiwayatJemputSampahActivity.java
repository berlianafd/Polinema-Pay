package com.example.polinemapay.activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polinemapay.R;
import com.example.polinemapay.activity.RiwayatJemputSampah.ApplicationAdapterRiwayatJS;
import com.example.polinemapay.activity.RiwayatJemputSampah.ApplicationRiwayatJS;
import com.example.polinemapay.activity.RiwayatJemputSampah.FetchDataListener;
import com.example.polinemapay.activity.RiwayatJemputSampah.FetchDataTaskRiwayatJS;

import java.util.List;

public class RiwayatJemputSampahActivity extends ListActivity implements FetchDataListener {
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_jemput_sampah);
        initView();
    }

    private void initView() {
        TextView tt = (TextView) findViewById(R.id.toolbarText);
        tt.setText("Riwayat Jemput Sampah");
        // show progress dialog
        dialog = ProgressDialog.show(this, "", "Loading...");

        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        String idUser =(String) b.get("idUser");

        String url = "https://www.polinema-pay.online/android/ListRiwayatJemputSampah.php?idUser=" + idUser;
        FetchDataTaskRiwayatJS task = new FetchDataTaskRiwayatJS(this);
        task.execute(url);
    }


    @Override
    public void onFetchComplete(List<ApplicationRiwayatJS> data) {
        // dismiss the progress dialog
        if(dialog != null)  dialog.dismiss();
        // create new adapter
        ApplicationAdapterRiwayatJS adapter = new ApplicationAdapterRiwayatJS(this, data);
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
