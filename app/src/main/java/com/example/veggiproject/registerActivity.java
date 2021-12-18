package com.example.veggiproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;

public class registerActivity extends AppCompatActivity {
    public static boolean on_reset_password_fragment = false;
    public FrameLayout frame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        frame = findViewById(R.id.frameLayout);
        setFragment(new sign_in_fragment());

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(on_reset_password_fragment){
                on_reset_password_fragment = false;
                setFragment(new sign_in_fragment());
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setFragment(sign_in_fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frame.getId(), fragment);
        fragmentTransaction.commit();
    }
}