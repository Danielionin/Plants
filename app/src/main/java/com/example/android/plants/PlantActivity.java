package com.example.android.plants;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant);

        //get plant name
        Bundle bundle = getIntent().getExtras();
        final String plant_name = bundle.getString("plant_name");

        // assign views
        titleView = (TextView) findViewById(R.id.title);
        descriptionView = (TextView) findViewById(R.id.description);
        imageView = (ImageView) findViewById(R.id.image);
        how_to_plantView = (TextView) findViewById(R.id.how_to_plant);
        how_to_growView = (TextView) findViewById(R.id.how_to_grow);

        //get database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference plant_db = mDatabase.child(plant_name);

        //Read from the database
        plant_db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String description = dataSnapshot.child("Description").getValue(String.class);
                String plant_img = dataSnapshot.child("plant_img").getValue(String.class);
                String how_to_plant = dataSnapshot.child("How To Plant").getValue(String.class);
                String how_to_grow = dataSnapshot.child("How To Grow").getValue(String.class);

                // assign text values to relevant views
                titleView.setText(plant_name);
                descriptionView.setText(description);
                how_to_plantView.setText(how_to_plant);
                how_to_growView.setText(how_to_grow);
                Picasso.with(PlantActivity.this).load(plant_img).into(imageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w("SearchActivity", "Failed to read value.", databaseError.toException());
            }
        });
    }
}
