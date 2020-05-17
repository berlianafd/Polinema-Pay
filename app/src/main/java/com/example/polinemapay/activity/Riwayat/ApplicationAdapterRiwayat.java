package com.example.polinemapay.activity.Riwayat;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.polinemapay.R;

import java.util.List;

public class ApplicationAdapterRiwayat extends ArrayAdapter<ApplicationRiwayat>{
    private List<ApplicationRiwayat> items;

    public ApplicationAdapterRiwayat(Context context, List<ApplicationRiwayat> items) {
        super(context, R.layout.content_main_riwayat, items);
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(v == null) {
            LayoutInflater li = LayoutInflater.from(getContext());
            v = li.inflate(R.layout.content_main_riwayat, null);
        }

        ApplicationRiwayat app = items.get(position);

        if(app != null) {
            TextView fiturRwyt = (TextView)v.findViewById(R.id.namaFitur);
            TextView tglRwyt = (TextView)v.findViewById(R.id.tanggal);
            TextView poinRwyt = (TextView)v.findViewById(R.id.poin);

            if(tglRwyt != null) tglRwyt.setText(app.getTanggal());

            if(poinRwyt != null){
                if ((app.getFitur().equals("ts"))){
                    if(fiturRwyt != null) fiturRwyt.setText("Penukaran Sampah");
                    poinRwyt.setTextColor(Color.BLUE);
                    poinRwyt.setText("+" + app.getPoin() + "poin");
                }else if ((app.getFitur().equals("tp"))){
                    if(fiturRwyt != null) fiturRwyt.setText("Penukaran Poin");
                    poinRwyt.setTextColor(Color.RED);
                    poinRwyt.setText("-" + app.getPoin() + "poin");
                } else {
                    if(fiturRwyt != null) fiturRwyt.setText("Transaksi Jual");
                    poinRwyt.setTextColor(Color.BLUE);
                    poinRwyt.setText("+" + app.getPoin() + "poin");
                }
            }
        }
        return v;
    }
}