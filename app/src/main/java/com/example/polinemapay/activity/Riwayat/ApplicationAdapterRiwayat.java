package com.example.polinemapay.activity.Riwayat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.polinemapay.R;
import com.example.polinemapay.activity.DetailRiwayatTPActivity;
import com.example.polinemapay.activity.DetailRiwayatTRActivity;
import com.example.polinemapay.activity.DetailRiwayatTSActivity;

import java.util.List;

import androidx.cardview.widget.CardView;

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
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View v = convertView;

        if(v == null) {
            LayoutInflater li = LayoutInflater.from(getContext());
            v = li.inflate(R.layout.content_main_riwayat, null);
        }

        final ApplicationRiwayat app = items.get(position);

        if(app != null) {
            TextView fiturRwyt = (TextView)v.findViewById(R.id.namaFitur);
            TextView tglRwyt = (TextView)v.findViewById(R.id.tanggal);
            TextView poinRwyt = (TextView)v.findViewById(R.id.poin);
            TextView jamRwyt = (TextView)v.findViewById(R.id.jamTP);

            if(tglRwyt != null) tglRwyt.setText(app.getTanggal());
            if(jamRwyt != null) jamRwyt.setText(app.getJam());

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


            CardView cardViewTugas = (CardView)v.findViewById(R.id.cardViewRiwayat);
            cardViewTugas.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ListView) parent).performItemClick(convertView, position, 0); // Let the event be handled in onItemClick()
                    if(app.getFitur().equals("tp")) {
                        Intent intent = new Intent(getContext(), DetailRiwayatTPActivity.class);
                        intent.putExtra("tgl", app.getTanggal());
                        intent.putExtra("jam", app.getJam());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(intent);
                    } else if (app.getFitur().equals("ts")){
                        Intent intent = new Intent(getContext(), DetailRiwayatTSActivity.class);
                        intent.putExtra("tgl", app.getTanggal());
                        intent.putExtra("jam", app.getJam());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(intent);
                    } else if(app.getFitur().equals("tr")){
                        Intent intent = new Intent(getContext(), DetailRiwayatTRActivity.class);
                        intent.putExtra("tgl", app.getTanggal());
                        intent.putExtra("jam", app.getJam());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(intent);
                    }
                }
            }));
        }
        return v;
    }
}