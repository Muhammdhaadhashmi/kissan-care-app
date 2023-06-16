package com.codescafe.kissanformer.farmer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codescafe.kissanformer.Activities.AudioFiles;
import com.codescafe.kissanformer.Activities.PdfFiles;
import com.codescafe.kissanformer.Activities.SupportChat;
import com.codescafe.kissanformer.Activities.VideoFiles;
import com.codescafe.kissanformer.BuildConfig;
import com.codescafe.kissanformer.R;
import com.codescafe.kissanformer.auth.FarmerManager;
import com.codescafe.kissanformer.auth.Login;
import com.codescafe.kissanformer.model.FarmerUserModel;
import com.codescafe.kissanformer.model.IssueModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class FarmerHome extends AppCompatActivity {

    ImageView loguot;
    TextView userid;
    Activity activity;
    FarmerManager farmerManager;
    CardView addFarmer,farmerRecords,pdfFiles,videoFiles,audioFiles,supportChat,PersonalChat;
    FarmerUserModel farmerUserModel;
    NavigationView navigationView;
    DrawerLayout drawer;
    ImageView menu;
    boolean x = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_home);
        activity = this;
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        farmerManager = new FarmerManager(activity);
        farmerUserModel = farmerManager.getUserDetails("userDetail");

        getSharedPreferences("name",MODE_PRIVATE).edit().putBoolean("name",false).apply();
        x = getSharedPreferences("name",MODE_PRIVATE).getBoolean("name",false);
        if (!x){
            if (farmerUserModel.getName().equals("name")){
                openDialog();
            }

        }


        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer);
        menu = findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        pdfFiles = findViewById(R.id.pdfFiles);
        videoFiles = findViewById(R.id.videoFiles);
        audioFiles = findViewById(R.id.audioFiles);
        supportChat = findViewById(R.id.supportChat);
        PersonalChat = findViewById(R.id.PersonalChat);

        userid = findViewById(R.id.userId);
       // userid.setText("ID : "+farmerUserModel.getId());
        loguot = findViewById(R.id.loguot);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.usernameTxt);
        navUsername.setText(""+farmerUserModel.getName());
        pdfFiles.setOnClickListener(view -> startActivity(new Intent(activity, PdfFiles.class)));
        videoFiles.setOnClickListener(view -> startActivity(new Intent(activity, VideoFiles.class)));
        audioFiles.setOnClickListener(view -> startActivity(new Intent(activity, AudioFiles.class)));
        PersonalChat.setOnClickListener(view -> startActivity(new Intent(activity, PersonalChat.class)));
        supportChat.setOnClickListener(view ->
        {

            final String[] c = {"false"};
            FirebaseDatabase.getInstance().getReference("Support Chat")
                    .child(farmerUserModel.getId()).child("Issues").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                for (DataSnapshot ds:snapshot.getChildren()){
                                    IssueModel issueModel = ds.getValue(IssueModel.class);
                                    assert issueModel != null;
                                    if (issueModel.getStatus().equals("Pending")){
                                        c[0] = "true";
                                        Intent intent = new Intent(activity,SupportChat.class);
                                        intent.putExtra("issue",issueModel.getKey());
                                        startActivity(intent);
                                        break;
                                    }
                                }
                            }
                            if (c[0].equals("false")){
                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                View v = getLayoutInflater().inflate(R.layout.goaldialog,null);
                                builder.setView(v);
                                Dialog dialog = builder.create();
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                EditText goalTxt,name,city;
                                TextView cancel,update;
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Support Chat")
                                        .child(farmerUserModel.getId())
                                        .child("Issues");

                                goalTxt = v.findViewById(R.id.goalTxt);
                                name = v.findViewById(R.id.name);
                                city = v.findViewById(R.id.city);
                                cancel = v.findViewById(R.id.cancel);
                                update = v.findViewById(R.id.update);
                                update.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        String key = ref.push().getKey();
                                        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

                                        if (goalTxt.getText().toString().equals("")){
                                            Toast.makeText(FarmerHome.this, "Enter Subject", Toast.LENGTH_SHORT).show();
                                        }else if (name.getText().toString().equals("")){
                                            Toast.makeText(FarmerHome.this, "Enter Name", Toast.LENGTH_SHORT).show();
                                        }else if (city.getText().toString().equals("")){
                                            Toast.makeText(FarmerHome.this, "Enter City", Toast.LENGTH_SHORT).show();
                                        }else{
                                            HashMap<String,Object> hashMap = new HashMap<>();
                                            hashMap.put("subject",goalTxt.getText().toString().trim());
                                            hashMap.put("name",name.getText().toString().trim());
                                            hashMap.put("city",city.getText().toString().trim());
                                            hashMap.put("date",date);
                                            hashMap.put("userId",farmerUserModel.getId());
                                            hashMap.put("status","Pending");
                                            hashMap.put("key",key);

                                            assert key != null;
                                            FirebaseDatabase.getInstance().getReference("Support Chat")
                                                            .child("103")
                                                                    .child(farmerUserModel.getId())
                                                                            .child("Issues").child(key)
                                                            .setValue(hashMap);
                                            ref.child(key).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Intent intent = new Intent(activity,SupportChat.class);
                                                        intent.putExtra("issue",""+key);
                                                        startActivity(intent);
                                                        dialog.dismiss();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(activity,SupportChat.class);
                                        intent.putExtra("issue","no");
                                        startActivity(intent);
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
//            Intent intent = new Intent(activity,SupportChat.class);
//            intent.putExtra("id",farmerUserModel.getId());
//            intent.putExtra("type","farmer");
//            startActivity(intent);
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment frag = null;
                item.setChecked(true);
                int itemId = item.getItemId();

                if (itemId == R.id.share) {
                    drawer.close();
                    try {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, ""+getString(R.string.app_name));
                        String shareMessage = "\nLet me recommend you this application\n\n";
                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        startActivity(Intent.createChooser(shareIntent, "choose one"));
                    } catch (Exception e) {

                    }

                } else if (itemId == R.id.login) {
                    drawer.close();
                    startActivity(new Intent(activity, Login.class));
                } else if (itemId == R.id.rate) {
                    drawer.close();
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    try {
                        startActivity(myAppLinkToMarket);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(getApplicationContext(), " unable to find app", Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }

        });
        loguot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Are You Sure You Want To Logout");
                builder.setPositiveButton(Html.fromHtml("Logout"), (dialogInterface, i) -> {
                    farmerManager.logoutUser();
                    FarmerUserModel user = new FarmerUserModel();
                    user.setId("0");
                    farmerManager.setUserDetails("", user);
                    startActivity(new Intent(activity, Login.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                });
                builder.setNegativeButton(Html.fromHtml("Cancel"), (dialogInterface, i) -> dialogInterface.dismiss());
                builder.show();
            }
        });getpermission();



    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View v = getLayoutInflater().inflate(R.layout.namedialog,null);
        builder.setView(v);
        EditText name = v.findViewById(R.id.name);
        TextView setName = v.findViewById(R.id.update);
        TextView cancel = v.findViewById(R.id.cancel);

        Dialog dialog = builder.create();
        setName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("name",""+name.getText().toString().trim());
                farmerUserModel.setName(""+name.getText().toString().trim());
                farmerManager.setUserDetails("userDetail", farmerUserModel);
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(farmerUserModel.getId())
                        .updateChildren(hashMap);
                dialog.dismiss();
                //startActivity(new Intent(activity,FarmerHome.class));
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
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