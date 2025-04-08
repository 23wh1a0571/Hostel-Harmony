package com.example.hostel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Mainpage extends AppCompatActivity {

    private TextView hostelNameText;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private Spinner floorSpinner;
    private Button facilitiesButton;
    private ImageButton backButton;

    private DatabaseReference hostelsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        // Get hostel name from intent


        // Initialize UI
        hostelNameText = findViewById(R.id.hostel_name);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        floorSpinner = findViewById(R.id.mySpinner);
        facilitiesButton = findViewById(R.id.button_facilities);
        backButton = findViewById(R.id.back);

        hostelsRef = FirebaseDatabase.getInstance().getReference("hostels");

        // 🔙 Back button
        backButton.setOnClickListener(v -> finish());

        // Get hostel name from Intent
        String hostelName = getIntent().getStringExtra("hostel_name");
        if (hostelName != null) {
            hostelNameText.setText(hostelName);
            fetchHostelData(hostelName);
        } else {
            Toast.makeText(this, "No hostel selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchHostelData(String hostelName) {
        // Facilities button opens FloorActivity with selected hostel and floor
        facilitiesButton.setOnClickListener(v -> {
            String selectedFloor = floorSpinner.getSelectedItem() != null
                    ? floorSpinner.getSelectedItem().toString()
                    : null;

            if (selectedFloor != null && !selectedFloor.isEmpty()) {
                Intent intent = new Intent(Mainpage.this, FloorActivity.class);
                intent.putExtra("hostelName", hostelName);
                intent.putExtra("floorName", selectedFloor);
                startActivity(intent);
            } else {
                Toast.makeText(Mainpage.this, "Please select a floor", Toast.LENGTH_SHORT).show();
            }
        });

        hostelsRef.child(hostelName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<String> imageUrls = FirebaseHelper.extractImages(snapshot);
                    List<String> floors = FirebaseHelper.extractFloors(snapshot);

                    // Load images into ViewPager
                    ImagePageAdapter adapter = new ImagePageAdapter(Mainpage.this, imageUrls);
                    viewPager.setAdapter(adapter);

                    // Sync TabLayout with ViewPager2
                    new TabLayoutMediator(tabLayout, viewPager,
                            (tab, position) -> tab.setText(String.valueOf(position + 1))
                    ).attach();

                    // Load floors into Spinner
                    SpinnerAdapterHelper.setSpinnerData(Mainpage.this, floorSpinner, floors);

                } else {
                    // Add default data if not present
                    FirebaseHelper.addHostel(hostelsRef, hostelName, FirebaseHelper.getDefaultImagesFor(hostelName));
                    Toast.makeText(Mainpage.this, "Default data added for " + hostelName, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Mainpage.this, "Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }
}
