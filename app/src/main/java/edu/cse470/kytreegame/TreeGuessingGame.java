package edu.cse470.kytreegame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;


public class TreeGuessingGame extends AppCompatActivity {
    private ImageView treeImageView1;
    private ImageView treeImageView2;
    private Button button1;
    private Button button2;
    private Button button3;
    private TextView feedbackTextView;
    private Random random;
    private List<String> treeFolders;
    private String currentTreeFolder;
    private String correctTreeName;
    private boolean isGameWon = false;
    private int currentStreak = 0;
    private TextView highScoreTextView;
    private TextView highScoreLabelTextView;
    private TextView streakLabelTextView;
    private TextView streakTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tree_guessing_game);

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.deep_blue));

        highScoreLabelTextView = findViewById(R.id.highScoreLabelTextView);
        highScoreTextView = findViewById(R.id.highScoreTextView);

        streakLabelTextView = findViewById(R.id.streakLabelTextView);
        streakTextView = findViewById(R.id.streakTextView);
        currentStreak = loadStreak();

        treeImageView1 = findViewById(R.id.treeImageView1);
        treeImageView2 = findViewById(R.id.treeImageView2);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        feedbackTextView = findViewById(R.id.feedbackTextView);

        random = new Random();

        try {
            AssetManager assetManager = getAssets();
            treeFolders = Arrays.asList(Objects.requireNonNull(assetManager.list("trees")));
        } catch (IOException e) {
            Log.e("MainActivity, onCreate(),", "An error occurred", e);
        }
        setupBackToMainMenuButton();
        restoreGameState();
    }


    private void restoreGameState() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        currentTreeFolder = sharedPreferences.getString("currentTreeFolder", "");
        correctTreeName = sharedPreferences.getString("correctTreeName", "");
        currentStreak = sharedPreferences.getInt("currentStreak", 0);
        isGameWon = sharedPreferences.getBoolean("isGameWon", false);
        startGame();
    }

    private void startGame() {
        isGameWon = false;

        feedbackTextView.setText("");
        streakTextView.setText(String.valueOf(currentStreak));
        resetButtonColors();

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

        List<String> buttonOptions = Arrays.asList(correctTreeName, incorrectTreeName1, incorrectTreeName2);
        Collections.shuffle(buttonOptions);

        button1.setText(buttonOptions.get(0));
        button2.setText(buttonOptions.get(1));
        button3.setText(buttonOptions.get(2));

        String[] imageFiles;
        try {
            AssetManager assetManager = getAssets();
            imageFiles = assetManager.list("trees/" + currentTreeFolder);
        } catch (IOException e) {
            Log.e("MainActivity, startGame(),", "An error occurred", e);
            return;
        }

        updateHighScoreText();
        currentStreak = loadStreak();


        // Select three distinct image files from the current folder
        assert imageFiles != null;
        List<String> imageFileList = Arrays.asList(imageFiles);
        Collections.shuffle(imageFileList);
        List<String> chosenImages = imageFileList.subList(0, 3);

        displayRandomTreeImages(chosenImages);
    }

    private void displayRandomTreeImages(List<String> chosenImages) {
        try {
            AssetManager assetManager = getAssets();
            List<String> imageFileList = new ArrayList<>(chosenImages);

            // Shuffle the list of images
            Collections.shuffle(imageFileList);

            // Ensures the indices of the two chosen images are different
            int index1 = random.nextInt(imageFileList.size());
            int index2;
            do {
                index2 = random.nextInt(imageFileList.size());
            } while (index2 == index1);

            // Open the input streams for the chosen images
            InputStream inputStream1 = assetManager.open("trees/" + currentTreeFolder + "/" + imageFileList.get(index1));
            InputStream inputStream2 = assetManager.open("trees/" + currentTreeFolder + "/" + imageFileList.get(index2));

            // Decode the images and set them to the ImageViews
            Bitmap bitmap1 = BitmapFactory.decodeStream(inputStream1);
            Bitmap bitmap2 = BitmapFactory.decodeStream(inputStream2);
            treeImageView1.setImageBitmap(bitmap1);
            treeImageView2.setImageBitmap(bitmap2);

            inputStream1.close();
            inputStream2.close();
        } catch (IOException e) {
            Log.e("MainActivity, displayRandomTreeImages(),", "An error occurred", e);
        }
    }

    private void setupBackToMainMenuButton() {
        ImageView backButtonImageView = findViewById(R.id.backToMainMenuButton);
        backButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the main menu activity
                Intent intent = new Intent(TreeGuessingGame.this, MainActivity.class);
                startActivity(intent);
                finish(); // Finish the current activity to prevent going back to it
            }
        });
    }


    private int getHighScore() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("highScore", 0); // 0 is the default value if high score is not found
    }

    private int loadStreak() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("currentStreak", 0);
    }

    private void saveHighScore(int highScore) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("highScore", highScore);
        editor.apply();
    }

    private void saveStreak(int streak) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("currentStreak", streak);
        editor.apply();
    }


    private void updateHighScoreText() {
        int highScore = getHighScore();
        highScoreTextView.setText(String.valueOf(highScore));
        highScoreLabelTextView.setText(" HIGH\nSCORE");
        streakLabelTextView.setText("    THIS\n STREAK");
    }

    public void onTreeButtonClick(View view) {
        Button clickedButton = (Button) view;
        String selectedTreeName = clickedButton.getText().toString();

        if (isGameWon) {
            startGame();
            isGameWon = false;
            return;
        }

        if (selectedTreeName.equals(correctTreeName)) {
            currentStreak++;
            streakTextView.setText(String.valueOf(currentStreak));
            saveStreak(currentStreak);

            if (currentStreak > getHighScore()) {
                saveHighScore(currentStreak);
                updateHighScoreText();
            }
            correctFeedback(selectedTreeName, clickedButton);
            isGameWon = true;

        } else {
            // Reset streak to 0 when the user selects the wrong tree
            currentStreak = 0;
            streakTextView.setText(String.valueOf(currentStreak));
            saveStreak(currentStreak);
            wrongFeedback(selectedTreeName, clickedButton);
        }
    }

    private void wrongFeedback(String selectedTreeName, Button clickedButton){
        String article = startsWithVowel(selectedTreeName) ? "an" : "a";
        String feedback = "This is not " + article + " " + selectedTreeName + " tree.";
        feedbackTextView.setText(feedback);
        feedbackTextView.setTextColor(ContextCompat.getColor(this, R.color.custom_red));

        clickedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.custom_red));
        clickedButton.setTextColor(ContextCompat.getColor(this, R.color.custom_text_white));
    }

    private void correctFeedback(String incorrectTreeName, Button clickedButton){
        String article = startsWithVowel(incorrectTreeName) ? "an" : "a";
        String feedback = "This is " + article + " " + incorrectTreeName + " tree.";
        feedbackTextView.setText(feedback);
        feedbackTextView.setTextColor(ContextCompat.getColor(this, R.color.custom_green));

        clickedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.custom_green));
        clickedButton.setTextColor(ContextCompat.getColor(this, R.color.custom_text_white));
    }

    private boolean startsWithVowel(String word) {
        word = word.toLowerCase();
        char firstChar = word.charAt(0);
        return firstChar == 'a' || firstChar == 'e' || firstChar == 'i' || firstChar == 'o' || firstChar == 'u';
    }

    private void resetButtonColors() {
        int defaultButtonColor = ContextCompat.getColor(this, R.color.deep_blue);
        int defaultTextColor = ContextCompat.getColor(this, R.color.white);

        button1.setBackgroundColor(defaultButtonColor);
        button1.setTextColor(defaultTextColor);
        button2.setBackgroundColor(defaultButtonColor);
        button2.setTextColor(defaultTextColor);
        button3.setBackgroundColor(defaultButtonColor);
        button3.setTextColor(defaultTextColor);
    }
}
