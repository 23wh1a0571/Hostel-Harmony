package com.example.hostel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Seethamma_Floor extends AppCompatActivity {
    //private Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seethamma_floor);

        int[] buttonIds = {
                R.id.room1, R.id.room2, R.id.room3, R.id.room4, R.id.room5,
                R.id.room6, R.id.room7, R.id.room8, R.id.room9, R.id.room10, R.id.room11
        };

        // ✅ Loop through each button and set a click listener
        for (int id : buttonIds) {
            Button button = findViewById(id);
            if (button != null) { // Ensure the button exists in the layout
                button.setOnClickListener(v -> {
                    Intent i = new Intent(Seethamma_Floor.this, Room.class);
                    startActivity(i);
                });
            }
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.floor1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}