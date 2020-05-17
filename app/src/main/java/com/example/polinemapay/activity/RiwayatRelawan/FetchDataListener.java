package com.example.polinemapay.activity.RiwayatRelawan;

import java.util.List;

public interface FetchDataListener {
    public void onFetchComplete(List<ApplicationRiwayatR> data);
    public void onFetchFailure(String msg);
}

