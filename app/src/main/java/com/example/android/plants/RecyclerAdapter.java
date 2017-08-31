package com.example.android.plants;

import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private String[] mDataset;

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
    public RecyclerAdapter(String[] myDataset) {
        mDataset = myDataset;
        for (int i=0; i < myDataset.length; i++) {
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position % 2 == 0) {
            holder.branch.setImageResource(R.drawable.right_leave_new);
            holder.leftButton.setVisibility(View.INVISIBLE);
        }
        else {
            holder.branch.setImageResource(R.drawable.left_leave_new);
            holder.rightButton.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}