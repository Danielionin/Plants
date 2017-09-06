package com.example.android.plants;

import android.content.Context;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomGridViewActivity extends BaseAdapter {

    private Context mContext;
//    private final ArrayList<String> gridViewString;
//    private final ArrayList<String> gridViewImageId;
    private final ArrayList<Plant> gridViewPlant;

    public CustomGridViewActivity(Context context, ArrayList<Plant> gridViewPlant) {
        mContext = context;
//        this.gridViewImageId = gridViewImageId;
//        this.gridViewString = gridViewString;
        this.gridViewPlant = gridViewPlant;
    }

    @Override
    public int getCount() {
        return gridViewPlant.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private void updateView(TextView textView, ImageView imgView, int i, View gridView) {

        textView = (TextView) gridView.findViewById(R.id.android_gridview_text);
        imgView = (ImageView) gridView.findViewById(R.id.android_gridview_image);

        // set text from arraylist to gridview text
        textView.setText(gridViewPlant.get(i).plant_name);
        // using Picasso library to insert image url in imageView in gridview
        Picasso.with(mContext).load(gridViewPlant.get(i).url).into(imgView);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View gridViewAndroid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TextView textViewAndroid = null;
        ImageView imageViewAndroid = null;

        if (convertView == null) {

            //TODO maybe remove first line
            //gridViewAndroid = new View(mContext);

            gridViewAndroid = inflater.inflate(R.layout.gridview_layout, null);
            updateView(textViewAndroid, imageViewAndroid, i, gridViewAndroid);
        } else {
            gridViewAndroid = (View) convertView;
            updateView(textViewAndroid, imageViewAndroid, i, gridViewAndroid);
        }

        return gridViewAndroid;
    }
}