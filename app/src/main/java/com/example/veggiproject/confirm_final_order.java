package com.example.veggiproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class confirm_final_order extends AppCompatActivity {
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        str = getIntent().getStringExtra("Total Price");
        Toast.makeText(getApplicationContext(), "total amount: "+str, Toast.LENGTH_SHORT).show();
    }
}