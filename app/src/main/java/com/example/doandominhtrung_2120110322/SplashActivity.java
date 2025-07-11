package com.example.doandominhtrung_2120110322;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.logo);
        ImageView logoStatic = findViewById(R.id.logoStatic);

        // Load animation phóng to
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
        logo.startAnimation(anim);

        // Khi animation kết thúc:
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                // Ẩn logo animation
                logo.clearAnimation(); // Ngắt animation để tránh hình ảnh bị "đóng băng"
                logo.setVisibility(View.GONE);

                // Hiện logo tĩnh
                Animation fadeIn = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.fade_in);
                logoStatic.setVisibility(View.VISIBLE);
                logoStatic.startAnimation(fadeIn);

                // Chuyển sang màn hình chính sau 1s
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                    finish();
                }, 1000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

    }
}
