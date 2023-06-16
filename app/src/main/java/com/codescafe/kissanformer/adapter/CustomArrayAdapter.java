package com.codescafe.kissanformer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codescafe.kissanformer.R;

import java.util.ArrayList;

public class CustomArrayAdapter extends ArrayAdapter<String> {

    private ArrayList<String> objects;
    private Context context;

    public CustomArrayAdapter(Context context, int resourceId,
                              ArrayList<String> objects) {
        super(context, resourceId, objects);
        this.objects = objects;
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=(LayoutInflater) context.getSystemService(  Context.LAYOUT_INFLATER_SERVICE );
        View row=inflater.inflate(R.layout.layout_route, parent, false);
        TextView label=(TextView)row.findViewById(R.id.name);
        label.setText(objects.get(position));

        if (position == 0) {//Special style for dropdown header
            label.setTextColor(context.getResources().getColor(R.color.black));
        }

        return row;
    }

}
