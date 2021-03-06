package com.example.polinemapay.activity;

import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.polinemapay.R;
import com.example.polinemapay.activity.Tugas.ApplicationAdapterTugas;
import com.example.polinemapay.activity.Tugas.ApplicationTugas;
import com.example.polinemapay.activity.Tugas.FetchDataListener;
import com.example.polinemapay.activity.Tugas.FetchDataTaskTugas;
import com.example.polinemapay.helper.SessionManager;

public class TugasActivity extends ListActivity implements FetchDataListener {
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tugas);
        initView();
    }

    private void initView() {
        // show progress dialog
        dialog = ProgressDialog.show(this, "", "Loading...");

        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        String idUser =(String) b.get("idUser");
        SessionManager session = new SessionManager(getApplicationContext());

        System.out.println("jwt_token session "+session.getSessionJwtToken());
        String url = "https://www.polinema-pay.online/android/ListTugas.php?idUser=" + idUser+"&jwtToken="+session.getSessionJwtToken();
        FetchDataTaskTugas task = new FetchDataTaskTugas(this);
        task.execute(url);
    }

    @Override
    public void onFetchComplete(List<ApplicationTugas> data) {
        // dismiss the progress dialog
        if(dialog != null)  dialog.dismiss();
        // create new adapter
        ApplicationAdapterTugas adapter = new ApplicationAdapterTugas(this, data);
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