package com.example.veggiproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Fragment;
import android.os.Bundle;
import android.widget.FrameLayout;

public class registerActivity extends AppCompatActivity {
    public FrameLayout frame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        frame = findViewById(R.id.frameLayout);
        setFragment(new sign_in_fragment());

    }
    public void setFragment(sign_in_fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frame.getId(), fragment);
        fragmentTransaction.commit();
    }
}