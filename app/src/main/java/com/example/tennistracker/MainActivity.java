package com.example.tennistracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    AddNewMatchFragment addNewMatchFragment = new AddNewMatchFragment();
    MatchListFragment matchListFragment = new MatchListFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Tennis tracker");

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.page_match_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // take picture and save it locally here
        if (item.getItemId() == R.id.take_picture) {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivity(intent);
            return true;
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.page_match_list:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_frame_layout, matchListFragment).commit();
                return true;
            case R.id.page_add_new_match:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_frame_layout, addNewMatchFragment).commit();
                return true;
        }
        return false;
    }
}