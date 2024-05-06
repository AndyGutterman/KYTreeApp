package edu.cse470.kytreegame;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;


public class GalleryDetailsActivity extends AppCompatActivity {

    private ImageView[] imageViews;
    private TextView treeNameTextView;
    private TextView[] imageTextViews;
    private TextView licensesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_details);

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.coffee_statusbar));
        window.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.coffee)));

        imageViews = new ImageView[]{
                findViewById(R.id.imageView1),
                findViewById(R.id.imageView2),
                findViewById(R.id.imageView3),
                findViewById(R.id.imageView4),
                findViewById(R.id.imageView5),
                findViewById(R.id.imageView6)
        };

        imageTextViews = new TextView[]{
                findViewById(R.id.imageNameTextView1),
                findViewById(R.id.imageNameTextView2),
                findViewById(R.id.imageNameTextView3),
                findViewById(R.id.imageNameTextView4),
                findViewById(R.id.imageNameTextView5),
                findViewById(R.id.imageNameTextView6)
        };

        treeNameTextView = findViewById(R.id.treeNameTextView);
        licensesTextView = findViewById(R.id.licensesTextView);

        loadImage();
        displayTreeName();
        displayLicenses();
    }

    private void loadImage() {
        AssetManager assetManager = getAssets();
        String treeName = getIntent().getStringExtra("treeName");

        String imagePath = "licensed_trees/" + treeName + "/";

        try {
            String[] imageFiles = assetManager.list(imagePath);
            if (imageFiles != null) {
                if (imageFiles.length == 0) {
                    licensesTextView.setVisibility(View.GONE);
                }

                // If there's only one image, load it once
                if (imageFiles.length == 1) {
                    String fullImagePath = imagePath + imageFiles[0];
                    try (InputStream inputStream = assetManager.open(fullImagePath)) {
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageViews[0].setImageBitmap(bitmap);
                        imageTextViews[0].setText(imageFiles[0]);
                    }
                } else {
                    // Load multiple images
                    int loadedImageCount = 0; // Track number of loaded images
                    for (String fileName : imageFiles) {
                        if (!fileName.endsWith(".txt")) { // Ignore .txt files
                            String imageName = fileName.replaceFirst("[.][^.]+$", "");
                            String fullImagePath = imagePath + fileName;
                            try (InputStream inputStream = assetManager.open(fullImagePath)) {
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                // Set bitmap to ImageView
                                imageViews[loadedImageCount].setImageBitmap(bitmap);
                                String caption = "\n\"" + imageName + "\"";
                                imageTextViews[loadedImageCount].setText(caption);
                                loadedImageCount++;
                                if (loadedImageCount >= imageViews.length) {
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            // If error occurs, set corresponding ImageView to invisible
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[i].setVisibility(View.INVISIBLE);
                imageTextViews[i].setVisibility(View.INVISIBLE);
            }
            licensesTextView.setVisibility(View.GONE);
            Log.e("GalleryDetailsActivity", "Error loading image: " + e.getMessage()); // Log the error
        }
    }


    private void displayLicenses() {
        AssetManager assetManager = getAssets();
        String treeName = getIntent().getStringExtra("treeName"); // Get the tree name passed from GalleryActivity
        try {
            String filePath = "licensed_trees/" + treeName + "/image_names_urls_license.txt";
            InputStream inputStream = assetManager.open(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder licenses = new StringBuilder();
            licenses.append("Image Attributions:\n\n");
            String line;
            while ((line = reader.readLine()) != null) {
                licenses.append(line).append("\n");
            }
            reader.close();

            licensesTextView.setText(licenses.toString());
        } catch (IOException e) {
            Log.e("GalleryDetailsActivity", "Error loading licenses: " + e.getMessage()); // Log the error
        }
    }



    private void displayTreeName() {
        // Get name passed from GalleryActivity
        String treeName = getIntent().getStringExtra("treeName");
        String scientificName = getScientificName(treeName);
        String displayText = treeName + " \n(" + scientificName + ")";
        treeNameTextView.setText(displayText);
    }

    private String getScientificName(String treeName) {
        String[] treeNames = getResources().getStringArray(R.array.tree_names);
        String[] treeNamesScientific = getResources().getStringArray(R.array.tree_names_scientific);

        // Find the index of the treeName in the non-scientific array
        int index = Arrays.asList(treeNames).indexOf(treeName);

        if (index != -1 && index < treeNamesScientific.length) {
            return treeNamesScientific[index];
        } else {
            return "Scientific name not found";
        }
    }


}
