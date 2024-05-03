package edu.cse470.kytreegame;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        List<String> treeNames = getTreeNames();

        ListView listView = findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item_layout, treeNames);
        listView.setAdapter(adapter);

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.goldenrod_gold));
        window.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.coffee)));

        // Set click listener for list items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click, navigate to GalleryDetailsActivity
                String treeName = treeNames.get(position);
                Intent intent = new Intent(GalleryActivity.this, GalleryDetailsActivity.class);
                intent.putExtra("treeName", treeName); // Pass tree name to details activity
                startActivity(intent);
            }
        });
    }

    private List<String> getTreeNames() {
        List<String> treeFolders = null;

        try {
            AssetManager assetManager = getAssets();
            treeFolders = Arrays.asList(Objects.requireNonNull(assetManager.list("trees")));
        } catch (IOException e) {
            Log.e("GalleryActivity", "An error occurred", e);
        }

        return treeFolders;
    }
}
