package com.example.polinemapay.activity;

import java.util.List;

public interface FetchDataListener {
    public void onFetchComplete(List<ApplicationPesanan> data);
    public void onFetchFailure(String msg);
}

