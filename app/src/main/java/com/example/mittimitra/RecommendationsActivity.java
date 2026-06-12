
package com.example.mittimitra;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

    public class RecommendationsActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_recommendations);

            // Get references to each card
            LinearLayout btnFertilizer = findViewById(R.id.btn_fertilizer);
            LinearLayout btnPesticide = findViewById(R.id.btn_pesticide);
            LinearLayout btnIrrigation = findViewById(R.id.btn_irrigation);
            LinearLayout btnNutrition  = findViewById(R.id.btn_nutrition);
            LinearLayout btnShelfLife  = findViewById(R.id.btn_shelflife);

            // Set listeners
            btnFertilizer.setOnClickListener(v ->
                    startActivity(new Intent(this, FertilizerActivity.class)));

            btnPesticide.setOnClickListener(v ->
                    startActivity(new Intent(this, PesticideActivity.class)));

            btnIrrigation.setOnClickListener(v ->
                    startActivity(new Intent(this, IrrigationActivity.class)));

            btnNutrition.setOnClickListener(v ->
                    startActivity(new Intent(this, NutritionalActivity.class)));

            btnShelfLife.setOnClickListener(v ->
                    startActivity(new Intent(this, ShelfActivity.class)));

            // Optional: handle BottomNavigationView

    BottomNavigationView bottom = findViewById(R.id.bottom_nav);
        if (bottom != null) {
            bottom.setOnItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.nav_dashboard) {
                    startActivity(new Intent(this, DashboardActivity.class));
                    return true;
                } else if (id == R.id.nav_readings) {
                    startActivity(new Intent(this, ReadingsActivity.class));
                    return true;
                } else if (id == R.id.nav_recomms) {
                    return true;
                } else if (id == R.id.nav_chatbot) {
                    startActivity(new Intent(this, AiCoachActivity.class));
                    return true;
                } else if (id == R.id.nav_profile) {
                    startActivity(new Intent(this, ProfileActivity.class));
                    return true;
                }
                return false;
            });
            bottom.setSelectedItemId(R.id.nav_recomms);
        }
    }
}


