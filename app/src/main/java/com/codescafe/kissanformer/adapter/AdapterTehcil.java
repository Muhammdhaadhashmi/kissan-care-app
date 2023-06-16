package com.codescafe.kissanformer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codescafe.kissanformer.Activities.AddFarmer;
import com.codescafe.kissanformer.Activities.FamerProfile;
import com.codescafe.kissanformer.R;
import com.codescafe.kissanformer.model.CityModel;
import com.codescafe.kissanformer.model.DistrictModel;

import java.util.ArrayList;

public class AdapterTehcil extends RecyclerView.Adapter<AdapterTehcil.MyHolder>{

    final Context context;
    final ArrayList<CityModel> userList;

    public AdapterTehcil(Context context, ArrayList<CityModel> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.item_mm,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.name.setText(userList.get(position).getTehsil_name());
    }
    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder{


        TextView name;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
        }

    }
}
