package com.example.mittimitra;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FertilizerActivity extends AppCompatActivity {

    private TextView fertilizerName, fertilizerNpkLabel, fertilizerDescription;
    private TextView quantityText, timingText;
    private Spinner cropSpinner;
    private ImageView fertilizerImage, backArrow;

    // NPK values (these would be from your Bluetooth sensor)
    private double nitrogen = 2.5; // Example value
    private double phosphorus = 0.5; // Example value
    private double potassium = 1.0; // Example value

    // Map to link crop names to drawable resource IDs
    private static final Map<String, Integer> FERTILIZER_IMAGES = new HashMap<>();

    static {
        FERTILIZER_IMAGES.put("Wheat", R.drawable.p2);
        FERTILIZER_IMAGES.put("Rice", R.drawable.p3);
        FERTILIZER_IMAGES.put("Maize", R.drawable.p4);
        FERTILIZER_IMAGES.put("Cotton", R.drawable.p5);
        // Note: No images provided for Potato and Sugarcane, so they will use the default one.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fertilizer);

        // Initialize UI components
        fertilizerName = findViewById(R.id.fertilizer_name);
        fertilizerNpkLabel = findViewById(R.id.fertilizer_npk_label);
        fertilizerDescription = findViewById(R.id.fertilizer_description);
        quantityText = findViewById(R.id.quantity_text);
        timingText = findViewById(R.id.timing_text);
        cropSpinner = findViewById(R.id.crop_spinner);
        fertilizerImage = findViewById(R.id.fertilizer_image);
        backArrow = findViewById(R.id.backArrow);

        // Set up the back button
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set up the listener for the crop spinner
        setupSpinnerListener();
    }

    private void setupSpinnerListener() {
        cropSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected crop from the spinner
                String selectedCrop = parent.getItemAtPosition(position).toString();
                // Call the recommendation function with the selected crop
                getFertilizerRecommendation(selectedCrop, nitrogen, phosphorus, potassium);
                // Change the image based on the selected crop
                if (FERTILIZER_IMAGES.containsKey(selectedCrop)) {
                    fertilizerImage.setImageResource(FERTILIZER_IMAGES.get(selectedCrop));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void getFertilizerRecommendation(String crop, double n, double p, double k) {
        // Dummy logic to find the condition. You'll need to define your own thresholds.
        String condition = "Low N";
        if (n > 2.0) condition = "High N";
        if (p < 0.4) condition = "Low P";
        if (k < 0.7) condition = "Low K";
        // ... and so on for all conditions

        List<DataRepository.FertilizerRecommendation> recommendations = DataRepository.FERTILIZER_DATA.get(crop);
        if (recommendations != null) {
            for (DataRepository.FertilizerRecommendation rec : recommendations) {
                if (rec.condition.equals(condition)) {
                    // Update UI with the local data
                    fertilizerName.setText(rec.recommendedFertilizer);
                    fertilizerNpkLabel.setText(String.format("N:%.1f%%, P:%.1f%%, K:%.1f%%", n, p, k));
                    fertilizerDescription.setText("A tailored recommendation for " + crop + " based on your soil.");
                    quantityText.setText("Quantity and timing will depend on local factors.");
                    timingText.setText("Consult a local agricultural expert for best results.");
                    return;
                }
            }
        }

        Toast.makeText(this, "No recommendation found for this condition.", Toast.LENGTH_SHORT).show();
    }
}
