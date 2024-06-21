package edu.cse470.kytreegame.Game;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import edu.cse470.kytreegame.R;

public class GameSettingsActivity extends AppCompatActivity {

    private TextView highScoreTextView;
    private TextView highScoreLabelTextView;
    private TextView streakLabelTextView;
    private TextView streakTextView;

    private Button clearHighScoreButton;
    private Button clearStreakButton;

    private int currentStreak;
    private int highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settings);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.coffee_statusbar));
        window.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.coffee)));

        highScoreLabelTextView = findViewById(R.id.highScoreLabelTextView);
        highScoreTextView = findViewById(R.id.highScoreTextView);

        streakLabelTextView = findViewById(R.id.streakLabelTextView);
        streakTextView = findViewById(R.id.streakTextView);

        ImageView backButtonImageView = findViewById(R.id.backToMainMenuButton);

        backButtonImageView.setOnClickListener(v -> navigateBackToGame());

        clearHighScoreButton = findViewById(R.id.clearHighScoreButton);
        clearStreakButton = findViewById(R.id.clearStreakButton);

        clearHighScoreButton.setOnClickListener(v -> resetHighScore());
        clearStreakButton.setOnClickListener(v -> resetStreak());

        loadStats();

        Configuration configuration = getResources().getConfiguration();
        int screenSize = configuration.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        if (screenSize == Configuration.SCREENLAYOUT_SIZE_SMALL || screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            adjustLayoutForSmallDevices();
        }

        // Handle back button press using OnBackPressedDispatcher
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateBackToGame();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @SuppressLint("SetTextI18n")
    private void loadStats(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        highScore = sharedPreferences.getInt("highScore", 0);
        currentStreak = sharedPreferences.getInt("currentStreak", 0);
        streakTextView.setText(String.valueOf(currentStreak));
        highScoreTextView.setText(String.valueOf(highScore));
        highScoreLabelTextView.setText(" HIGH SCORE");
        streakLabelTextView.setText("    CURRENT STREAK");
    }

    private void resetHighScore(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        currentStreak = sharedPreferences.getInt("currentStreak", 0);
        highScore = sharedPreferences.getInt("highScore", 0);

        editor.putInt("highScore", Math.max(currentStreak, 0));
        editor.apply();
        loadStats();
    }

    private void resetStreak(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("currentStreak", 0);
        editor.apply();
        loadStats();
    }

    private void adjustLayoutForSmallDevices() {
        highScoreTextView.setTextSize(10);
        highScoreLabelTextView.setTextSize(10);
        streakTextView.setTextSize(10);
        streakLabelTextView.setTextSize(10);
        clearHighScoreButton.setTextSize(10);
        clearStreakButton.setTextSize(10);
    }

    private void navigateBackToGame() {
        Intent intent = new Intent(GameSettingsActivity.this, GameActivity.class);
        startActivity(intent);
        finish();
    }
}
