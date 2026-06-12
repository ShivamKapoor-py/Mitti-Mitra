package com.example.mittimitra;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class PesticideActivity extends AppCompatActivity {

    private TextView pesticideName, pesticideDescription, pesticideQuantityText, pesticideTimingText;
    private Spinner cropSpinner, pestSpinner;
    private ImageView pesticideImage, backArrow;

    // Map to link crop names to drawable resource IDs
    private static final Map<String, Integer> PESTICIDE_IMAGES = new HashMap<>();

    static {
        PESTICIDE_IMAGES.put("Wheat", R.drawable.f1);
        PESTICIDE_IMAGES.put("Rice", R.drawable.f2);
        PESTICIDE_IMAGES.put("Maize", R.drawable.f3);
        PESTICIDE_IMAGES.put("Cotton", R.drawable.f4);
        PESTICIDE_IMAGES.put("Potato", R.drawable.p1);
        // Note: No image provided for Sugarcane, so it will use the default one.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Corrected the layout file name to match the XML provided.
        setContentView(R.layout.activity_pest);

        // Initialize UI components
        pesticideName = findViewById(R.id.pesticide_name);
        pesticideDescription = findViewById(R.id.pesticide_description);
        pesticideQuantityText = findViewById(R.id.pesticide_quantity_text);
        pesticideTimingText = findViewById(R.id.pesticide_timing_text);
        pesticideImage = findViewById(R.id.pesticide_image);
        cropSpinner = findViewById(R.id.pesticide_crop_spinner);
        pestSpinner = findViewById(R.id.pest_spinner);
        backArrow = findViewById(R.id.backArrow);

        // Set up the back button
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set up listeners for the spinners
        setupSpinners();
    }

    private void setupSpinners() {
        // Set up the listener for the crop spinner
        cropSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCrop = parent.getItemAtPosition(position).toString();
                updatePestSpinner(selectedCrop);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set up the listener for the pest spinner
        pestSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCrop = cropSpinner.getSelectedItem().toString();
                String selectedPest = parent.getItemAtPosition(position).toString();
                getPesticideRecommendation(selectedCrop, selectedPest);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void updatePestSpinner(String crop) {
        List<String> pestsForCrop = DataRepository.getPestsForCrop(crop);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pestsForCrop);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pestSpinner.setAdapter(adapter);

        // Change the image based on the selected crop
        if (PESTICIDE_IMAGES.containsKey(crop)) {
            pesticideImage.setImageResource(PESTICIDE_IMAGES.get(crop));
        }
    }

    private void getPesticideRecommendation(String crop, String pest) {
        List<DataRepository.PesticideRecommendation> recommendations = DataRepository.PESTICIDE_DATA.get(crop);
        if (recommendations != null) {
            for (DataRepository.PesticideRecommendation rec : recommendations) {
                if (rec.pest.equals(pest)) {
                    // Update UI with the local data
                    pesticideName.setText(rec.pesticideName);
                    pesticideDescription.setText(rec.recommendation);
                    // Add dummy text for quantity and timing
                    pesticideQuantityText.setText("Consult a local agricultural expert for quantity.");
                    pesticideTimingText.setText("Apply as per product label instructions.");
                    return;
                }
            }
        }
        Toast.makeText(this, "No recommendation found for this pest and crop.", Toast.LENGTH_SHORT).show();
        // Clear UI if no recommendation is found
        pesticideName.setText("N/A");
        pesticideDescription.setText("No specific recommendation available.");
        pesticideQuantityText.setText("No quantity data available.");
        pesticideTimingText.setText("No timing data available.");
    }
}
