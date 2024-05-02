package edu.cse470.kytreegame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Button startGameButton = findViewById(R.id.start_game_button);
        Button treeGalleryButton = findViewById(R.id.tree_gallery_button);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        treeGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //
            }
        });

        // Add onClickListeners for other buttons as needed
    }

    private void startGame() {
        // Start the game activity or perform other actions
        Intent intent = new Intent(this, TreeGuessingGame.class);
        startActivity(intent);
    }
}
