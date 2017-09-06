package com.example.android.plants;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PlantActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    TextView titleView;
    TextView descriptionView;
    TextView how_to_plantView;
    TextView how_to_growView;
    ImageView imageView;
    ImageView difficultView;
    Button addButton;
    Button removeButton;
    Button sensorButton;
    TextView dateView;
    TextView wateredView;
    TextView descriptionTitleView;
    String addButtonStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant);

        //get plant name
        Bundle bundle = getIntent().getExtras();
        final String plant_name = bundle.getString("plant_name");
        String isPlanted = bundle.getString("is_planted");

        // assign views
        titleView = (TextView) findViewById(R.id.title);
        descriptionView = (TextView) findViewById(R.id.description);
        imageView = (ImageView) findViewById(R.id.image);
        how_to_plantView = (TextView) findViewById(R.id.how_to_plant);
        how_to_growView = (TextView) findViewById(R.id.how_to_grow);
        difficultView = (ImageView) findViewById(R.id.stars);
        addButton = (Button) findViewById(R.id.add_button);
        removeButton = (Button) findViewById(R.id.remove_button);
        sensorButton = (Button) findViewById(R.id.sensor_button);

        dateView = (TextView) findViewById(R.id.date);
        wateredView = (TextView) findViewById(R.id.watered);
        descriptionTitleView = (TextView) findViewById(R.id.description_title);

        //get database
        mDatabase = FirebaseDatabase.getInstance().getReference("Plants");
        final DatabaseReference plant_db = mDatabase.child(plant_name);

        //Read from the database
        plant_db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String description = dataSnapshot.child("Description").getValue(String.class);
                String plant_img = dataSnapshot.child("plant_img").getValue(String.class);
                String how_to_plant = dataSnapshot.child("How To Plant").getValue(String.class);
                String how_to_grow = dataSnapshot.child("How To Grow").getValue(String.class);
                int how_difficult = dataSnapshot.child("difficult").getValue(int.class);

                // assign text values to relevant views
                titleView.setText(plant_name);
                descriptionView.setText(description);
                how_to_plantView.setText(how_to_plant);
                how_to_growView.setText(how_to_grow);
                Picasso.with(PlantActivity.this).load(plant_img).into(imageView);
                // make R.drawable.imagename as integer and send to setImageSource
                int resourceId = getResources().getIdentifier("star" + Integer.toString(how_difficult), "drawable", getPackageName());
                difficultView.setImageResource(resourceId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w("SearchActivity", "Failed to read value.", databaseError.toException());
            }
        });

        if (isPlanted.equals("True")) {
            dateView.setVisibility(View.VISIBLE);
            wateredView.setVisibility(View.VISIBLE);
            addButton.setVisibility(View.GONE);
            removeButton.setVisibility(View.VISIBLE);
        } else {
            dateView.setVisibility(View.INVISIBLE);
            wateredView.setVisibility(View.INVISIBLE);
            removeButton.setVisibility(View.GONE);
            addButton.setVisibility(View.VISIBLE);
        }


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plant_db.child("Planted").setValue("True");
                Toast.makeText(PlantActivity.this, "Plant added", Toast.LENGTH_SHORT).show();
                dateView.setVisibility(View.VISIBLE);
                wateredView.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.GONE);
                removeButton.setVisibility(View.VISIBLE);
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plant_db.child("Planted").setValue("False");
                Toast.makeText(PlantActivity.this, "Plant removed", Toast.LENGTH_SHORT).show();

                dateView.setVisibility(View.INVISIBLE);
                wateredView.setVisibility(View.INVISIBLE);
                removeButton.setVisibility(View.GONE);
                addButton.setVisibility(View.VISIBLE);
            }
        });

        sensorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    //check that the plant is added before allowing to set sensor
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String isPlanted = dataSnapshot.child(plant_name).child("Planted").getValue(String.class);
                        if (isPlanted.equals("True")) {
                            // set all other sensors to false
                            for (DataSnapshot parent : dataSnapshot.getChildren()) {
                                parent.child("Sensor").getRef().setValue("False");
                                parent.child("Water").getRef().setValue("N/A");
                            }
                            plant_db.child("Sensor").setValue("True");
                            plant_db.child("Water").setValue("False");

                            Intent i = new Intent(PlantActivity.this, MainActivity.class);
                            startActivity(i);
                        }
                        else {
                            Toast.makeText(PlantActivity.this, "You need to add a plant first", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });



    }
}
