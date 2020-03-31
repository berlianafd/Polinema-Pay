package com.example.polinemapay.activity.Pesanan;

import com.example.polinemapay.activity.Pesanan.ApplicationPesanan;
import com.example.polinemapay.activity.Riwayat.ApplicationRiwayat;

import java.util.List;

public interface FetchDataListener {
    public void onFetchComplete(List<ApplicationPesanan> data);
    public void onFetchFailure(String msg);
}

