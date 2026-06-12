package com.example.mittimitra;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;
import java.io.IOException;

import okhttp3.*;

public class ChatbotActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();
    // Replace with the live ngrok URL printed in Colab
    private final String BASE_URL = "https://thiolacetic-lakita-caespitosely.ngrok-free.app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        EditText etQuestion = findViewById(R.id.etQuestion);
        ImageButton btnSend = findViewById(R.id.btnSend);
        TextView tvAnswer = findViewById(R.id.tvAnswer);

        btnSend.setOnClickListener(v -> {
            String question = etQuestion.getText().toString().trim();
            if (!question.isEmpty()) sendQuestion(question, tvAnswer);
        });

        // your existing bottom navigation code...
//        BottomNavigationView bottom = findViewById(R.id.bottom_nav);
//        if (bottom != null) {
//            bottom.setOnItemSelectedListener(item -> {
//                switch (item.getItemId()) {
//                    case R.id.nav_dashboard:
//                        startActivity(new Intent(this, DashboardActivity.class));
//                        return true;
//                    case R.id.nav_readings:
//                        startActivity(new Intent(this, ReadingsActivity.class));
//                        return true;
//                    case R.id.nav_recomms:
//                        startActivity(new Intent(this, RecommendationsActivity.class));
//                        return true;
//                    case R.id.nav_chatbot:
//                        return true;
//                    case R.id.nav_profile:
//                        startActivity(new Intent(this, ProfileActivity.class));
//                        return true;
//                }
//                return false;
//            });
//            bottom.setSelectedItemId(R.id.nav_chatbot);
//        }
    }

    private void sendQuestion(String question, TextView tvAnswer) {
        // Build JSON body
        JSONObject json = new JSONObject();
        try {
            json.put("question", question);
        } catch (Exception e) { e.printStackTrace(); }

        RequestBody body = RequestBody.create(
                json.toString(), MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> tvAnswer.setText("Error: " + e.getMessage()));
            }
            @Override public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> tvAnswer.setText("Server error"));
                    return;
                }
                try {
                    String resp = response.body().string();
                    JSONObject obj = new JSONObject(resp);
                    String answer = obj.optString("answer", "No answer");
                    runOnUiThread(() -> tvAnswer.setText(answer));
                } catch (Exception e) {
                    runOnUiThread(() -> tvAnswer.setText("Parse error"));
                }
            }
        });
    }
}
