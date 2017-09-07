package com.example.android.plants;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private DatabaseReference firebaseDB;
    private ArrayList<String> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView branch;
        Button leftButton;
        Button rightButton;
        // each data item is just a string in this case
        public ViewHolder(View v) {
            super(v);

            branch = (ImageView) v.findViewById(R.id.tree);
            leftButton = (Button) v.findViewById(R.id.button_left);
            rightButton = (Button) v.findViewById(R.id.button_right);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerAdapter(ArrayList<String> myDataset) {
        mDataset = myDataset;
        for (int i=0; i < myDataset.size(); i++) {
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view by inflating it
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final int index = position;
        final ViewHolder viewHolder = holder;

        firebaseDB = FirebaseDatabase.getInstance().getReference("Plants");
        firebaseDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String plant_name = mDataset.get(index);
                final String sensor_mode = dataSnapshot.child(plant_name).child("Sensor").getValue(String.class);

                if (index % 2 == 0) {
                    viewHolder.branch.setImageResource(R.drawable.right_leave_new);
                    viewHolder.leftButton.setVisibility(View.INVISIBLE);
                    viewHolder.rightButton.setText(plant_name);
                    viewHolder.rightButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(v.getContext(), PlantActivity.class);
                            i.putExtra("plant_name", plant_name);
                            i.putExtra("is_planted", "True");
                            i.putExtra("sensor_mode", sensor_mode);
                            v.getContext().startActivity(i);
                        }
                    });
                }
                else {
                    viewHolder.branch.setImageResource(R.drawable.left_leave_new);
                    viewHolder.rightButton.setVisibility(View.INVISIBLE);
                    viewHolder.leftButton.setText(plant_name);
                    viewHolder.leftButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(v.getContext(), PlantActivity.class);
                            i.putExtra("plant_name", plant_name);
                            i.putExtra("is_planted", "True");
                            i.putExtra("sensor_mode", sensor_mode);
                            v.getContext().startActivity(i);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}