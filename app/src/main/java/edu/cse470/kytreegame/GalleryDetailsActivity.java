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
    private TextView licensesTextView; // Add reference to the TextView for licenses

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

        // Initialize TextView array with references to TextViews for image names
        imageTextViews = new TextView[]{
                findViewById(R.id.imageNameTextView1),
                findViewById(R.id.imageNameTextView2),
                findViewById(R.id.imageNameTextView3),
                findViewById(R.id.imageNameTextView4),
                findViewById(R.id.imageNameTextView5),
                findViewById(R.id.imageNameTextView6)
        };

        // Find TextView for tree name
        treeNameTextView = findViewById(R.id.treeNameTextView);

        // Find TextView for licenses
        licensesTextView = findViewById(R.id.licensesTextView);

        // Load image into ImageViews and update image names
        loadImage();

        // Display tree name
        displayTreeName();

        // Display licenses
        displayLicenses();
    }

    private void loadImage() {
        AssetManager assetManager = getAssets();
        String treeName = getIntent().getStringExtra("treeName");

        // Construct path to the image folder
        String imagePath = "licensed_trees/" + treeName + "/";

        // Try to open the image files
        try {
            // Get all image files in the folder
            String[] imageFiles = assetManager.list(imagePath);
            if (imageFiles != null) {
                // If there are no image files, hide the license TextView
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
                    int loadedImageCount = 0; // Track the number of loaded images
                    for (String fileName : imageFiles) {
                        if (!fileName.endsWith(".txt")) { // Ignore .txt files
                            String imageName = fileName.replaceFirst("[.][^.]+$", "");
                            String fullImagePath = imagePath + fileName;
                            try (InputStream inputStream = assetManager.open(fullImagePath)) {
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                // Set bitmap to ImageView
                                imageViews[loadedImageCount].setImageBitmap(bitmap);
                                // Update image name TextView without the file extension
                                String caption = "\n\"" + imageName + "\"";
                                imageTextViews[loadedImageCount].setText(caption);
                                loadedImageCount++; // Increment the loaded image count
                                if (loadedImageCount >= imageViews.length) {
                                    return; // All images loaded, exit the loop
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            // If an error occurs, set the corresponding ImageView to invisible
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[i].setVisibility(View.INVISIBLE);
                imageTextViews[i].setVisibility(View.INVISIBLE);
            }
            // Hide the license TextView
            licensesTextView.setVisibility(View.GONE);
            Log.e("GalleryDetailsActivity", "Error loading image: " + e.getMessage()); // Log the error
        }
    }


    private void displayLicenses() {
        AssetManager assetManager = getAssets();
        String treeName = getIntent().getStringExtra("treeName"); // Get the tree name passed from GalleryActivity
        try {
            // Construct the file path for the license based on the tree name
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

            // Set licenses to the TextView
            licensesTextView.setText(licenses.toString());
        } catch (IOException e) {
            Log.e("GalleryDetailsActivity", "Error loading licenses: " + e.getMessage()); // Log the error
        }
    }



    private void displayTreeName() {
        // Get tree name passed from GalleryActivity
        String treeName = getIntent().getStringExtra("treeName");

        // Find the corresponding scientific name from treesScientific.xml
        String scientificName = getScientificName(treeName);

        // Construct the display text
        String displayText = treeName + " \n(" + scientificName + ")";

        // Set tree name to the TextView
        treeNameTextView.setText(displayText);
    }

    private String getScientificName(String treeName) {
        String[] treeNames = getResources().getStringArray(R.array.tree_names);
        String[] treeNamesScientific = getResources().getStringArray(R.array.tree_names_scientific);

        // Find the index of the treeName in the non-scientific array
        int index = Arrays.asList(treeNames).indexOf(treeName);

        // If index is found, return the corresponding scientific name
        if (index != -1 && index < treeNamesScientific.length) {
            return treeNamesScientific[index];
        } else {
            return "Scientific name not found";
        }
    }





}
