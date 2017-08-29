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
    private final ArrayList<String> gridViewString;
    private final ArrayList<String> gridViewImageId;

    public CustomGridViewActivity(Context context, ArrayList<String> gridViewString, ArrayList<String> gridViewImageId) {
        mContext = context;
        this.gridViewImageId = gridViewImageId;
        this.gridViewString = gridViewString;
    }

    @Override
    public int getCount() {
        return gridViewString.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View gridViewAndroid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            TextView textViewAndroid;
            ImageView imageViewAndroid;

            //TODO maybe remove first line
            gridViewAndroid = new View(mContext);
            gridViewAndroid = inflater.inflate(R.layout.gridview_layout, null);


            textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.android_gridview_text);
            imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.android_gridview_image);


            // set text from arraylist to gridview text
            textViewAndroid.setText(gridViewString.get(i));
            //using Picasso library to insert image url in imageView in gridview
            Picasso.with(mContext).load(gridViewImageId.get(i)).into(imageViewAndroid);
        } else {
            gridViewAndroid = (View) convertView;
        }

        return gridViewAndroid;
    }
}