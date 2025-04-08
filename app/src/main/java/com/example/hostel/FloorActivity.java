package com.example.hostel;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FloorActivity extends AppCompatActivity {

    private GridLayout roomGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_floor);

        String hostelName = getIntent().getStringExtra("hostelName");
        String floorName = getIntent().getStringExtra("floorName");

        TextView title = findViewById(R.id.title);
        roomGrid = findViewById(R.id.roomGrid);

        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> finish());

        if (hostelName != null && floorName != null) {
            title.setText(hostelName + " - " + floorName);
            loadRoomsFromFirebase(hostelName, floorName);
        } else {
            Toast.makeText(this, "Hostel or Floor not found!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadRoomsFromFirebase(String hostelName, String floorName) {
        FirebaseDatabase.getInstance().getReference("hostels")
                .child(hostelName)
                .child(floorName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        roomGrid.removeAllViews();

                        for (DataSnapshot roomSnap : snapshot.getChildren()) {
                            String roomNumber = roomSnap.getKey();

                            // Skip bunk nodes like 101_bunks
                            if (roomNumber == null || roomNumber.endsWith("_bunks")) continue;

                            String roomStatus = "available"; // default

                            // Handle both old and new formats
                            if (roomSnap.getValue() instanceof String) {
                                roomStatus = roomSnap.getValue(String.class); // old format
                            } else if (roomSnap.hasChild("room_status")) {
                                roomStatus = roomSnap.child("room_status").getValue(String.class); // new format
                            }

                            Button btn = new Button(FloorActivity.this);
                            btn.setText(roomNumber);
                            btn.setTextColor(Color.BLACK);
                            btn.setTextSize(16f);
                            btn.setAllCaps(false);

                            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                            params.width = 350;
                            params.height = 180;
                            params.setMargins(24, 24, 24, 24);
                            btn.setLayoutParams(params);

                            if ("booked".equalsIgnoreCase(roomStatus)) {
                                btn.setBackgroundColor(ContextCompat.getColor(FloorActivity.this, android.R.color.darker_gray));
                                btn.setEnabled(false);
                            } else {
                                btn.setBackgroundColor(Color.parseColor("#FFF5CC"));
                                String finalRoomNumber = roomNumber;

                                btn.setOnClickListener(v -> {
                                    Intent intent = new Intent(FloorActivity.this, BunkSelectionActivity.class);
                                    intent.putExtra("hostelName", hostelName);
                                    intent.putExtra("floorName", floorName);
                                    intent.putExtra("roomNumber", finalRoomNumber);
                                    startActivity(intent);
                                });
                            }

                            roomGrid.addView(btn);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(FloorActivity.this, "Failed to load rooms.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
