package edu.cse470.kytreegame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
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
    private boolean isGameWon = false;
    private int currentStreak = 0;
    private TextView highScoreTextView;
    private TextView highScoreLabelTextView;
    private TextView streakLabelTextView;
    private TextView streakTextView;

    private String correctTreeName;

    private String incorrectTreeName1;

    private String incorrectTreeName2;

    private String correctImagePath1;

    private String correctImagePath2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tree_guessing_game);

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.coffee_statusbar));
        window.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.coffee)));



        highScoreLabelTextView = findViewById(R.id.highScoreLabelTextView);
        highScoreTextView = findViewById(R.id.highScoreTextView);

        streakLabelTextView = findViewById(R.id.streakLabelTextView);
        streakTextView = findViewById(R.id.streakTextView);

        treeImageView1 = findViewById(R.id.treeImageView1);
        treeImageView2 = findViewById(R.id.treeImageView2);

        feedbackTextView = findViewById(R.id.feedbackTextView);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);

        ImageView backButtonImageView = findViewById(R.id.backToMainMenuButton);
        backButtonImageView.setOnClickListener(v -> {
            Intent intent = new Intent(TreeGuessingGame.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        random = new Random();

        try {
            AssetManager assetManager = getAssets();
            treeFolders = Arrays.asList(Objects.requireNonNull(assetManager.list("trees")));
        } catch (IOException e) {
            Log.e("MainActivity, onCreate(),", "An error occurred", e);
        }

        loadStats();
        startGame();
    }



    private void loadStats(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int highScore = sharedPreferences.getInt("highScore", 0);

        currentStreak = sharedPreferences.getInt("currentStreak", 0);
        streakTextView.setText(String.valueOf(currentStreak));
        highScoreTextView.setText(String.valueOf(highScore));
        highScoreLabelTextView.setText(" HIGH\nSCORE");
        streakLabelTextView.setText("    THIS\n STREAK");
    }




    private void startGame() {
        feedbackTextView.setText("");
        resetButtonColors();

        newCorrectTree();
        newWrongTrees();

        List<String> shuffledImages = shuffledFolder();
        loadImages(shuffledImages);
        loadButtons();
    }


    private void newCorrectTree(){
        int randomIndex = random.nextInt(treeFolders.size());
        correctTreeName = treeFolders.get(randomIndex);
    }


    private void newWrongTrees(){
        List<String> allTreeNames = new ArrayList<>(treeFolders);
        allTreeNames.remove(correctTreeName);
        Collections.shuffle(allTreeNames);
        incorrectTreeName1 = allTreeNames.get(0);
        incorrectTreeName2 = allTreeNames.get(1);
    }


    private void loadImages(List<String> chosenImages) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        sharedPreferences.getString("correctImagePath1", "-1");
        sharedPreferences.getString("correctImagePath2", "-1");
        try {
            AssetManager assetManager = getAssets();
            int index1 = random.nextInt(2);
            int index2;
            do {
                index2 = random.nextInt(2);
            } while (index2 == index1);

            correctImagePath1 = "trees/" + correctTreeName + "/" + chosenImages.get(index1);
            correctImagePath2 = "trees/" + correctTreeName + "/" + chosenImages.get(index2);
            saveCorrectImagePaths();

            InputStream inputStream1 = assetManager.open(correctImagePath1);
            InputStream inputStream2 = assetManager.open(correctImagePath2);

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


    private void loadButtons(){
        List<String> buttonOptions = Arrays.asList(correctTreeName, incorrectTreeName1, incorrectTreeName2);
        Collections.shuffle(buttonOptions);
        button1.setText(buttonOptions.get(0));
        button2.setText(buttonOptions.get(1));
        button3.setText(buttonOptions.get(2));
    }


    private List<String> shuffledFolder(){
        String[] imageFiles = new String[0];
        try {
            AssetManager assetManager = getAssets();
            imageFiles = assetManager.list("trees/" + correctTreeName);
        } catch (IOException e) {
            Log.e("MainActivity, startGame(),", "An error occurred", e);
        }
        assert imageFiles != null;
        List<String> imageFileList = Arrays.asList(imageFiles);
        Collections.shuffle(imageFileList);

        return imageFileList.subList(0, 2);
    }


    private void saveCorrectImagePaths(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("correctImagePath1", correctImagePath1);
        editor.putString("correctImagePath2", correctImagePath2);
        editor.apply();
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




    public void onTreeButtonClick(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

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

            if (currentStreak > sharedPreferences.getInt("highScore", 0)) {
                saveHighScore(currentStreak);
                loadStats();
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
