package com.example.odsen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        TextView loggedInn = findViewById(R.id.MAIN_logged_inn_user);
        String userText = getString(R.string.MAIN_logged_inn_as, "dust");
        loggedInn.setText(userText);
    }
}