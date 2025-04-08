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

        List<String> floors = getFloorsForHostel(name);
        hostelData.put("floors", floors);

        hostelsRef.child(name).setValue(hostelData)
                .addOnSuccessListener(aVoid -> {
                    Log.d("FirebaseHelper", name + " added successfully");
                    addRoomsToAllFloors(hostelsRef, name, floors, 12); // Add 12 rooms per floor
                })
                .addOnFailureListener(e -> Log.e("FirebaseHelper", "Failed to add " + name, e));
    }

    // Returns floor names based on hostel name
    private static List<String> getFloorsForHostel(String name) {
        switch (name) {
            case "Soudamini":
            case "Seethamma":
                return Arrays.asList("Ground Floor", "First Floor", "Second Floor", "Third Floor");
            case "Samyutha":
            case "Saradha":
                return Arrays.asList("First Floor", "Second Floor", "Third Floor", "Fourth Floor");
            case "Saraswathi":
                return Arrays.asList("First Floor", "Second Floor");
            case "Sravani":
                return Arrays.asList("Ground Floor");
            default:
                return Arrays.asList("Ground Floor", "First Floor"); // fallback
        }
    }

    // Adds room data under a specific hostel & floor
    public static void addRoomsToFloor(DatabaseReference hostelsRef, String hostelName, String floorName, int roomCount) {
        HashMap<String, Object> rooms = new HashMap<>();
        for (int i = 1; i <= roomCount; i++) {
            String roomNumber = "Room " + String.format("%03d", i); // e.g., Room 001
            rooms.put(roomNumber, "available");
        }

        hostelsRef.child(hostelName).child(floorName).setValue(rooms)
                .addOnSuccessListener(aVoid ->
                        Log.d("FirebaseHelper", "Rooms added to " + hostelName + " - " + floorName))
                .addOnFailureListener(e ->
                        Log.e("FirebaseHelper", "Failed to add rooms to " + floorName, e));
    }

    // Adds rooms to all floors of a hostel
    public static void addRoomsToAllFloors(DatabaseReference hostelsRef, String hostelName, List<String> floors, int roomCount) {
        for (String floor : floors) {
            addRoomsToFloor(hostelsRef, hostelName, floor, roomCount);
        }
    }

    // Default image names for each hostel
    public static List<String> getDefaultImagesFor(String hostelName) {
        switch (hostelName) {
            case "Soudamini":
                return Arrays.asList("seethammar4", "samyutha", "sravani");
            case "Samyutha":
                return Arrays.asList("saradha", "samyutha", "sravani");
            case "Saraswathi":
                return Arrays.asList("seeethamma", "seethammaf2", "seethammaf3");
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
        DataSnapshot imagesSnapshot = snapshot.child("images");
        if (imagesSnapshot.exists()) {
            for (DataSnapshot imgSnap : imagesSnapshot.getChildren()) {
                String imgName = imgSnap.getValue(String.class);
                if (imgName != null) images.add(imgName);
            }
        }
        return images;
    }

    // Extracts floor names from Firebase snapshot
    public static List<String> extractFloors(DataSnapshot snapshot) {
        List<String> floors = new ArrayList<>();
        DataSnapshot floorsSnapshot = snapshot.child("floors");
        if (floorsSnapshot.exists()) {
            for (DataSnapshot floorSnap : floorsSnapshot.getChildren()) {
                String floor = floorSnap.getValue(String.class);
                if (floor != null) floors.add(floor);
            }
        }
        return floors;
    }
}
