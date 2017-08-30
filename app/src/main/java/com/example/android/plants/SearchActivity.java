package com.example.android.plants;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    GridView androidGridView;
    ArrayList<String> gridViewNames = new ArrayList<>();
    ArrayList<String> gridViewImages = new ArrayList<>();
    ArrayList<String> gridViewNamesBackup = new ArrayList<>();
    ArrayList<String> gridViewImagesBackup = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // define an adapter for grid view
        final CustomGridViewActivity adapterViewAndroid = new CustomGridViewActivity(SearchActivity.this, gridViewNames, gridViewImages);
        androidGridView=(GridView)findViewById(R.id.grid_view_image_text);
        androidGridView.setAdapter(adapterViewAndroid);
        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                String plant_name = gridViewNames.get(+i);
                Intent intent = new Intent(SearchActivity.this, PlantActivity.class);
                // add plant name to intent for future database access
                intent.putExtra("plant_name", plant_name);
                startActivity(intent);
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Read from the database
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            // fill arrays with names and images
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot parent : dataSnapshot.getChildren()) {
                    String plantName = parent.getKey();
                    gridViewNames.add(plantName);
                    gridViewNamesBackup.add(plantName);
                    String plant_img_url = parent.child("img_url").getValue().toString();
                    gridViewImages.add(plant_img_url);
                    gridViewImagesBackup.add(plant_img_url);
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
                gridViewNames.clear();
                gridViewImages.clear();

                for (int i=0; i < gridViewNamesBackup.size(); i++) {
                    String name = gridViewNamesBackup.get(i);
                    String url = gridViewImagesBackup.get(i);
                    if (name.startsWith(mEdit.toString())) {
                        gridViewNames.add(name);
                        gridViewImages.add(url);
                    }
                }
                adapterViewAndroid.notifyDataSetChanged();

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            public void onTextChanged(CharSequence s, int start, int before, int count){
            }
        });
    }


}
