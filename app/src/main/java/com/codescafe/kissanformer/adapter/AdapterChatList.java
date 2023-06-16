package com.codescafe.kissanformer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codescafe.kissanformer.R;
import com.codescafe.kissanformer.model.FarmerUserModel;
import com.codescafe.kissanformer.model.IssueModel;
import com.codescafe.kissanformer.model.ModelTeamChat;
import com.codescafe.kissanformer.supportteam.ChatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("ALL")
public class AdapterChatList extends RecyclerView.Adapter<AdapterChatList.MyHolder> {

    final Context context;
    final List<FarmerUserModel> modelChatLists;

    public AdapterChatList(Context context, List<FarmerUserModel> modelChatLists) {
        this.context = context;
        this.modelChatLists = modelChatLists;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_user_list, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        FarmerUserModel farmerUserModel = modelChatLists.get(position);
        //UserInfo
        //holder.name.setText(Objects.requireNonNull(""+farmerUserModel.getId()));
        //UserInfo
        FirebaseDatabase.getInstance().getReference().child("Users").child(modelChatLists.get(position).getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    FarmerUserModel userModel = snapshot.getValue(FarmerUserModel.class);
                    //Name
                    holder.name.setText(Objects.requireNonNull(snapshot.child("name").getValue()).toString());

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Support Chat").child("103");
                    reference.child(modelChatLists.get(position).getId()).child("Issues").addValueEventListener(new ValueEventListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //holder.message.setText("No Message");
                            //Toast.makeText(context, "hi", Toast.LENGTH_SHORT).show();
                            if (snapshot.exists()){
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    //Toast.makeText(context, "hi", Toast.LENGTH_SHORT).show();
                                    IssueModel issueModel = ds.getValue(IssueModel.class);
                                    if (issueModel.getStatus().equals("Pending")){
                                        reference.child(modelChatLists.get(position).getId()).child("Issues").child(issueModel.getKey())
                                                .child("Messages")
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for (DataSnapshot dss:snapshot.getChildren()){
                                                            //Toast.makeText(context, "hi"+issueModel.getKey(), Toast.LENGTH_SHORT).show();
                                                            ModelTeamChat chat = ds.getValue(ModelTeamChat.class);
                                                            //Log.e("i'm here " ,"true");
                                                            if (chat.getReceiver().equals("103") && chat.getSender().equals(""+modelChatLists.get(position).getId()) || chat.getReceiver().equals(""+modelChatLists.get(position).getId()) && chat.getSender().equals("103")) {
                                                               // Toast.makeText(context, "hi", Toast.LENGTH_SHORT).show();
                                                                //Log.e("i'm here " ,"true");
                                                                switch (chat.getType()) {
                                                                    case "audio":
                                                                        holder.message.setText("Sent a audio");
                                                                        break;
                                                                    case "text":
                                                                        holder.message.setText(chat.getType());
                                                                        break;
                                                                    default:
                                                                        holder.message.setText(chat.getMsg());
                                                                        break;
                                                                }
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

                                    }

                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.time.setVisibility(View.GONE);
        //Click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("model", modelChatLists.get(position).getId());
            context.startActivity(intent);
        });


    }


    @Override
    public int getItemCount() {
        return modelChatLists.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {

//        final CircleImageView dp;
//        final ImageView verified;
        final View online;
        final TextView name;
        final TextView message;
        final TextView count, time;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //dp = itemView.findViewById(R.id.dp);
            //verified = itemView.findViewById(R.id.verified);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            message = itemView.findViewById(R.id.last_msg);
            count = itemView.findViewById(R.id.pending_msg_count);
            online = itemView.findViewById(R.id.offline_view);
        }

    }
}
