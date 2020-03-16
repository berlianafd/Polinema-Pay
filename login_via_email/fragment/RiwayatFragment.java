package com.example.polinemapay.fragment;

import android.os.Bundle;

import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.polinemapay.R;

public class RiwayatFragment extends ListFragment {


    public RiwayatFragment() {
        // Required empty public constructor
    }

    public static RiwayatFragment newInstance(String param1, String param2) {
        RiwayatFragment fragment = new RiwayatFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_riwayat, container, false);
        // Inflate the layout for this fragment

        return view;
    }
}

