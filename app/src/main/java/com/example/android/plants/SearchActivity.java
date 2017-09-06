package com.example.android.plants;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatabaseReference mDatabase;
    GridView androidGridView;
//    ArrayList<String> gridViewNames = new ArrayList<>();
//    ArrayList<String> gridViewImages = new ArrayList<>();
//    ArrayList<String> gridViewNamesBackup = new ArrayList<>();
//    ArrayList<String> gridViewImagesBackup = new ArrayList<>();
    ArrayList<Plant> gridViewArray = new ArrayList<>();
    ArrayList<Plant> gridViewArrayBackup = new ArrayList<>();
    String isPlanted;
    Plant plant;

    private Spinner spinner;
    private static final String[]paths = {"A-Z", "Z-A", "Difficulty"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // define "sort-by" drop-down menu
        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(SearchActivity.this,
                android.R.layout.simple_spinner_item,paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // define an adapter for grid view
        final CustomGridViewActivity adapterViewAndroid = new CustomGridViewActivity(SearchActivity.this, gridViewArray);
        androidGridView=(GridView)findViewById(R.id.grid_view_image_text);
        androidGridView.setAdapter(adapterViewAndroid);

        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                final String plant_name = gridViewArray.get(+i).plant_name;
                final Intent intent = new Intent(SearchActivity.this, PlantActivity.class);
                // add plant name to intent for future database access
                intent.putExtra("plant_name", plant_name);
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        // we add "is_planted" so the plant page will know what button to display ("add" or "remove")
                        isPlanted = dataSnapshot.child("Plants").child(plant_name).child("Planted").getValue().toString();
                        intent.putExtra("is_planted", isPlanted);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        // Read from the database
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            // fill arrays with names and images
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot parent : dataSnapshot.child("Plants").getChildren()) {
                    String plantName = parent.getKey();
                    //gridViewNames.add(plantName);
                    //gridViewNamesBackup.add(plantName);
                    String plant_img_url = parent.child("img_url").getValue().toString();
                    //gridViewImages.add(plant_img_url);
                    //gridViewImagesBackup.add(plant_img_url);
                    plant = new Plant(plantName, plant_img_url);
                    gridViewArray.add(plant);
                    gridViewArrayBackup.add(plant);
                }
                // update view after changes
                adapterViewAndroid.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w("SearchActivity", "Failed to read value.", databaseError.toException());
            }
        });

        EditText search_box = (EditText) findViewById(R.id.search_input);
        search_box.addTextChangedListener(new TextWatcher()
        {
            @Override
            // change the content of the adapter's arrays, based on user's search word
            public void afterTextChanged(Editable mEdit){
//                gridViewNames.clear();
//                gridViewImages.clear();
                gridViewArray.clear();

//                for (int i=0; i < gridViewNamesBackup.size(); i++) {
//                    String name = gridViewNamesBackup.get(i);
//                    String url = gridViewImagesBackup.get(i);
//                    if (name.startsWith(mEdit.toString())) {
//                        gridViewNames.add(name);
//                        gridViewImages.add(url);
//                    }
//                }
                for (int i=0; i < gridViewArrayBackup.size(); i++) {
                    String name = gridViewArrayBackup.get(i).plant_name;
                    String url = gridViewArrayBackup.get(i).url;
                    if (name.startsWith(mEdit.toString())) {
                        Plant temp = new Plant(name, url);
                        gridViewArray.add(temp);
                    }
                }
                adapterViewAndroid.notifyDataSetChanged();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            public void onTextChanged(CharSequence s, int start, int before, int count){
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                Toast.makeText(this, "hey1", Toast.LENGTH_SHORT).show();
                Collections.sort(gridViewArray, new Comparator<Plant>() {
                    public int compare(Plant o1, Plant o2) {
                        return o1.plant_name.compareTo(o2.plant_name);
                    }
                });
                break;
            case 1:
                Toast.makeText(this, "hey2", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
