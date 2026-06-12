package com.example.mittimitra;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AiCoachActivity extends AppCompatActivity {
    private RecyclerView chatRecyclerView;
    private EditText messageInput;
    private ImageButton sendButton;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_coach);

        // Initialize views
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        // Initialize chat messages
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);

        // Set up RecyclerView
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        // Add welcome message
        addMessage("AI Coach", "Hello! I'm your AI agriculture coach. How can I help you today? You can ask me about:\n" +
                "- Soil health and fertility\n" +
                "- Crop recommendations\n" +
                "- Fertilizer and nutrient management\n" +
                "- Irrigation and water efficiency\n" +
                "- Sustainable farming practices");


        // Set up send button click listener
        sendButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (!message.isEmpty()) {
                // Add user message
                addMessage("You", message);
                messageInput.setText("");

                // Generate AI response
                generateAiResponse(message);
            }
        });
    }

    private void addMessage(String sender, String message) {
        chatMessages.add(new ChatMessage(sender, message));
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
    }

    private void generateAiResponse(String userMessage) {
        // Simulate AI response (replace with actual AI integration)
        String response;
        userMessage = userMessage.toLowerCase();

        if (userMessage.contains("soil") || userMessage.contains("fertility")) {
            response = "For maintaining healthy soil, remember to:\n" +
                    "1. Test your soil regularly for pH and nutrients\n" +
                    "2. Add compost or organic manure\n" +
                    "3. Avoid overuse of chemical fertilizers\n" +
                    "4. Rotate crops to maintain soil balance\n" +
                    "Would you like me to suggest soil improvement methods?";
        } else if (userMessage.contains("crop") || userMessage.contains("plant") || userMessage.contains("cultivation")) {
            response = "For better crop selection:\n" +
                    "1. Choose crops suited to your soil type and climate\n" +
                    "2. Prefer disease-resistant varieties\n" +
                    "3. Maintain proper spacing and timely sowing\n" +
                    "4. Follow integrated pest management\n" +
                    "Would you like me to recommend crops for your soil or region?";
        } else if (userMessage.contains("fertilizer") || userMessage.contains("nutrient") || userMessage.contains("manure")) {
            response = "For effective fertilizer and nutrient management:\n" +
                    "1. Use NPK in balanced ratios based on soil testing\n" +
                    "2. Apply compost or green manure regularly\n" +
                    "3. Avoid excess nitrogen to prevent soil acidification\n" +
                    "4. Split fertilizer doses for better absorption\n" +
                    "Would you like a fertilizer schedule for a specific crop?";
        } else if (userMessage.contains("irrigation") || userMessage.contains("water")) {
            response = "For efficient irrigation:\n" +
                    "1. Use drip or sprinkler systems to save water\n" +
                    "2. Water early morning or late evening to reduce evaporation\n" +
                    "3. Monitor soil moisture before watering\n" +
                    "4. Collect rainwater to reduce dependency on groundwater\n" +
                    "Would you like irrigation advice for your crop type?";
        } else {
            response = "I'm here to help with your farming journey! You can ask about:\n" +
                    "- Soil health and fertility\n" +
                    "- Crop recommendations\n" +
                    "- Fertilizer and nutrient management\n" +
                    "- Irrigation and water efficiency\n" +
                    "- Sustainable farming practices";
        }

        // Add AI response after a short delay
        chatRecyclerView.postDelayed(() -> addMessage("AI Coach", response), 1000);
    }

    // Chat Message class
    public static class ChatMessage {
        String sender;
        String message;

        ChatMessage(String sender, String message) {
            this.sender = sender;
            this.message = message;
        }
    }
} 