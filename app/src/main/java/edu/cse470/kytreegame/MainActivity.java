package edu.cse470.kytreegame;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.graphics.drawable.ColorDrawable;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.goldenrod_gold));
        window.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.deep_blue)));

        Button startGameButton = findViewById(R.id.start_game_button);
        startGameButton.setOnClickListener(v -> startGame());

        Button galleryButton = findViewById(R.id.tree_gallery_button);
        galleryButton.setOnClickListener(v -> openGalleryActivity());
    }

    private void openGalleryActivity() {
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivity(intent);
    }
    private void startGame() {
        // Start the game activity or perform other actions
        Intent intent = new Intent(this, TreeGuessingGame.class);
        startActivity(intent);
    }
}
