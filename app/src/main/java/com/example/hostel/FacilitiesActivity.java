package com.example.hostel;

import android.os.Bundle;
import android.widget.ImageButton;
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

public class FacilitiesActivity extends AppCompatActivity {

    private ViewPager2 facilitiesViewPager;
    private TabLayout facilitiesTabLayout;
    private DatabaseReference hostelsRef;

    private TextView facilityDescription;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilities);

        facilitiesViewPager = findViewById(R.id.facilitiesViewPager);
        facilitiesTabLayout = findViewById(R.id.facilitiesTabLayout);
        facilityDescription = findViewById(R.id.facilityDescription);
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());

        hostelsRef = FirebaseDatabase.getInstance().getReference("hostels");

        String hostelName = getIntent().getStringExtra("hostelName");
        if (hostelName != null) {
            loadFacilitiesImages(hostelName);
            loadFacilityDescription(hostelName);
        } else {
            Toast.makeText(this, "No hostel name passed", Toast.LENGTH_SHORT).show();
        }
    }
    private void loadFacilityDescription(String hostelName) {
        String description = FirebaseHelper.getFacilityDescriptionFor(hostelName);
        facilityDescription.setText(description);
    }

    private void loadFacilitiesImages(String hostelName) {
        hostelsRef.child(hostelName).child("facilities").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        List<String> facilityImages = FirebaseHelper.extractFacilities(snapshot);

                        ImagePageAdapter adapter = new ImagePageAdapter(FacilitiesActivity.this, facilityImages);
                        facilitiesViewPager.setAdapter(adapter);

                        new TabLayoutMediator(facilitiesTabLayout, facilitiesViewPager,
                                (tab, position) -> tab.setText("●")
                        ).attach();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(FacilitiesActivity.this, "Failed to load facilities", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
