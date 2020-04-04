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
//            ImageView icon = (ImageView)v.findViewById(R.id.appIcon);
            TextView fiturRwyt = (TextView)v.findViewById(R.id.namaFitur);
            TextView tglRwyt = (TextView)v.findViewById(R.id.tanggal);
            TextView poinRwyt = (TextView)v.findViewById(R.id.poin);


//            if(icon != null) {
//                Resources res = getContext().getResources();
//                String sIcon = "com.sj.jsondemo:drawable/" + app.getIcon();
//                icon.setImageDrawable(res.getDrawable(res.getIdentifier(sIcon, null, null)));
//            }

            if(tglRwyt != null) tglRwyt.setText(app.getTanggal());

            if(poinRwyt != null){
                if ((app.getFitur().equals("ts"))){
                    if(fiturRwyt != null) fiturRwyt.setText("Penukaran Sampah");
                    poinRwyt.setTextColor(Color.BLUE);
                    poinRwyt.setText("+" + app.getPoin() + "poin");
                }else{
                    if(fiturRwyt != null) fiturRwyt.setText("Penukaran Poin");
                    poinRwyt.setTextColor(Color.RED);
                    poinRwyt.setText("-" + app.getPoin() + "poin");
                }
            }



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