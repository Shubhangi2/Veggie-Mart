package com.example.veggiproject;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class profileJavaClass extends AppCompatActivity {
    private TextView userName, userEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header_main);
        
        userName = findViewById(R.id.nav_header_user_name);
        userEmail = findViewById(R.id.nav_header_user_email);
        
        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(profileJavaClass.this, "this is username", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
