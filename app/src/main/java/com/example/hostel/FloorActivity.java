package com.example.hostel;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
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
                            String status = roomSnap.getValue(String.class);

                            Button btn = new Button(FloorActivity.this);
                            btn.setText(roomNumber);
                            btn.setTextColor(Color.BLACK);
                            btn.setTextSize(16f);
                            btn.setAllCaps(false);

                            // Set background based on room status
                            if ("booked".equalsIgnoreCase(status)) {
                                btn.setBackgroundColor(ContextCompat.getColor(FloorActivity.this, android.R.color.darker_gray));
                                btn.setEnabled(false); // disable interaction
                            } else {
                                btn.setBackgroundColor(Color.parseColor("#FFF5CC")); // Light yellow for available
                            }

                            // Grid layout parameters
                            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                            params.width = 350;
                            params.height = 180;
                            params.setMargins(24, 24, 24, 24);
                            btn.setLayoutParams(params);

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
