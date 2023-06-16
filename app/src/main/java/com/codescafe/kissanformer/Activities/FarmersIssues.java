package com.codescafe.kissanformer.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.codescafe.kissanformer.R;
import com.codescafe.kissanformer.adapter.AdapterIssues;
import com.codescafe.kissanformer.model.IssueModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FarmersIssues extends AppCompatActivity {
    ImageView back;
    RecyclerView rvfarmers;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmers_issues);


        id = getIntent().getStringExtra("id");
        back = findViewById(R.id.back);
        rvfarmers = findViewById(R.id.rvfarmers);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        getIssues();

    }

    AdapterIssues adapterIssues;
    ArrayList<IssueModel> list = new ArrayList<>();
    private void getIssues() {
        FirebaseDatabase.getInstance().getReference("Support Chat").child(id).child("Issues").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds:snapshot.getChildren()){
                        IssueModel issueModel = ds.getValue(IssueModel.class);
                        assert issueModel != null;
                        if (issueModel.getStatus().equals("Complete")){
                            list.add(issueModel);
                        }
                    }
                    adapterIssues = new AdapterIssues(FarmersIssues.this,list);
                    rvfarmers.setAdapter(adapterIssues);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}