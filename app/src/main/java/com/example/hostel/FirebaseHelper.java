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
    public static List<String> extractFacilities(DataSnapshot snapshot) {
        List<String> facilityImages = new ArrayList<>();
        for (DataSnapshot item : snapshot.getChildren()) {
            String url = item.getValue(String.class);
            if (url != null) {
                facilityImages.add(url);
            }
        }
        return facilityImages;
    }

    public static String getFacilityDescriptionFor(String hostelName) {
        switch (hostelName) {
            case "Soudamini":
                return "• Spacious rooms\n\n• Clean bathrooms\n\n• Peaceful environment for study and relaxation";
            case "Samyutha":
                return "• Modern amenities\n\n• 24/7 Wi-Fi\n\n• Secure access, ideal for tech-savvy students";
            case "Saraswathi":
                return "• Friendly atmosphere\n\n• Well-maintained kitchen\n\n• Well-maintained laundry areas";
            case "Saradha":
                return "• Beautiful garden\n\n• Regular cleaning\n\n• Close to academic blocks";
            case "Seethamma":
                return "• Well-furnished rooms\n\n• Reading rooms\n\n• Calm surroundings";
            case "Sravani":
                return "• Compact yet cozy\n\n• All essential facilities\n\n• Dedicated common area";
            default:
                return "This hostel offers all basic amenities, ensuring a safe and comfortable stay for students.";
        }
    }

    // Adds a hostel with image URLs and auto-generates rooms/floors
    public static void addHostel(DatabaseReference hostelsRef, String name, List<String> images) {
        HashMap<String, Object> hostelData = new HashMap<>();
        hostelData.put("name", name);
        hostelData.put("images", images);

        List<String> floors = getFloorsForHostel(name);
        hostelData.put("floors", floors);

        List<String> facilities = getDefaultFacilitiesFor(name);
        hostelData.put("facilities", facilities);

        String description = getFacilityDescriptionFor(name);
        hostelData.put("facilityDescription", description);


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

    // NEW: Default facilities for each hostel
    public static List<String> getDefaultFacilitiesFor(String hostelName) {
        switch (hostelName) {
            case "Soudamini":
                return Arrays.asList("saraswathif5", "saraswathif6", "saraswathif7", "saraswathif8");
            case "Samyutha":
                return Arrays.asList("samyutha_f1", "samyutha_f2", "samyutha_f3", "samyutha_f4", "samyutha_f5");
            case "Saraswathi":
                return Arrays.asList("saraswathif1", "saraswathif2", "saraswathif3", "saraswathif4");
            case "Saradha":
                return Arrays.asList("saradha_f1", "saradha_f2", "saradha_f3", "saradha_f4");
            case "Seethamma":
                return Arrays.asList("seethammaf2", "seethammaf12", "seethammaf6");
            case "Sravani":
                return Arrays.asList("samyutha_f6", "samyutha_f7", "samyutha_f8");
            default:
                return Arrays.asList("seethammaf10", "seethammaf11");
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
        Map<String, String> roomStatusMap = new HashMap<>();

        for (int i = 1; i <= roomCount; i++) {
            String roomNumber = "Room " + String.format("%03d", i);
            String status = "available";

            if (bookedRoomsMap.containsKey(roomNumber) && bookedRoomsMap.get(roomNumber).contains(floorName)) {
                status = "booked";
            }

            rooms.put(roomNumber, status);
            roomStatusMap.put(roomNumber, status); // save for bunk generation later
        }

        hostelsRef.child(hostelName).child(floorName).setValue(rooms)
                .addOnSuccessListener(aVoid -> {
                    Log.d("FirebaseHelper", "Rooms added to " + hostelName + " - " + floorName);
                    for (Map.Entry<String, String> entry : roomStatusMap.entrySet()) {
                        addBunksToRoom(hostelsRef, hostelName, floorName, entry.getKey(), entry.getValue());
                    }
                })
                .addOnFailureListener(e ->
                        Log.e("FirebaseHelper", "Failed to add rooms to " + floorName, e));
    }

    public static void addBunksToRoom(DatabaseReference hostelsRef, String hostelName, String floorName, String roomName, String roomStatus) {
        Map<String, String> bunks = new HashMap<>();
        bunks.put("Upper 1", roomStatus);
        bunks.put("Upper 2", roomStatus);
        bunks.put("Lower 1", roomStatus);
        bunks.put("Lower 2", roomStatus);

        hostelsRef.child(hostelName)
                .child(floorName)
                .child(roomName + "_bunks")
                .setValue(bunks)
                .addOnSuccessListener(aVoid ->
                        Log.d("FirebaseHelper", "Bunks added for " + roomName))
                .addOnFailureListener(e ->
                        Log.e("FirebaseHelper", "Failed to add bunks for " + roomName, e));
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
                return Arrays.asList("seethamma", "seethammar1", "seethammar2", "seethammar6");
            case "Sravani":
                return Arrays.asList("seethammar5", "seethammar4", "seethammaf3");
            default:
                return Arrays.asList("seethamma", "sravani", "saraswathi");
        }
    }
}
