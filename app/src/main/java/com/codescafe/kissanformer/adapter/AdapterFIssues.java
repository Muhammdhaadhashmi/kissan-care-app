package com.codescafe.kissanformer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codescafe.kissanformer.Activities.CompleteChatView;
import com.codescafe.kissanformer.R;
import com.codescafe.kissanformer.model.IssueModel;

import java.util.ArrayList;

public class AdapterFIssues extends RecyclerView.Adapter<AdapterFIssues.MyHolder>{

    final Context context;
    final ArrayList<IssueModel> userList;

    public AdapterFIssues(Context context, ArrayList<IssueModel> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.itemissue,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {

        IssueModel issueModel = userList.get(position);
        holder.name.setText(issueModel.getName());
        holder.startDate.setText(issueModel.getDate());
        holder.experience.setText(issueModel.getCity());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CompleteChatView.class);
                intent.putExtra("model",issueModel);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder{


        TextView name,startDate,experience;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            startDate = itemView.findViewById(R.id.startDate);
            experience = itemView.findViewById(R.id.experience);
        }

    }
}
