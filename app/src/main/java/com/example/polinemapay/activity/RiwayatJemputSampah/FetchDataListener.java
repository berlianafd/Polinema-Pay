package com.example.polinemapay.activity.RiwayatJemputSampah;

import java.util.List;

public interface FetchDataListener {
    public void onFetchComplete(List<ApplicationRiwayatJS> data);
    public void onFetchFailure(String msg);
}

