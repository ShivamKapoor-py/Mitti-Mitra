package com.example.mittimitra;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class WelcomeActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        EditText etName = findViewById(R.id.etFullName);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        EditText etConfirmPassword = findViewById(R.id.etConfirmPassword);
        Button signUpButton = findViewById(R.id.sign_up_button);
        TextView loginLink = findViewById(R.id.login_link);

        fAuth = FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(v -> {

            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                etName.setError("Name is required");
                return;
            }
            if (TextUtils.isEmpty(email)) {
                etEmail.setError("Email is required");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                etPassword.setError("Password is required");
                return;
            }
            if (password.length() < 6) {
                etPassword.setError("Password must be at least 6 characters");
                return;
            }
            if (!password.equals(confirmPassword)) {
                etConfirmPassword.setError("Passwords do not match");
                return;
            }

            // 🔹 CREATE USER
            fAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener((Task<AuthResult> task) -> {

                        if (task.isSuccessful()) {

                            FirebaseUser user = fAuth.getCurrentUser();

                            // 🔹 SAVE NAME TO FIREBASE PROFILE
                            if (user != null) {
                                UserProfileChangeRequest profileUpdates =
                                        new UserProfileChangeRequest.Builder()
                                                .setDisplayName(name)
                                                .build();

                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(updateTask -> {
                                            if (updateTask.isSuccessful()) {
                                                Toast.makeText(this,
                                                        "Account created successfully",
                                                        Toast.LENGTH_SHORT).show();

                                                startActivity(new Intent(this, CropSelectActivity.class));
                                                finish();
                                            } else {
                                                Toast.makeText(this,
                                                        "Failed to save name",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }

                        } else {
                            Toast.makeText(this,
                                    task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });

        loginLink.setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class))
        );
    }
}
