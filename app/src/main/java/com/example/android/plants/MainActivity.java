package com.example.android.plants;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageButton searchButton;
    private ImageButton settingsButton;
    private ImageView noPlantsMessage;

    private static ArrayList<String> my_plants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new RecyclerAdapter(my_plants);
        mRecyclerView.setAdapter(mAdapter);

        // takes us to search page
        searchButton = (ImageButton) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(i);
            }
        });

        // take us to settings page
        settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
            }
        });



        // get plant name if we're coming from plant page (added plant)
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("plant_name")) {
                String plant_name = extras.getString("plant_name");
                my_plants.add(plant_name);
            }
        }

        noPlantsMessage = (ImageView) findViewById(R.id.no_plants_text);
        noPlantsMessage.setVisibility(View.INVISIBLE);
        if(my_plants.isEmpty()) {
            noPlantsMessage.setVisibility(View.VISIBLE);
        }
    }
}
