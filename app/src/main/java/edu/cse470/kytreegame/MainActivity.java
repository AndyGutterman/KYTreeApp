package edu.cse470.kytreegame;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
    private boolean isGameWon = false;
    private int currentStreak = 0;
    private TextView highScoreTextView;
    private TextView highScoreLabelTextView;
    private TextView streakLabelTextView;
    private TextView streakTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            treeFolders = Arrays.asList(assetManager.list("trees"));
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            e.printStackTrace();
            return;
        }

        updateHighScoreText();
        currentStreak = loadStreak();


        // Select three distinct image files from the current folder
        List<String> imageFileList = Arrays.asList(imageFiles);
        Collections.shuffle(imageFileList);
        List<String> chosenImages = imageFileList.subList(0, 3);

        displayRandomTreeImages(chosenImages);
    }

    private void displayRandomTreeImages(List<String> chosenImages) {
        try {
            AssetManager assetManager = getAssets();
            List<String> imageFileList = new ArrayList<>(chosenImages); // Create a copy of chosenImages

            // Shuffle the list of images
            Collections.shuffle(imageFileList);

            // Ensure the indices of the two chosen images are different
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
            e.printStackTrace();
        }
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

    private Button wrongFeedback(String selectedTreeName, Button clickedButton){
        String article = startsWithVowel(selectedTreeName) ? "an" : "a";
        feedbackTextView.setText("This is not " + article + " " + selectedTreeName + " tree.");
        feedbackTextView.setTextColor(getResources().getColor(R.color.custom_red));

        clickedButton.setBackgroundColor(getResources().getColor(R.color.custom_red));
        clickedButton.setTextColor(getResources().getColor(R.color.custom_text_white));
        return clickedButton;
    }

    private Button correctFeedback(String incorrectTreeName, Button clickedButton){
        String article = startsWithVowel(incorrectTreeName) ? "an" : "a";
        feedbackTextView.setText("This is " + article + " " + incorrectTreeName + " tree.");
        feedbackTextView.setTextColor(getResources().getColor(R.color.custom_green));

        clickedButton.setBackgroundColor(getResources().getColor(R.color.custom_green));
        clickedButton.setTextColor(getResources().getColor(R.color.custom_text_white));
        return clickedButton;
    }

    private boolean startsWithVowel(String word) {
        // Convert the word to lowercase for case insensitivity
        word = word.toLowerCase();
        // Check if the first character is a vowel
        char firstChar = word.charAt(0);
        return firstChar == 'a' || firstChar == 'e' || firstChar == 'i' || firstChar == 'o' || firstChar == 'u';
    }

    private void resetButtonColors() {
        // Get the deep blue color from the color resource
        int defaultButtonColor = getResources().getColor(R.color.deep_blue);
        int defaultTextColor = getResources().getColor(android.R.color.white); // White text color

        button1.setBackgroundColor(defaultButtonColor);
        button1.setTextColor(defaultTextColor);
        button2.setBackgroundColor(defaultButtonColor);
        button2.setTextColor(defaultTextColor);
        button3.setBackgroundColor(defaultButtonColor);
        button3.setTextColor(defaultTextColor);
    }

}
