package com.example.polinemapay.activity;

import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.example.polinemapay.R;
import com.example.polinemapay.activity.Pesanan.ApplicationAdapterPesanan;
import com.example.polinemapay.activity.Pesanan.ApplicationPesanan;
import com.example.polinemapay.activity.Pesanan.FetchDataListener;
import com.example.polinemapay.activity.Pesanan.FetchDataTaskPesanan;

public class PesananActivity extends ListActivity implements FetchDataListener {
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan);
        initView();
    }

    private void initView() {
        // show progress dialog
        dialog = ProgressDialog.show(this, "", "Loading...");

        String url = "https://www.polinema-pay.online/android/ListPesanan.php";
        FetchDataTaskPesanan task = new FetchDataTaskPesanan(this);
        task.execute(url);
    }


    @Override
    public void onFetchComplete(List<ApplicationPesanan> data) {
        // dismiss the progress dialog
        if(dialog != null)  dialog.dismiss();
        // create new adapter
        ApplicationAdapterPesanan adapter = new ApplicationAdapterPesanan(this, data);
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