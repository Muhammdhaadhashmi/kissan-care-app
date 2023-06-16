package com.codescafe.kissanformer.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codescafe.kissanformer.R;

import java.util.ArrayList;
import java.util.List;

/********************************************
 *     Created by DailyCoding on 15-May-21.  *
 ********************************************/

public class SpinnerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> fruitList;

    public SpinnerAdapter(Context context, ArrayList<String> fruitList) {
        this.context = context;
        this.fruitList = fruitList;
    }

    @Override
    public int getCount() {
        return fruitList != null ? fruitList.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rootView = LayoutInflater.from(context)
                .inflate(R.layout.item_mmm, viewGroup, false);

        TextView txtName = rootView.findViewById(R.id.name);
        txtName.setText(fruitList.get(i));

//        rootView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, ""+fruitList.get(i), Toast.LENGTH_SHORT).show();
//            }
//        });

        return rootView;
    }
}