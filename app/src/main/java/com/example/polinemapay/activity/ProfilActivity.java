package com.example.polinemapay.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.polinemapay.R;
import com.example.polinemapay.helper.SQLiteHandler;
import com.example.polinemapay.helper.SessionManager;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class ProfilActivity extends AppCompatActivity {

    private EditText txtName;
    private EditText txtnohp;
    private Button btnLogout;

    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        txtName = (EditText) findViewById(R.id.namaProfil);
//        txtnohp = (EditText) findViewById(R.id.nohpProfil);
//        btnLogout = (Button) findViewById(R.id.btnLogout);
//
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }
//
//        // Fetching user details from SQLite
//        HashMap<String, String> user = db.getUserDetails();
//
//        String name = user.get("name");
//        String nohp = user.get("nohp");
//
//        // Displaying the user details on the screen
//        txtName.setText(name);
//        txtnohp.setText(nohp);
//
//        // Logout button click event
//        btnLogout.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                logoutUser();
//            }
//        });
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(ProfilActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
