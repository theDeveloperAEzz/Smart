package com.example.thedeveloper.smart.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.thedeveloper.smart.R;
import com.example.thedeveloper.smart.fragment.WelcomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, Main2Activity.class));
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragment = null;
        if (savedInstanceState == null) {
            fragment = new WelcomeFragment();
            ft.add(R.id.content_frame, fragment).commit();
        }
    }
}
