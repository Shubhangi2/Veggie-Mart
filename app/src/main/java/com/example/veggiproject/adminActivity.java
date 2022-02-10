package com.example.veggiproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class adminActivity extends AppCompatActivity {
    private TextView addItem, signOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        addItem = findViewById(R.id.admin_add_items);
        signOut = findViewById(R.id.admin_sign_out);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Ready to add new item", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(adminActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminActivity.this, signout_admin.class);
                startActivity(intent);
                finish();
            }
        });


    }
}