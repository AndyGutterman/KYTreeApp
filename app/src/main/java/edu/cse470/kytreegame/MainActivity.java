package edu.cse470.kytreegame;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Color;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ImageView treeImageView1;
    private ImageView treeImageView2;
    private Button button1;
    private Button button2;
    private Button button3;
    private TextView feedbackTextView;
    private Random random;
    private List<String> treeFolders;
    private String currentTreeFolder;
    private List<String> currentTreeNames;
    private String correctTreeName;

    private int currentStreak = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.deep_blue));


        // Initialize the views
        treeImageView1 = findViewById(R.id.treeImageView1);
        treeImageView2 = findViewById(R.id.treeImageView2);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        feedbackTextView = findViewById(R.id.feedbackTextView);

        // Initialize the random object
        random = new Random();

        // Initialize the tree folders from assets
        try {
            AssetManager assetManager = getAssets();
            treeFolders = Arrays.asList(assetManager.list("trees"));
            startGame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to start a new game
    private void startGame() {
        // Reset the game won state
        isGameWon = false;

        // Clear the feedback text
        feedbackTextView.setText("");

        // Reset the background colors and text colors of the buttons to their default state
        resetButtonColors();

        // Select a random folder from tree folders to be the correct tree
        int randomIndex = random.nextInt(treeFolders.size());
        currentTreeFolder = treeFolders.get(randomIndex);
        correctTreeName = currentTreeFolder;

        // Retrieve all tree names excluding the correct tree name
        List<String> allTreeNames = new ArrayList<>(treeFolders);
        allTreeNames.remove(correctTreeName);

        // Shuffle the list of tree names and choose two incorrect tree names
        Collections.shuffle(allTreeNames);
        String incorrectTreeName1 = allTreeNames.get(0);
        String incorrectTreeName2 = allTreeNames.get(1);

        // Assign correct and incorrect tree names to buttons
        List<String> buttonOptions = Arrays.asList(correctTreeName, incorrectTreeName1, incorrectTreeName2);
        Collections.shuffle(buttonOptions);

        button1.setText(buttonOptions.get(0));
        button2.setText(buttonOptions.get(1));
        button3.setText(buttonOptions.get(2));

        // List the image files in the chosen folder
        String[] imageFiles;
        try {
            AssetManager assetManager = getAssets();
            imageFiles = assetManager.list("trees/" + currentTreeFolder);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Select three distinct image files from the current folder
        List<String> imageFileList = Arrays.asList(imageFiles);
        Collections.shuffle(imageFileList);
        List<String> chosenImages = imageFileList.subList(0, 3);

        // Display the images for the current folder
        displayRandomTreeImages(chosenImages);
    }// Function to reset button colors to default

    public void onTreeButtonClick(View view) {
        // Get the clicked button
        Button clickedButton = (Button) view;
        // Get the selected tree name from the button text
        String selectedTreeName = clickedButton.getText().toString();

        if (isGameWon) {
            // If the game has been won and the correct button is clicked again, start a new game
            startGame();
            // Reset the isGameWon flag
            isGameWon = false;
            return; // Exit the function
        }

        // Check if the selected tree name matches the correct tree name
        if (selectedTreeName.equals(correctTreeName)) {
            // Correct answer
            // Increment the streak counter
            currentStreak++;

            // Set the feedback text to include the current streak
            feedbackTextView.setText("Correct! (Streak: " + currentStreak + ")\nThis is a " + correctTreeName + ".");
            feedbackTextView.setTextColor(getResources().getColor(R.color.custom_green));

            // Change the button background color to custom green and text color to white
            clickedButton.setBackgroundColor(getResources().getColor(R.color.custom_green));
            clickedButton.setTextColor(getResources().getColor(R.color.custom_text_white));

            // Set isGameWon to true to indicate that the game has been won
            isGameWon = true;
        } else {
            // Incorrect answer
            // Reset the streak counter
            currentStreak = 0;

            // Set the feedback text
            feedbackTextView.setText("Incorrect\nThis is not a " + selectedTreeName + " tree.");
            feedbackTextView.setTextColor(getResources().getColor(R.color.custom_red));

            // Change the button background color to custom red and text color to white
            clickedButton.setBackgroundColor(getResources().getColor(R.color.custom_red));
            clickedButton.setTextColor(getResources().getColor(R.color.custom_text_white));
        }
    }

    private void resetButtonColors() {
        // Get the deep blue color from the color resource
        int defaultButtonColor = getResources().getColor(R.color.deep_blue);
        int defaultTextColor = getResources().getColor(android.R.color.white); // White text color

        // Set the background and text colors of the buttons
        button1.setBackgroundColor(defaultButtonColor);
        button1.setTextColor(defaultTextColor);
        button2.setBackgroundColor(defaultButtonColor);
        button2.setTextColor(defaultTextColor);
        button3.setBackgroundColor(defaultButtonColor);
        button3.setTextColor(defaultTextColor);
    }




    // Function to display random tree images
    private void displayRandomTreeImages(List<String> chosenImages) {
        try {
            AssetManager assetManager = getAssets();

            // Open the image files as InputStreams using the chosen image file names
            InputStream inputStream1 = assetManager.open("trees/" + currentTreeFolder + "/" + chosenImages.get(0));
            InputStream inputStream2 = assetManager.open("trees/" + currentTreeFolder + "/" + chosenImages.get(1));

            // Decode the images and set them to the ImageViews
            Bitmap bitmap1 = BitmapFactory.decodeStream(inputStream1);
            Bitmap bitmap2 = BitmapFactory.decodeStream(inputStream2);
            treeImageView1.setImageBitmap(bitmap1);
            treeImageView2.setImageBitmap(bitmap2);

            // Close the InputStreams
            inputStream1.close();
            inputStream2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Function to handle button clicks

    private boolean isGameWon = false; // Boolean variable to track if the game is in a winning state





}
