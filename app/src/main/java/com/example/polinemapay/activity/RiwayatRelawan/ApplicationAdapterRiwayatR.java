package com.example.polinemapay.activity.RiwayatRelawan;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.polinemapay.R;
import com.example.polinemapay.activity.DetailTugasActivity;

import java.util.List;

import androidx.cardview.widget.CardView;

public class ApplicationAdapterRiwayatR extends ArrayAdapter<ApplicationRiwayatR>{
    private List<ApplicationRiwayatR> items;

    public ApplicationAdapterRiwayatR(Context context, List<ApplicationRiwayatR> items) {
        super(context, R.layout.content_main_riwayatrelawan, items);
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
            v = li.inflate(R.layout.content_main_riwayatrelawan, null);
        }

        final ApplicationRiwayatR app = items.get(position);

        if(app != null) {
//            ImageView icon = (ImageView)v.findViewById(R.id.appIcon);
            TextView namaacara = (TextView)v.findViewById(R.id.namaAcaraPesanan);
            TextView tglacara = (TextView)v.findViewById(R.id.tanggalJemputPesanan);
            TextView wktacara = (TextView)v.findViewById(R.id.waktuJemputPesanan);
            TextView tmptacara = (TextView)v.findViewById(R.id.alamatJemputPesanan);
            TextView perkiraanBS = (TextView)v.findViewById(R.id.perkiraanBS);
            CardView cardViewTugas = (CardView)v.findViewById(R.id.cardTugas);

            if(namaacara != null) namaacara.setText(app.getNamaAcara());
            if(tglacara != null) tglacara.setText(app.getTanggalJemput());
            if(wktacara != null) wktacara.setText(app.getWaktuJemput());
            if(tmptacara != null) tmptacara.setText(app.getAlamatJemput());
            if(perkiraanBS != null) perkiraanBS.setText(app.getPerkiraanBeratSampah()+" Kg");

            cardViewTugas.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ((ListView) parent).performItemClick(convertView, position, 0); // Let the event be handled in onItemClick()
                    String idTgs = app.getIdAcara();
                    Intent intent = new Intent(getContext(), DetailTugasActivity.class);
                    intent.putExtra("idTugas", idTgs);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                }
            }));
        }
        return v;
    }
}