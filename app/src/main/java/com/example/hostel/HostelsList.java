package com.example.hostel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HostelsList extends AppCompatActivity {
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hostels_list);

        // Click listeners for hostel buttons
        findViewById(R.id.Seethamma_button).setOnClickListener(v -> openHostelPage("Seethamma"));
        findViewById(R.id.Sravani_button).setOnClickListener(v -> openHostelPage("Sravani"));
        findViewById(R.id.Saradha_button).setOnClickListener(v -> openHostelPage("Saradha"));
        findViewById(R.id.Samyutha_button).setOnClickListener(v -> openHostelPage("Samyutha"));
        findViewById(R.id.Saraswathi_button).setOnClickListener(v -> openHostelPage("Saraswathi"));
        findViewById(R.id.Soudamini_button).setOnClickListener(v -> openHostelPage("Soudamini"));
        // Back button listener
        back = findViewById(R.id.imageButton);
        back.setOnClickListener(v -> {
            Intent i = new Intent(HostelsList.this, MainActivity.class);
            startActivity(i);
        });

        // Insets setup
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.hostel), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void openHostelPage(String hostelName) {
        Intent intent = new Intent(HostelsList.this, Mainpage.class);
        intent.putExtra("hostel_name", hostelName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); // 👈 Add this
        startActivity(intent);


    }
}
