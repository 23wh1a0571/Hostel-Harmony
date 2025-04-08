package com.example.hostel;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BunkSelectionActivity extends AppCompatActivity {

    private GridLayout bunkGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bunk_selection);

        String hostelName = getIntent().getStringExtra("hostelName");
        String floorName = getIntent().getStringExtra("floorName");
        String roomNumber = getIntent().getStringExtra("roomNumber");

        TextView title = findViewById(R.id.bunkTitle);
        bunkGrid = findViewById(R.id.bunkGrid);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        if (hostelName != null && floorName != null && roomNumber != null) {
            title.setText(roomNumber + " Bunks");
            loadBunks(hostelName, floorName, roomNumber);
        } else {
            Toast.makeText(this, "Missing data", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadBunks(String hostel, String floor, String room) {
        FirebaseDatabase.getInstance().getReference("hostels")
                .child(hostel).child(floor).child(room + "_bunks")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        bunkGrid.removeAllViews();

                        for (DataSnapshot bunkSnap : snapshot.getChildren()) {
                            String bunkName = bunkSnap.getKey();
                            String status = bunkSnap.getValue(String.class);

                            Button bunkBtn = new Button(BunkSelectionActivity.this);
                            bunkBtn.setText(bunkName);
                            bunkBtn.setTextColor(Color.BLACK);
                            bunkBtn.setTextSize(14f);
                            bunkBtn.setAllCaps(false);

                            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                            params.width = 300;
                            params.height = 160;
                            params.setMargins(20, 20, 20, 20);
                            bunkBtn.setLayoutParams(params);

                            if ("booked".equalsIgnoreCase(status)) {
                                bunkBtn.setBackgroundColor(ContextCompat.getColor(BunkSelectionActivity.this, android.R.color.darker_gray));
                                bunkBtn.setEnabled(false);
                            } else {
                                bunkBtn.setBackgroundColor(Color.parseColor("#FFF5CC")); // Gold
                                bunkBtn.setOnClickListener(v -> {
                                    Toast.makeText(BunkSelectionActivity.this, bunkName + " selected!", Toast.LENGTH_SHORT).show();
                                    // Future: Trigger booking
                                });
                            }

                            bunkGrid.addView(bunkBtn);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(BunkSelectionActivity.this, "Failed to load bunks", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
