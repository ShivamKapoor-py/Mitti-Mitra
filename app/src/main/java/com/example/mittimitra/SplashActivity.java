package com.example.mittimitra;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.splash_logo);

        // Fade-in Animation
        Animation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(1500);
        logo.startAnimation(fadeIn);

        Prefs prefs = new Prefs(this);

        new Handler().postDelayed(() -> {

            // 1️⃣ If user already logged in → main app
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                startActivity(new Intent(this, CropSelectActivity.class));
                finish();
                return;
            }

            // 2️⃣ If language already selected → login
            String lang = prefs.getLanguage();
            if (lang != null) {
                if (lang.equals("hi")) {
                    startActivity(new Intent(this, LoginActivityh.class));
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                finish();
                return;
            }

            // 3️⃣ First-time user → language selection
            startActivity(new Intent(this, LanguageActivity.class));
            finish();

        }, 1800); // same delay as your animation
    }
}
