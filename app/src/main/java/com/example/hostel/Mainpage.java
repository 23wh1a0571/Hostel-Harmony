package com.example.hostel;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hostel.ImageAdapter;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;

public class Mainpage extends AppCompatActivity {
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mainpage);

        //Back button implementation
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Mainpage.this, MainActivity.class);
                Toast.makeText(Mainpage.this, "success", Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });


        Spinner spinner = findViewById(R.id.mySpinner);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item,
                R.id.spinner_text, getResources().getStringArray(R.array.floors));


        adapter.setDropDownViewResource(R.layout.spinner_item);



        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                Toast.makeText(Mainpage.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
                if (selectedItem.equals("Floor 1")) {
                    Intent intent = new Intent(Mainpage.this, Seethamma_Floor.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        // Add images from drawable
        List<Integer> imageList = Arrays.asList(
                R.drawable.samyutha,
                R.drawable.saradha,
                R.drawable.sravani,
                R.drawable.saraswathi
        );


        viewPager.setAdapter(new ImageAdapter(this, imageList));


        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);



        // Fix: Disable excessive preloading
        viewPager.setOffscreenPageLimit(1);

        viewPager.setPageTransformer((page, position) -> {
            page.setAlpha(0.5f + (1 - Math.abs(position)) * 0.5f);
        });




        TabLayout tabLayout = findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {}).attach();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
