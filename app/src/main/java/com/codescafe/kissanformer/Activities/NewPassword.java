package com.codescafe.kissanformer.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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
import com.codescafe.kissanformer.auth.Login;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewPassword extends AppCompatActivity {

    EditText oldPassword,newPassword;
    Activity activity;
    String val;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        activity = this;
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        //Back
        findViewById(R.id.imageView).setOnClickListener(v -> onBackPressed());
        val = getIntent().getStringExtra("email");

        //EditText
        oldPassword = findViewById(R.id.oldpassword);
        newPassword = findViewById(R.id.newPassword);
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
        //Button
        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String old_pw = oldPassword.getText().toString().trim();
                    String new_pw = newPassword.getText().toString().trim();

                    if (old_pw.isEmpty() || old_pw.contains(" ")) {
                        oldPassword.setError("Enter New Password");
                        oldPassword.requestFocus();
                        return;
                    }
                    if (old_pw.length() <= 5) {
                        oldPassword.setError("Enter min 6 characters");
                        oldPassword.requestFocus();
                        return;
                    }
                    if (new_pw.isEmpty()) {
                        newPassword.setError("Enter New Password");
                        newPassword.requestFocus();
                        return;
                    }
                    if (new_pw.length() <= 5) {
                        newPassword.setError("Enter min 6 characters");
                        newPassword.requestFocus();
                        return;
                    }
                    if (!old_pw.equals(new_pw)) {
                        oldPassword.setError("Password Not Match");
                        newPassword.setError("Password Not Match");
                        oldPassword.requestFocus();
                        newPassword.requestFocus();
                        return;
                    }
                    String url ="https://kissancare.reedspak.org/passF.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("status",response.toString() );
                            JSONObject jsonObject = null;
                            try {
                                // jsonObject = new JSONObject(response);
                                //String ss = jsonObject.toString();
                                if (response.trim().equals("1")){
                                    Toast.makeText(activity, "Password Updated Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(activity, Login.class));
                                    finishAffinity();
                                }else{
                                    Toast.makeText(activity, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
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
                            map.put("email",""+val);
                            map.put("password",""+newPassword.getText().toString().trim());
                            return map;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(activity);
                    requestQueue.add(stringRequest);

                }catch (Exception e){
                    e.printStackTrace();
                }
                }
            });
    }
}