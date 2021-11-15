package com.example.drawforme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // == bottom nav ==
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        // == bottom nav ==

        //FrameLayout에 fragment 띄우기
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new homeFrag()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new homeFrag()).commit();
                        break;

                    case R.id.page_2:
                        Intent addAc = new Intent(getApplicationContext(), AddActivity.class);
                        startActivity(addAc);
                        break;

                    case R.id.page_3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new listFrag()).commit();
                        break;

                    case R.id.page_4:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new profileFrag()).commit();
                        break;

                }
                return true;
            }
        });
    }
}
