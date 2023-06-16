package com.codescafe.kissanformer.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codescafe.kissanformer.R;
import com.codescafe.kissanformer.auth.DataEnterManager;
import com.codescafe.kissanformer.model.DataEntryModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {

    EditText oldPassword,newPassword;
    DataEnterManager dataEnterManager;
    DataEntryModel dataEntryModel;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        activity = this;
        //Back
        findViewById(R.id.imageView).setOnClickListener(v -> onBackPressed());
        dataEnterManager = new DataEnterManager(activity);
        dataEntryModel = dataEnterManager.getUserDetails("userDetails");

        //EditText
        oldPassword = findViewById(R.id.oldpassword);
        newPassword = findViewById(R.id.newPassword);
        newPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (newPassword.getRight() - newPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if(newPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                            newPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_24, 0);
                            newPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        }
                        else{
                            newPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_off_24, 0);
                            newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }
                    }
                }
                return false;
            }
        });
        oldPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (oldPassword.getRight() - oldPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if(oldPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                            oldPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_24, 0);
                            oldPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        }
                        else{
                            oldPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_off_24, 0);
                            oldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }
                    }
                }
                return false;
            }
        });

        //Button
        findViewById(R.id.send).setOnClickListener(v -> {

            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            String oldPass = oldPassword.getText().toString().trim();
            String newPass = newPassword.getText().toString().trim();

            if (oldPassword.getText().toString().trim().equals("")){
                Snackbar.make(v, "Enter Old Password", Snackbar.LENGTH_LONG).show();
                findViewById(R.id.progressBar).setVisibility(View.GONE);
            }else if (newPassword.getText().toString().trim().equals("")){
                Snackbar.make(v, "Enter New Password", Snackbar.LENGTH_LONG).show();
                findViewById(R.id.progressBar).setVisibility(View.GONE);
            }else if (oldPassword.getText().toString().trim().equals(dataEntryModel.getPassword())){
                String url ="https://kissancare.reedspak.org/updatePsw.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("status",response.toString() );
                        findViewById(R.id.progressBar).setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("status",error.toString() );
                        findViewById(R.id.progressBar).setVisibility(View.GONE);
                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<>();
                        map.put("User_ID","");
                        map.put("User_Psw","");
                        return map;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(activity);
                requestQueue.add(stringRequest);
            }else{
                Toast.makeText(activity, "Current Password is Not Matched", Toast.LENGTH_SHORT).show();
            }


        });

    }
}