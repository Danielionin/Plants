package com.example.android.plants;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageButton searchButton;
    private ImageButton settingsButton;
    private ImageView no_plants_view;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private DatabaseReference mDatabase;
    private Button x;

    private static ArrayList<String> my_plants = new ArrayList<>();;

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

        if (!my_plants.isEmpty()) {
            no_plants_view = (ImageView) findViewById(R.id.no_plant_msg);
            no_plants_view.setVisibility(View.GONE);
        }
    }

    // all this part is to save the plant array on the sharedPrefrences
    // after we leave this activity
    @Override
    public void onPause(){
        super.onPause();
        prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        editor = prefs.edit();

        Set<String> set = new HashSet<String>();
        set.addAll(my_plants);
        editor.putStringSet("my_plants_set", set);
        editor.commit();
    }

    @Override
    // called when we get to this activity using back button
    public void onResume(){
        super.onResume();

        //get database
        mDatabase = FirebaseDatabase.getInstance().getReference("Plants");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                my_plants.clear();
                boolean hasPlants = false;
                for (DataSnapshot parent : dataSnapshot.getChildren()) {
                    String plant_name = parent.getKey();
                    if (parent.child("Planted").getValue(String.class).equals("True") && !my_plants.contains(plant_name)) {
                        hasPlants = true;
                        my_plants.add(plant_name);
                    }
                }
                mAdapter.notifyDataSetChanged();

                no_plants_view = (ImageView) findViewById(R.id.no_plant_msg);
                if (!hasPlants) {
                    no_plants_view.setVisibility(View.VISIBLE);
                } else {
                    no_plants_view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // get plant name from shared preferences (defined by add button in plant activity)
//        String plant_name = prefs.getString("plant_name", "no_id");
//        if (!plant_name.equals("no_id")) {
//            my_plants.add(plant_name);
//            editor = prefs.edit();
//            editor.remove("plant_name");
//            editor.apply();
//            mAdapter.notifyDataSetChanged();
//        }


    }
}
