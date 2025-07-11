package com.example.doandominhtrung_2120110322;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class LauncherActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Chuyển ngay sang SplashActivity
        startActivity(new Intent(this, SplashActivity.class));
        finish();
    }
}
