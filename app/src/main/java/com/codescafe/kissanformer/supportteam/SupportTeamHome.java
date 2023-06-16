package com.codescafe.kissanformer.supportteam;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codescafe.kissanformer.Activities.AllFarmer;
import com.codescafe.kissanformer.Activities.AudioFiles;
import com.codescafe.kissanformer.Activities.PdfFiles;
import com.codescafe.kissanformer.Activities.SupportChat;
import com.codescafe.kissanformer.Activities.VideoFiles;
import com.codescafe.kissanformer.R;
import com.codescafe.kissanformer.auth.FarmerManager;
import com.codescafe.kissanformer.auth.Login;
import com.codescafe.kissanformer.auth.SupportTeamManager;
import com.codescafe.kissanformer.model.FarmerUserModel;
import com.codescafe.kissanformer.model.SupportTeamModel;
import com.google.android.material.navigation.NavigationView;

public class SupportTeamHome extends AppCompatActivity {

    CardView addFarmer, farmerRecords, pdfFiles, videoFiles, audioFiles, supportChat,casualChat;
    Activity activity;
    SupportTeamManager farmerManager;
    SupportTeamModel supportTeamModel;
    TextView userid;
    ImageView loguot;
    ImageView menu;
    DrawerLayout drawer;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_team_home);
        activity = this;
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        farmerManager = new SupportTeamManager(activity);
        supportTeamModel = farmerManager.getUserDetails("userDetail");


        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer);
        menu = findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.usernameTxt);
        navUsername.setText(""+supportTeamModel.getAdmin_name());

//        pdfFiles = findViewById(R.id.pdfFiles);
//        videoFiles = findViewById(R.id.videoFiles);
//        audioFiles = findViewById(R.id.audioFiles);
        supportChat = findViewById(R.id.supportChat);
        casualChat = findViewById(R.id.casualChat);

        userid = findViewById(R.id.userId);
        userid.setText("ID : " + supportTeamModel.getAdmin_id());
        loguot = findViewById(R.id.loguot);

       // pdfFiles.setOnClickListener(view -> startActivity(new Intent(activity, PdfFiles.class)));
       // videoFiles.setOnClickListener(view -> startActivity(new Intent(activity, VideoFiles.class)));
       // audioFiles.setOnClickListener(view -> startActivity(new Intent(activity, AudioFiles.class)));
        casualChat.setOnClickListener(view -> startActivity(new Intent(activity, AllPersonalChat.class)));
        supportChat.setOnClickListener(view ->
        {
            startActivity(new Intent(activity, AllChat.class));
        });
        loguot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Are You Sure You Want To Logout");
                builder.setPositiveButton(Html.fromHtml("Logout"), (dialogInterface, i) -> {
                    farmerManager.logoutUser();
                    SupportTeamModel user = new SupportTeamModel();
                    user.setAdmin_id("0");
                    farmerManager.setUserDetails("", supportTeamModel);
                    startActivity(new Intent(activity, Login.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                });
                builder.setNegativeButton(Html.fromHtml("Cancel"), (dialogInterface, i) -> dialogInterface.dismiss());
                builder.show();
            }
        });
        getpermission();
    }
    public void getpermission() {
        String exread = Manifest.permission.READ_EXTERNAL_STORAGE;
        String exwrite = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String call_phone = Manifest.permission.CALL_PHONE;
        String mic = Manifest.permission.RECORD_AUDIO;
        if (ContextCompat.checkSelfPermission(this, exread) !=
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                exwrite) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                mic) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        call_phone) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{exread, exwrite, call_phone}, 200);
            }
        }
    }
}