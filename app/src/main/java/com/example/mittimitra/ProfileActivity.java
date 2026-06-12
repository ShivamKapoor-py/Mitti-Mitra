package com.example.mittimitra;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    ImageView profileImage;
    TextView profileName;

    ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        profileImage = findViewById(R.id.profileImage);
        profileName = findViewById(R.id.profileName);

        TextView logout = findViewById(R.id.logout);
        TextView deleteAccount = findViewById(R.id.deleteaccount);

        loadUserProfile();

        // 🔹 Pick image launcher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        updateProfileImage(imageUri);
                    }
                });

        // 🔹 Change profile image
        profileImage.setOnClickListener(v -> openImagePicker());

        // 🔹 Logout
        logout.setOnClickListener(v -> showLogoutDialog());

        // 🔹 Delete account
        deleteAccount.setOnClickListener(v -> showDeleteAccountDialog());

        setupBottomNav();
    }

    // ================= LOAD PROFILE =================
    private void loadUserProfile() {
        if (user != null) {
            profileName.setText(
                    user.getDisplayName() != null ? user.getDisplayName() : "User"
            );

            if (user.getPhotoUrl() != null) {
                Picasso.get()
                        .load(user.getPhotoUrl())
                        .placeholder(R.drawable.avatar_profile)
                        .into(profileImage);
            }
        }
    }

    // ================= IMAGE PICKER =================
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    // ================= UPDATE IMAGE =================
    private void updateProfileImage(Uri imageUri) {
        UserProfileChangeRequest updates =
                new UserProfileChangeRequest.Builder()
                        .setPhotoUri(imageUri)
                        .build();

        user.updateProfile(updates)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Profile image updated", Toast.LENGTH_SHORT).show();
                    profileImage.setImageURI(imageUri);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to update image", Toast.LENGTH_SHORT).show()
                );
    }

    // ================= LOGOUT =================
    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (d, w) -> {
                    auth.signOut();
                    goToWelcome();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // ================= DELETE ACCOUNT =================
    private void showDeleteAccountDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("This action is permanent. Are you sure?")
                .setPositiveButton("Delete", (d, w) -> {
                    if (user != null) {
                        user.delete()
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(this,
                                            "Account deleted", Toast.LENGTH_SHORT).show();
                                    goToWelcome();
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(this,
                                                "Re-login required", Toast.LENGTH_LONG).show()
                                );
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // ================= NAVIGATION =================
    private void setupBottomNav() {
        BottomNavigationView bottom = findViewById(R.id.bottom_nav);
        if (bottom != null) {
            bottom.setOnItemSelectedListener(item -> {
                int id = item.getItemId();
               if (id == R.id.nav_dashboard) {
                   startActivity(new Intent(this, DashboardActivity.class));
                   finish();
                   return true;
               }
               else if (id == R.id.nav_readings) {
                   startActivity(new Intent(this, ReadingsActivity.class));
                   finish(); return true;
               } else if (id == R.id.nav_recomms)
                   { startActivity(new Intent(this, RecommendationsActivity.class));
                       finish();
                       return true;
                   }
               else if (id == R.id.nav_chatbot) {
                   startActivity(new Intent(this, AiCoachActivity.class));
                   finish();
                   return true;
               } else if (id == R.id.nav_profile) {
                   return true;
               } return false;
            });
            bottom.setSelectedItemId(R.id.nav_profile); } }

    // ================= REDIRECT =================
    private void goToWelcome() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
