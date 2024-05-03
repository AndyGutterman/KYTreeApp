package edu.cse470.kytreegame;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, treeNames);
        listView.setAdapter(adapter);

        // Set click listener for list items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click, navigate to GalleryDetailsActivity
                String treeName = treeNames.get(position);
                Intent intent = new Intent(GalleryActivity.this, GalleryDetailsActivity.class);
                intent.putExtra("treeName", treeName); // Pass tree name to details activity if needed
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
