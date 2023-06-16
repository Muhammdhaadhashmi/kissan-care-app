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

import com.codescafe.kissanformer.R;
import com.codescafe.kissanformer.model.FarmerUserModel;
import com.codescafe.kissanformer.model.ModelTeamChat;
import com.codescafe.kissanformer.supportteam.FarmerPersonalChat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("ALL")
public class AdapterPersonalChatList extends RecyclerView.Adapter<AdapterPersonalChatList.MyHolder> {

    final Context context;
    final List<FarmerUserModel> modelChatLists;

    public AdapterPersonalChatList(Context context, List<FarmerUserModel> modelChatLists) {
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

//                    //Time
//                    if (Objects.requireNonNull(snapshot.child("status").getValue()).toString().equals("online"))
//                        holder.online.setVisibility(View.VISIBLE);

                    //Name
                    holder.name.setText(Objects.requireNonNull(snapshot.child("name").getValue()).toString());

                    //DP
//                    if (!Objects.requireNonNull(snapshot.child("picture").getValue()).toString().isEmpty()) {
//                        Picasso.get().load(Objects.requireNonNull(snapshot.child("picture").getValue()).toString()).into(holder.dp);
//                    }

                   /* //Verify
                    if (Objects.requireNonNull(snapshot.child("verified").getValue()).toString().equals("yes")) {
                        holder.verified.setVisibility(View.VISIBLE);
                    }*/

                    //Typing
                    /*if (Objects.requireNonNull(snapshot.child("typingTo").getValue()).toString().equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
                        holder.message.setText("Typing...");
                    } else {*/
                    //LastMessage
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Personal Chat").child("103");
                    reference.child(modelChatLists.get(position).getId()).addValueEventListener(new ValueEventListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            holder.message.setText("No Message");
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                ModelTeamChat chat = ds.getValue(ModelTeamChat.class);
                                if (chat == null) {
                                    continue;
                                }
                                String sender = chat.getSender();
                                String receiver = chat.getReceiver();
                                if (sender == null || receiver == null) {
                                    continue;
                                }
                                if (chat.getReceiver().equals("103") && chat.getSender().equals(modelChatLists.get(position).getId()) || chat.getReceiver().equals(modelChatLists.get(position).getId()) && chat.getSender().equals("103")) {
                                    switch (chat.getType()) {
                                        case "audio":
                                            holder.message.setText("Sent a audio");
                                            break;
                                        case "image":
                                            holder.message.setText("Sent a image");
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Click
        holder.time.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FarmerPersonalChat.class);
            intent.putExtra("model", modelChatLists.get(position).getId());
            context.startActivity(intent);
            holder.count.setVisibility(View.GONE);
            holder.count.setText("");
        });

        FirebaseDatabase.getInstance().getReference("Personal Chat").child("103")
                .child(modelChatLists.get(position).getId()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                for (DataSnapshot ds : snapshot.getChildren()){

                    if (Objects.requireNonNull(ds.child("sender").getValue()).toString().equals(modelChatLists.get(position).getId()) && Objects.requireNonNull(ds.child("receiver").getValue()).toString().equals("103")){
                        ModelTeamChat post = ds.getValue(ModelTeamChat.class);
                        assert post != null;
                        if (Boolean.parseBoolean(ds.child("isSeen").getValue().toString()) == false){
                            i++;
                        }
                    }
                }
                if (i != 0){
                    holder.count.setVisibility(View.VISIBLE);
                    holder.count.setText(""+i);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
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
