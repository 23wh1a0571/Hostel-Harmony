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
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;

public class Seethamma_Facilities extends AppCompatActivity {
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seethamma_facilities);

        //back button
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Seethamma_Facilities.this, Mainpage.class);
                startActivity(i);
            }
        });

        //Scrolling images
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        // Add images from drawable
        List<Integer> imageList = Arrays.asList(
                R.drawable.seethammaf1,
                R.drawable.seethammaf2,
                R.drawable.seethammaf3,
                R.drawable.seethammaf4,
                R.drawable.seethammaf5,
                R.drawable.seethammaf6,
                R.drawable.seethammaf7,
                R.drawable.seethammaf8,
                R.drawable.seethammaf9,
                R.drawable.seethammaf10,
                R.drawable.seethammaf11,
                R.drawable.seethammaf12
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


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.facility), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}