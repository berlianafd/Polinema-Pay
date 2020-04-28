package com.example.polinemapay.activity.RiwayatJemputSampah;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.polinemapay.R;
import com.example.polinemapay.helper.SQLiteHandler;

import java.util.List;

public class ApplicationAdapterRiwayatJS extends ArrayAdapter<ApplicationRiwayatJS> {
    private static final String TAG = ApplicationAdapterRiwayatJS.class.getSimpleName();
    private List<ApplicationRiwayatJS> items;
    private SQLiteHandler db;
    String idUser;
    private ProgressDialog dialog;

    public ApplicationAdapterRiwayatJS(Context context, List<ApplicationRiwayatJS> items) {
        super(context, R.layout.content_main_riwayatjs, items);
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
            v = li.inflate(R.layout.content_main_riwayatjs, null);
        }

        ApplicationRiwayatJS app = items.get(position);

        if(app != null) {
            final TextView idJS = (TextView)v.findViewById(R.id.idJS);
            TextView namaacara = (TextView)v.findViewById(R.id.namaAcaraJS);
            TextView tglacara = (TextView)v.findViewById(R.id.tanggalJemputJS);
            TextView wktacara = (TextView)v.findViewById(R.id.waktuJemputJS);
            TextView tmptacara = (TextView)v.findViewById(R.id.alamatJemputJS);
            TextView perkiraanBS = (TextView)v.findViewById(R.id.perkiraanBSJS);
            Button status = (Button) v.findViewById(R.id.statusJS);

            if(idJS != null) idJS.setText(app.getIdJS());
            if(namaacara != null) namaacara.setText(app.getNamaAcara());
            if(tglacara != null) tglacara.setText(app.getTanggalJemput());
            if(wktacara != null) wktacara.setText(app.getWaktuJemput());
            if(tmptacara != null) tmptacara.setText(app.getAlamatJemput());
            if(perkiraanBS != null) perkiraanBS.setText(app.getPerkiraanBeratSampah()+" Kg");
            if(status != null) status.setText(app.getStatusJemput());
        }
        return v;
    }
}