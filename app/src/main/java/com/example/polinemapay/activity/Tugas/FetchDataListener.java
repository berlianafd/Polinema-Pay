package com.example.polinemapay.activity.Tugas;

import java.util.List;

public interface FetchDataListener {
    public void onFetchComplete(List<ApplicationTugas> data);
    public void onFetchFailure(String msg);
}

