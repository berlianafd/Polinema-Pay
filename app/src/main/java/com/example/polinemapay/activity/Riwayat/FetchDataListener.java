package com.example.polinemapay.activity.Riwayat;

import java.util.List;

public interface FetchDataListener {
    public void onFetchComplete(List<ApplicationRiwayat> data);
    public void onFetchFailure(String msg);
}

