package com.example.hostel;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FirebaseHelper {

    // Adds a hostel and its image URLs to Firebase
    public static void addHostel(DatabaseReference hostelsRef, String name, List<String> images) {
        HashMap<String, Object> hostelData = new HashMap<>();
        hostelData.put("name", name);
        hostelData.put("images", images);

        // Example: floors - 4 total floors
        List<String> floors = Arrays.asList("Ground Floor", "First Floor", "Second Floor", "Third Floor");
        hostelData.put("floors", floors);

        hostelsRef.child(name).setValue(hostelData)
                .addOnSuccessListener(aVoid -> Log.d("FirebaseHelper", name + " added successfully"))
                .addOnFailureListener(e -> Log.e("FirebaseHelper", "Failed to add " + name, e));
    }

    // Returns default image names for a hostel
    public static List<String> getDefaultImagesFor(String hostelName) {
        switch (hostelName) {
            case "Soudamini":
                return Arrays.asList("seethammar4", "samyutha", "sravani");
            case "Samyutha":
                return Arrays.asList("saradha", "samyutha", "sravani");
            case "Saraswathi":
                return Arrays.asList("seeethamma","seethammaf2", "seethammaf3");
            case "Saradha":
                return Arrays.asList("saradha", "samyutha", "saraswathi");
            case "Seethamma":
                return Arrays.asList("seethamma", "seethammaf2", "seethammaf3");
            case "Sravani":
                return Arrays.asList("seethammar1", "sravani", "seethammaf3");
            default:
                return Arrays.asList("default1", "default2", "default3");
        }
    }

    // Extracts image names from Firebase snapshot
    public static List<String> extractImages(DataSnapshot snapshot) {
        List<String> images = new ArrayList<>();
        for (DataSnapshot imgSnap : snapshot.child("images").getChildren()) {
            String imgName = imgSnap.getValue(String.class);
            if (imgName != null) images.add(imgName);
        }
        return images;
    }

    // Extracts floor names from Firebase snapshot
    public static List<String> extractFloors(DataSnapshot snapshot) {
        List<String> floors = new ArrayList<>();
        for (DataSnapshot floorSnap : snapshot.child("floors").getChildren()) {
            String floor = floorSnap.getValue(String.class);
            if (floor != null) floors.add(floor);
        }
        return floors;
    }
}
