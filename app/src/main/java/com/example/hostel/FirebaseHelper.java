package com.example.hostel;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseHelper {

    // Extract images from snapshot
    public static List<String> extractImages(DataSnapshot snapshot) {
        List<String> imageUrls = new ArrayList<>();
        DataSnapshot imagesSnapshot = snapshot.child("images");
        for (DataSnapshot imageSnap : imagesSnapshot.getChildren()) {
            String url = imageSnap.getValue(String.class);
            if (url != null) imageUrls.add(url);
        }
        return imageUrls;
    }

    // Extract floor list from snapshot
    public static List<String> extractFloors(DataSnapshot snapshot) {
        List<String> floors = new ArrayList<>();
        DataSnapshot floorsSnapshot = snapshot.child("floors");
        for (DataSnapshot floorSnap : floorsSnapshot.getChildren()) {
            String floor = floorSnap.getValue(String.class);
            if (floor != null) floors.add(floor);
        }
        return floors;
    }

    // Adds a hostel with image URLs and auto-generates rooms/floors
    public static void addHostel(DatabaseReference hostelsRef, String name, List<String> images) {
        HashMap<String, Object> hostelData = new HashMap<>();
        hostelData.put("name", name);
        hostelData.put("images", images);

        List<String> floors = getFloorsForHostel(name);
        hostelData.put("floors", floors);

        hostelsRef.child(name).setValue(hostelData)
                .addOnSuccessListener(aVoid -> {
                    Log.d("FirebaseHelper", name + " added successfully");
                    addRoomsToAllFloors(hostelsRef, name);
                })
                .addOnFailureListener(e -> Log.e("FirebaseHelper", "Failed to add " + name, e));
    }

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
                return Arrays.asList("Ground Floor", "First Floor");
        }
    }

    private static final Map<String, Map<String, Integer>> floorRoomCounts = new HashMap<>();
    private static final Map<String, List<String>> bookedRoomsMap = new HashMap<>();

    static {
        floorRoomCounts.put("Soudamini", Map.of(
                "Ground Floor", 10,
                "First Floor", 12,
                "Second Floor", 14,
                "Third Floor", 16
        ));

        floorRoomCounts.put("Seethamma", Map.of(
                "Ground Floor", 10,
                "First Floor", 12,
                "Second Floor", 13,
                "Third Floor", 15
        ));

        floorRoomCounts.put("Samyutha", Map.of(
                "First Floor", 8,
                "Second Floor", 9,
                "Third Floor", 10,
                "Fourth Floor", 11
        ));

        floorRoomCounts.put("Saradha", Map.of(
                "First Floor", 10,
                "Second Floor", 11,
                "Third Floor", 12,
                "Fourth Floor", 13
        ));

        floorRoomCounts.put("Saraswathi", Map.of(
                "First Floor", 6,
                "Second Floor", 7
        ));

        floorRoomCounts.put("Sravani", Map.of(
                "Ground Floor", 5
        ));

        bookedRoomsMap.put("Room 001", Arrays.asList("Ground Floor", "First Floor"));
        bookedRoomsMap.put("Room 003", Arrays.asList("Second Floor", "Third Floor"));
        bookedRoomsMap.put("Room 005", Arrays.asList("First Floor", "Fourth Floor"));
        bookedRoomsMap.put("Room 007", Arrays.asList("Second Floor"));
        bookedRoomsMap.put("Room 010", Arrays.asList("Third Floor", "Fourth Floor"));
    }

    public static void addRoomsToAllFloors(DatabaseReference hostelsRef, String hostelName) {
        Map<String, Integer> hostelFloors = floorRoomCounts.get(hostelName);
        if (hostelFloors != null) {
            for (Map.Entry<String, Integer> entry : hostelFloors.entrySet()) {
                addCustomRoomsToFloor(hostelsRef, hostelName, entry.getKey(), entry.getValue());
            }
        }
    }

    public static void addCustomRoomsToFloor(DatabaseReference hostelsRef, String hostelName, String floorName, int roomCount) {
        HashMap<String, Object> rooms = new HashMap<>();

        for (int i = 1; i <= roomCount; i++) {
            String roomNumber = "Room " + String.format("%03d", i);
            String status = "available";

            if (bookedRoomsMap.containsKey(roomNumber) && bookedRoomsMap.get(roomNumber).contains(floorName)) {
                status = "booked";
            }

            rooms.put(roomNumber, status);
        }

        hostelsRef.child(hostelName).child(floorName).setValue(rooms)
                .addOnSuccessListener(aVoid ->
                        Log.d("FirebaseHelper", "Rooms added to " + hostelName + " - " + floorName))
                .addOnFailureListener(e ->
                        Log.e("FirebaseHelper", "Failed to add rooms to " + floorName, e));
    }

    public static List<String> getDefaultImagesFor(String hostelName) {
        switch (hostelName) {
            case "Soudamini":
                return Arrays.asList("seethammar4", "samyutha", "sravani");
            case "Samyutha":
                return Arrays.asList("samyutha_r1", "samyutha_r2", "samyutha_r3");
            case "Saraswathi":
                return Arrays.asList("saraswathir1", "saraswathir2", "saraswathir3");
            case "Saradha":
                return Arrays.asList("saradha", "samyutha", "saraswathi");
            case "Seethamma":
                return Arrays.asList("seethamma", "seethammar1", "seethammar2");
            case "Sravani":
                return Arrays.asList("seethammar1", "sravani", "seethammaf3");
            default:
                return Arrays.asList("default1", "default2", "default3");
        }
    }
}
