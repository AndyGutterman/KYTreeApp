package edu.cse470.kytreegame;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;


public class GalleryDetailsActivity extends AppCompatActivity {

    private ImageView[] imageViews;
    private TextView treeNameTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_details);

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.coffee_statusbar));
        window.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.coffee)));

        // Initialize ImageView array with references to ImageViews
        imageViews = new ImageView[]{
                findViewById(R.id.imageView1),
                findViewById(R.id.imageView2),
                findViewById(R.id.imageView3),
                findViewById(R.id.imageView4),
                findViewById(R.id.imageView5),
                findViewById(R.id.imageView6)
        };

        // Find TextView for tree name
        treeNameTextView = findViewById(R.id.treeNameTextView);

        // Load image into ImageViews
        loadImage();

        // Display tree name
        displayTreeName();
    }
    private void loadImage() {
        AssetManager assetManager = getAssets();
        String treeName = getIntent().getStringExtra("treeName");

        // Loop through each ImageView and set the corresponding image
        for (int i = 0; i < imageViews.length; i++) {
            // Construct path to the image
            String imagePath = "trees/" + treeName + "/image_" + (i + 1) + ".jpg";

            // Try to open the image file
            try (InputStream inputStream = assetManager.open(imagePath)) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                // Set bitmap to ImageView
                imageViews[i].setImageBitmap(bitmap);
            } catch (IOException e) {
                // If image file doesn't exist, set the corresponding ImageView to invisible
                imageViews[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void displayTreeName() {
        // Get tree name passed from GalleryActivity
        String treeName = getIntent().getStringExtra("treeName");

        // Set tree name to the TextView
        treeNameTextView.setText(treeName);
    }
}
