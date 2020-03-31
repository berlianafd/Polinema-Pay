package com.example.polinemapay.activity.Tugas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.polinemapay.R;

import java.util.List;

public class ApplicationAdapterTugas extends ArrayAdapter<ApplicationTugas>{
    private List<ApplicationTugas> items;

    public ApplicationAdapterTugas(Context context, List<ApplicationTugas> items) {
        super(context, R.layout.content_main_tugas, items);
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
            v = li.inflate(R.layout.content_main_tugas, null);
        }

        ApplicationTugas app = items.get(position);

        if(app != null) {
//            ImageView icon = (ImageView)v.findViewById(R.id.appIcon);
            TextView namaacara = (TextView)v.findViewById(R.id.namaAcaraPesanan);
            TextView tglacara = (TextView)v.findViewById(R.id.tanggalJemputPesanan);
            TextView wktacara = (TextView)v.findViewById(R.id.waktuJemputPesanan);
            TextView tmptacara = (TextView)v.findViewById(R.id.alamatJemputPesanan);
            TextView perkiraanBS = (TextView)v.findViewById(R.id.perkiraanBS);


//            if(icon != null) {
//                Resources res = getContext().getResources();
//                String sIcon = "com.sj.jsondemo:drawable/" + app.getIcon();
//                icon.setImageDrawable(res.getDrawable(res.getIdentifier(sIcon, null, null)));
//            }

            if(namaacara != null) namaacara.setText(app.getNamaAcara());
            if(tglacara != null) tglacara.setText(app.getTanggalJemput());
            if(wktacara != null) wktacara.setText(app.getWaktuJemput());
            if(tmptacara != null) tmptacara.setText(app.getAlamatJemput());
            if(perkiraanBS != null) perkiraanBS.setText(app.getPerkiraanBeratSampah()+" Kg");



//            if(dlText != null) {
//                NumberFormat nf = NumberFormat.getNumberInstance();
//                dlText.setText(nf.format(app.getTotalDl())+" dl");
//            }

//            if(ratingCntr != null && ratingCntr.getChildCount() == 0) {
//                /*
//                 * max rating: 5
//                 */
//                for(int i=1; i<=5; i++) {
//                    ImageView iv = new ImageView(getContext());
//
//                    if(i <= app.getRating()) {
//                        iv.setImageDrawable(getContext().
//                                getResources().getDrawable(R.drawable.start_checked));
//                    }
//                    else {
//                        iv.setImageDrawable(getContext().
//                                getResources().getDrawable(R.drawable.start_unchecked));
//                    }
//
//                    ratingCntr.addView(iv);
//                }
//            }
        }

        return v;
    }
}