package edu.cse470.kytreegame.Gallery;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import edu.cse470.kytreegame.R;

public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        List<String> treeNames = getTreeNames();

        ListView listView = findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item_layout, R.id.textView, treeNames);
        listView.setAdapter(adapter);

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.goldenrod_gold));
        window.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.coffee)));

        // Set click listener for list items
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Handle item click, navigate to GalleryDetailsActivity
            String treeName = treeNames.get(position);
            Intent intent = new Intent(GalleryActivity.this, GalleryDetailsActivity.class);
            intent.putExtra("treeName", treeName); // Pass tree name to details activity
            startActivity(intent);
        });
    }

    private List<String> getTreeNames() {
        List<String> treeFolders = null;

        try {
            AssetManager assetManager = getAssets();
            treeFolders = Arrays.asList(Objects.requireNonNull(assetManager.list("licensed_trees")));
        } catch (IOException e) {
            Log.e("GalleryActivity", "An error occurred", e);
        }

        return treeFolders;
    }
}
