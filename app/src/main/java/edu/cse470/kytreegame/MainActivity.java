package edu.cse470.kytreegame;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import edu.cse470.kytreegame.Gallery.GalleryActivity;
import edu.cse470.kytreegame.Game.GameActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Button startButton = findViewById(R.id.start_game_button);

        Configuration configuration = getResources().getConfiguration();
        int screenSize = configuration.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        if (screenSize == Configuration.SCREENLAYOUT_SIZE_SMALL || screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL) {

            TextView appNameTextView = findViewById(R.id.native_trees_textview);
            Button galleryButton = findViewById(R.id.tree_gallery_button);

            appNameTextView.setTextSize(18);
            startButton.setTextSize(16);
            galleryButton.setTextSize(16);

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) startButton.getLayoutParams();
            params.topMargin = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
            params.bottomMargin = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());

            startButton.setLayoutParams(params);

            ViewGroup.MarginLayoutParams appNameTextViewParams = (ViewGroup.MarginLayoutParams) appNameTextView.getLayoutParams();
            appNameTextViewParams.topMargin = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
            appNameTextView.setLayoutParams(appNameTextViewParams);
        }

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.goldenrod_gold));
        window.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.deep_blue)));

        startButton.setOnClickListener(v -> startGame());

        Button galleryButton = findViewById(R.id.tree_gallery_button);
        galleryButton.setOnClickListener(v -> openGalleryActivity());
    }

    private void openGalleryActivity() {
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivity(intent);
    }

    private void startGame() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}
