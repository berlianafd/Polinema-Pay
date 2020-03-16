package com.example.polinemapay.fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.polinemapay.R;
import com.example.polinemapay.fragment.BerandaFragment;
import com.example.polinemapay.fragment.BottomNavigationBehavior;
import com.example.polinemapay.fragment.ProfilFragment;
import com.example.polinemapay.fragment.RiwayatFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivityFragment extends AppCompatActivity {
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);

        toolbar = getSupportActionBar();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // attaching bottom sheet behaviour - hide / show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

// load the store fragment by default
        toolbar.setTitle("Beranda");
        loadFragment(new BerandaFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_beranda:
                    toolbar.setTitle("Beranda");
                    fragment = new BerandaFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_riwayat:
                    toolbar.setTitle("Riwayat");
                    fragment = new RiwayatFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profil:
                    toolbar.setTitle("Profil");
                    fragment = new ProfilFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
