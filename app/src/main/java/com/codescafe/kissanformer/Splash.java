package com.codescafe.kissanformer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codescafe.kissanformer.auth.DataEnterManager;
import com.codescafe.kissanformer.auth.FarmerManager;
import com.codescafe.kissanformer.auth.Login;
import com.codescafe.kissanformer.auth.SupportTeamManager;
import com.codescafe.kissanformer.farmer.FarmerHome;
import com.codescafe.kissanformer.model.FarmerModel;
import com.codescafe.kissanformer.model.FarmerUserModel;
import com.codescafe.kissanformer.supportteam.SupportTeamHome;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Splash extends AppCompatActivity {

    SupportTeamManager supportTeamManager;
    DataEnterManager dataEnterManager;
    FarmerManager farmerManager;
    FirebaseAuth auth;
    Boolean newUser;
    String val;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        setContentView(R.layout.activity_splash);
        activity = this;

        supportTeamManager = new SupportTeamManager(activity);
        dataEnterManager = new DataEnterManager(activity);
        farmerManager = new FarmerManager(activity);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (supportTeamManager.getBooleanData("support")) {
                    startActivity(new Intent(activity, SupportTeamHome.class));

                }else if (dataEnterManager.getBooleanData("data")) {
                    startActivity(new Intent(activity, MainActivity.class));
                }else {
                    if (farmerManager.getBooleanData("farmer")){
                        startActivity(new Intent(activity, FarmerHome.class));
                    }else{
                        createNewFarmer();
                    }
                }
                finish();
            }
        },3000);
    }

    private void createNewFarmer() {
        String key = ""+System.currentTimeMillis();
        FarmerUserModel farmerUserModel = new FarmerUserModel("name","id","city");
        farmerUserModel.setId(""+key);
        farmerManager.setUserDetails("userDetail", farmerUserModel);
        farmerManager.setBooleanData("farmer", true);
        FirebaseDatabase.getInstance().getReference("Users")
                .child(key)
                .setValue(farmerUserModel);
        startActivity(new Intent(activity,FarmerHome.class));

    }
}