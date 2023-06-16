package com.codescafe.kissanformer.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.codescafe.kissanformer.R;
import com.codescafe.kissanformer.adapter.AdapterCompleteTeamChat;
import com.codescafe.kissanformer.model.IssueModel;
import com.codescafe.kissanformer.model.ModelTeamChat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CompleteChatView extends AppCompatActivity {

    ImageView back;
    AdapterCompleteTeamChat adapterCompleteTeamChat;
    ArrayList<ModelTeamChat> list = new ArrayList<>();
    RecyclerView chatlist;
    String id;
    IssueModel issueModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_chat_view);

        issueModel = (IssueModel) getIntent().getSerializableExtra("model");

        chatlist = findViewById(R.id.chatList);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FirebaseDatabase.getInstance().getReference("Support Chat")
                .child(issueModel.getUserId()).child("Issues").child(issueModel.getKey()).child("Messages")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //Toast.makeText(CompleteChatView.this, ""+snapshot.getRef(), Toast.LENGTH_LONG).show();
                        if (snapshot.exists()){
                            list.clear();
                            for (DataSnapshot ds:snapshot.getChildren()){
                                ModelTeamChat modelTeamChat = ds.getValue(ModelTeamChat.class);
                                list.add(modelTeamChat);
                            }
                            AdapterCompleteTeamChat adapterCompleteTeamChat = new AdapterCompleteTeamChat(CompleteChatView.this,list);
                            chatlist.setAdapter(adapterCompleteTeamChat);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}