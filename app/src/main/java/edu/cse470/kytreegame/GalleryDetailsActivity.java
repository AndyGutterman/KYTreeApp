package edu.cse470.kytreegame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

public class GalleryDetailsActivity extends AppCompatActivity {

    private ImageView[] imageViews;
    private TextView treeNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_details);

        // Initialize ImageView array with references to your ImageViews
        imageViews = new ImageView[]{
                findViewById(R.id.imageView1),
                findViewById(R.id.imageView2),
                findViewById(R.id.imageView3),
                findViewById(R.id.imageView4),
                findViewById(R.id.imageView5),
                findViewById(R.id.imageView6)
        };

        // Find the TextView for tree name
        treeNameTextView = findViewById(R.id.treeNameTextView);

        // Load the image into all ImageViews
        loadImage();

        // Display the tree name
        displayTreeName();
    }

    private void loadImage() {
        try {
            // Construct the path to the image
            String imagePath = "trees/Allegheny Serviceberry/image_1.jpg";

            // Load image from assets
            InputStream inputStream = getAssets().open(imagePath);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Loop through each ImageView and set the same image
            for (ImageView imageView : imageViews) {
                imageView.setImageBitmap(bitmap);
            }

            // Close the input stream after use
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayTreeName() {
        // Get the tree name passed from GalleryActivity
        String treeName = getIntent().getStringExtra("treeName");

        // Set the tree name to the TextView
        treeNameTextView.setText(treeName);
    }
}
