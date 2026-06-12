package com.example.mittimitra;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivityh extends AppCompatActivity {
    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginh);
        prefs = new Prefs(this);

        Button loginBtnh = findViewById(R.id.btn_loginh);
        EditText emailh = findViewById(R.id.input_login_emailh);
        EditText passh = findViewById(R.id.input_login_passwordh);
        TextView signuph = findViewById(R.id.signup_linkh);
        loginBtnh.setOnClickListener(v -> {
            // Basic local validation. Replace with real auth later.
            if (emailh.getText().toString().trim().length() > 0 && passh.getText().toString().trim().length() >= 4) {
                prefs.setLoggedIn(true);
                startActivity(new Intent(LoginActivityh.this, CropSelectActivity.class));
                finish();
            } else {
                passh.setError("वैध क्रेडेंशियल दर्ज करें");
            }
        });
        signuph.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivityh.this, WelcomeActivity.class);
            startActivity(intent);
        });
    }
}


