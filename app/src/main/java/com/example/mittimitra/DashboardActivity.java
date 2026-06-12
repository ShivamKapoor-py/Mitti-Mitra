package com.example.mittimitra;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity implements BluetoothService.Callback {

    private static final int REQ_PERM = 101;
    private static final int CAMERA_REQUEST = 100;

    // Use your ESP32's MAC address
    private static final String ESP32_MAC = "AC:15:18:D7:71:1A";

    private BluetoothService btService;
    private TextView tvMoisture, tvTds, tvHumidity, tvPh, tvRain;

    private LinearLayout nitrogenCard;
    private ImageView nitrogenIcon;
    private TextView nitrogenValue;

    private LinearLayout phosphorusCard;
    private ImageView phosphorusIcon;
    private TextView phosphorusValue;

    private int currentCardId = 0;

    // Potassium UI
    private LinearLayout potassiumCard;
    private ImageView potassiumIcon;
    private TextView potassiumValue;

    // 🔹 Alert UI
    private TextView alertText;
    private boolean isIrrigating = false; // track irrigation state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvMoisture = findViewById(R.id.tvMoisture);
        tvTds      = findViewById(R.id.tvTds);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvPh       = findViewById(R.id.tvPh);
        tvRain     = findViewById(R.id.tvRain);


        // Potassium views
        potassiumCard  = findViewById(R.id.potassiumCard);
        potassiumIcon  = findViewById(R.id.potassiumIcon);
        potassiumValue = findViewById(R.id.potassiumValue);

        nitrogenCard  = findViewById(R.id.nitrogenCard);
        nitrogenIcon  = findViewById(R.id.nitrogenIcon);
        nitrogenValue = findViewById(R.id.nitrogenValue);

        phosphorusCard  = findViewById(R.id.phosphorusCard);
        phosphorusIcon  = findViewById(R.id.phosphorusIcon);
        phosphorusValue = findViewById(R.id.phosphorusValue);

        // Alert view
        alertText = findViewById(R.id.alert);

        // Camera click for nitrogen
        potassiumCard.setOnClickListener(v -> {
            currentCardId = R.id.potassiumCard;
            startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST);
        });

        nitrogenCard.setOnClickListener(v -> {
            currentCardId = R.id.nitrogenCard;
            startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST);
        });

        phosphorusCard.setOnClickListener(v -> {
            currentCardId = R.id.phosphorusCard;
            startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST);
        });


        btService = new BluetoothService(this, this);

        // 🚀 Integrate Bottom Navigation
        BottomNavigationView bottom = findViewById(R.id.bottom_nav);
        if (bottom != null) {
            bottom.setOnItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.nav_dashboard) {
                    return true;
                } else if (id == R.id.nav_readings) {
                    startActivity(new Intent(this, ReadingsActivity.class));
                    return true;
                } else if (id == R.id.nav_recomms) {
                    startActivity(new Intent(this, RecommendationsActivity.class));
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

            // Set the correct selected item for the current activity.
            // Since you were in 'recomms', I'm setting this to reflect that.
            bottom.setSelectedItemId(R.id.nav_dashboard);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ensurePermissions();
    }

    private void ensurePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            String[] perms = {
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.ACCESS_FINE_LOCATION
            };
            for (String p : perms) {
                if (ActivityCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, perms, REQ_PERM);
                    return;
                }
            }
        } else {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PERM);
                return;
            }
        }
        startBluetoothConnect();
    }

    private void startBluetoothConnect() {
        BluetoothDevice device;

        // Try to get device by MAC address first
        try {
            device = btService.getAdapter().getRemoteDevice(ESP32_MAC);
        } catch (IllegalArgumentException e) {
            device = btService.findPairedDeviceByName("ESP32_Soil_TDS");
        }

        if (device == null) {
            Toast.makeText(this,
                    "Pair the ESP32 in system Bluetooth first",
                    Toast.LENGTH_LONG).show();
            return;
        }

        btService.connectWithFallback(device);
    }

    @Override
    public void onConnectionState(boolean connected, String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onNewReading(BluetoothService.SensorReading r) {
        runOnUiThread(() -> {
            if (!r.moisture.isEmpty()) {
                try {
                    // allow for decimal numbers and remove any stray units
                    String numeric = r.moisture.replaceAll("[^0-9.]", "");
                    float moistureVal = Float.parseFloat(numeric.trim());
                    int actualMoisture = Math.round(100 - moistureVal); // subtract from 100
                    tvMoisture.setText(actualMoisture + " %");
                } catch (NumberFormatException e) {
                    Log.e("Dashboard", "Invalid moisture value: " + r.moisture, e);
                }
            }
            if (!r.moisture.isEmpty()) {
                try {
                    // allow for decimal numbers and remove any stray units
                    String numeric = r.moisture.replaceAll("[^0-9.]", "");
                    float moistureVal = Float.parseFloat(numeric.trim());
                    int actualMoisture = Math.round(100 - moistureVal); // subtract from 100

                    // Update UI
                    tvMoisture.setText(actualMoisture + " %");

                    // 🔹 Alert logic
                    if (actualMoisture <= 20) {
                        // Alert to start irrigation
                        alertText.setVisibility(View.VISIBLE);
                        alertText.setText("Moisture below 20% – Please start irrigation.");
                        isIrrigating = true;

                    } else if (isIrrigating && actualMoisture >= 30) {
                        // Alert to stop irrigation
                        alertText.setVisibility(View.VISIBLE);
                        alertText.setText("Moisture above 30% – Stop watering.");
                        isIrrigating = false;

                    } else if (actualMoisture > 20 && actualMoisture < 30 && !isIrrigating) {
                        // Hide alert if between 20–30 and not irrigating
                        alertText.setVisibility(View.GONE);
                    }

                } catch (NumberFormatException e) {
                    Log.e("Dashboard", "Invalid moisture value: " + r.moisture, e);
                }
            }

            if (!r.tds.isEmpty())      tvTds.setText(r.tds + " ppm");
            if (!r.humidity.isEmpty()) tvHumidity.setText(r.humidity + " %");
            if (!r.ph.isEmpty())       tvPh.setText(r.ph);
            if (!r.rain.isEmpty() && tvRain != null) tvRain.setText(r.rain);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (btService != null) btService.cancel();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQ_PERM) {
            for (int r : grantResults) {
                if (r != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,
                            "Bluetooth permissions required", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            startBluetoothConnect();
        } else super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // Handle camera result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            if (currentCardId == R.id.potassiumCard && potassiumIcon != null && potassiumValue != null) {
                potassiumIcon.setVisibility(View.GONE);
                potassiumValue.setVisibility(View.VISIBLE);
                potassiumValue.setText("~128 ppm");
            }
            else if (currentCardId == R.id.nitrogenCard && nitrogenIcon != null && nitrogenValue != null) {
                nitrogenIcon.setVisibility(View.GONE);
                nitrogenValue.setVisibility(View.VISIBLE);
                nitrogenValue.setText("~132 ppm");
            }
            else if (currentCardId == R.id.phosphorusCard && phosphorusIcon != null && phosphorusValue != null) {
                phosphorusIcon.setVisibility(View.GONE);
                phosphorusValue.setVisibility(View.VISIBLE);
                phosphorusValue.setText("~46 ppm");
            }
        }
    }

}
