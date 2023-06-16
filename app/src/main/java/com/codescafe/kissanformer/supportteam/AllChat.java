package com.codescafe.kissanformer.supportteam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codescafe.kissanformer.Activities.AudioFiles;
import com.codescafe.kissanformer.R;
import com.codescafe.kissanformer.adapter.AdapterAudio;
import com.codescafe.kissanformer.adapter.AdapterChatList;
import com.codescafe.kissanformer.model.FarmerUserModel;
import com.codescafe.kissanformer.model.ModelChatList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AllChat extends AppCompatActivity {

    CardView toPersons;
    RecyclerView rvChatUser;
    List<ModelChatList> chatList;
    List<FarmerUserModel> userList= new ArrayList<>();
    List<FarmerUserModel> sortUserList= new ArrayList<>();
    AdapterChatList adapterChatList;
    ArrayList<String> list_id = new ArrayList<>();
    DatabaseReference databaseReference, liftRef;
    ProgressBar progressBar;
    TextView noData;
    EditText password;
    int textlength = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chat);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        databaseReference = FirebaseDatabase.getInstance().getReference("Support Chat").child("103");
        liftRef = FirebaseDatabase.getInstance().getReference("AdminChatList");

        password = findViewById(R.id.password);

        noData = findViewById(R.id.noData);
        progressBar = findViewById(R.id.progressBar);
        rvChatUser = findViewById(R.id.rvChats);
        chatList = new ArrayList<>();

        password.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textlength = password.getText().length();
                sortUserList.clear();
                for (int i = 0; i < userList.size(); i++) {
                    if (textlength <= userList.get(i).getName().length()) {
                        Log.d("ertyyy", userList.get(i).getName().toLowerCase().trim());
                        if (userList.get(i).getName().toLowerCase().trim().contains(
                                password.getText().toString().toLowerCase().trim())) {
                            sortUserList.add(userList.get(i));
                        }
                    }
                }
                adapterChatList = new AdapterChatList(AllChat.this, sortUserList);
                rvChatUser.setAdapter(adapterChatList);

            }
        });

        liftRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                list_id.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    list_id.add(ds.getKey());
/*                    ModelChatlist chatlist = ds.getValue(ModelChatlist.class);
                    chatlistList.add(chatlist);*/
                }
                loadChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadChats() {
        FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    FarmerUserModel user = ds.getValue(FarmerUserModel.class);
                    for (int i = 0; list_id.size() > i; i++) {
                        if (Objects.requireNonNull(user).getId() != null && user.getId().equals(list_id.get(i))) {
                            userList.add(user);
                            break;
                        }
                    }
                    if (userList.size()>0){
                        progressBar.setVisibility(View.GONE);
                        rvChatUser.setVisibility(View.VISIBLE);
                        adapterChatList = new AdapterChatList(AllChat.this, userList);
                        rvChatUser.setAdapter(adapterChatList);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}