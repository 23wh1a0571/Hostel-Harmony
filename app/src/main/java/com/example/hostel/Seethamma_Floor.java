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
    private Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seethamma_floor);
        b = findViewById(R.id.room1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Seethamma_Floor.this, Room.class);
                startActivity(i);
            }
        });
        b = findViewById(R.id.room2);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Seethamma_Floor.this, Room.class);
                startActivity(i);
            }
        });
        b = findViewById(R.id.room3);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Seethamma_Floor.this, Room.class);
                startActivity(i);
            }
        });
        b = findViewById(R.id.room4);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Seethamma_Floor.this, Room.class);
                startActivity(i);
            }
        });
        b = findViewById(R.id.room5);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Seethamma_Floor.this, Room.class);
                startActivity(i);
            }
        });
        b = findViewById(R.id.room6);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Seethamma_Floor.this, Room.class);
                startActivity(i);
            }
        });
        b = findViewById(R.id.room7);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Seethamma_Floor.this, Room.class);
                startActivity(i);
            }
        });
        b = findViewById(R.id.room8);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Seethamma_Floor.this, Room.class);
                startActivity(i);
            }
        });
        b = findViewById(R.id.room9);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Seethamma_Floor.this, Room.class);
                startActivity(i);
            }
        });
        b = findViewById(R.id.room10);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Seethamma_Floor.this, Room.class);
                startActivity(i);
            }
        });
        b = findViewById(R.id.room11);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Seethamma_Floor.this, Room.class);
                startActivity(i);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.floor1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}