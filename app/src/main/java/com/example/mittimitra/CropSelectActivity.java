package com.example.mittimitra;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CropSelectActivity extends AppCompatActivity {
    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_select);
        prefs = new Prefs(this);

        // For demo, consider first two tiles as selectable
        ImageView wheat = findViewById(R.id.wheat);
        ImageView rice = findViewById(R.id.rice);
        View.OnClickListener listener = v -> {
            String crop = (v.getId() == R.id.wheat) ? "Wheat" : "Rice";
            prefs.setSelectedCrop(crop);
            startActivity(new Intent(CropSelectActivity.this, LoadingActivity.class));
            finish();
        };
        if (wheat != null) wheat.setOnClickListener(listener);
        if (rice != null) rice.setOnClickListener(listener);
    }
}


