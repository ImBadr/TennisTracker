package com.example.tennistracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MatchDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_details);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView player_1 = findViewById(R.id.player_1_name);
        TextView player_2 = findViewById(R.id.player_2_name);
        TextView player_1_score = findViewById(R.id.player_1_score);
        TextView player_2_score = findViewById(R.id.player_2_score);
        TextView location = findViewById(R.id.address);

        Intent intent = getIntent();

        player_1.setText(intent.getStringExtra("player1_name"));
        player_2.setText(intent.getStringExtra("player2_name"));
        player_1_score.setText(intent.getStringExtra("player1_match_score"));
        player_2_score.setText(intent.getStringExtra("player2_match_score"));
        location.setText(intent.getStringExtra("location"));

    }
}