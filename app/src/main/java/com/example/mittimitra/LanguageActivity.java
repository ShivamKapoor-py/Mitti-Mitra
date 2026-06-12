package com.example.mittimitra;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LanguageActivity extends AppCompatActivity {

    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_select);

        prefs = new Prefs(this);

        Button hindi = findViewById(R.id.btn_hindi);
        Button continueBtn = findViewById(R.id.btn_continue);

        // English
        continueBtn.setOnClickListener(v -> {
            prefs.setLanguage("en");
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // Hindi
        hindi.setOnClickListener(v -> {
            prefs.setLanguage("hi");
            startActivity(new Intent(this, LoginActivityh.class));
            finish();
        });
    }
}
