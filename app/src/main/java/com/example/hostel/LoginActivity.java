package com.example.hostel;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button signInButton;
    private TextView registerText;
    private ImageButton back;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login); // Ensure login.xml exists in res/layout

        String redirectTo = getIntent().getStringExtra("redirect_to");
        String hostelName = getIntent().getStringExtra("hostelName");
        String floorName = getIntent().getStringExtra("floorName");
        String roomNumber = getIntent().getStringExtra("roomNumber");

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signInButton = findViewById(R.id.signInButton);
        registerText = findViewById(R.id.registerText);
        back = findViewById(R.id.imageButton); // 🔙 Back arrow (ImageButton)

        back.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
        });

        signInButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                            Intent redirectIntent;
                            if ("BunkSelectionActivity".equals(redirectTo)) {
                                redirectIntent = new Intent(LoginActivity.this, BunkSelectionActivity.class);
                                redirectIntent.putExtra("hostelName", hostelName);
                                redirectIntent.putExtra("floorName", floorName);
                                redirectIntent.putExtra("roomNumber", roomNumber);
                            } else {
                                redirectIntent = new Intent(LoginActivity.this, MainActivity.class); // default fallback
                            }

                            startActivity(redirectIntent);
                            finish();

                        } else {
                            Toast.makeText(LoginActivity.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        registerText.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}