package com.codescafe.kissanformer.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PasswordForget extends AppCompatActivity {

    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_forget);
        activity = this;
        //Firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        //Back
        findViewById(R.id.imageView).setOnClickListener(v -> onBackPressed());

        //EditText
        EditText email = findViewById(R.id.email);

        //Button
        findViewById(R.id.send).setOnClickListener(v -> {

            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            String mEmail = email.getText().toString().trim();

            if (mEmail.isEmpty()) {
                Snackbar.make(v, "Enter your email", Snackbar.LENGTH_LONG).show();
                findViewById(R.id.progressBar).setVisibility(View.GONE);
            } else {
                String url = "https://kissancare.reedspak.org/sendMail2.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        Log.e("status", response);

                        try {
                            //Toast.makeText(activity, ""+response, Toast.LENGTH_SHORT).show();
//                            jsonObject = new JSONObject(response);
//                            String ss = jsonObject.toString();
//                            Log.e("val",ss);

                             if (response.trim().equals("1")) {
                                 findViewById(R.id.progressBar).setVisibility(View.GONE);
                                 Intent intent = new Intent(activity, OTP.class);
                                 //Log.e("status", response);
                                 Log.e("status:1", "here");
                                 intent.putExtra("email", email.getText().toString().trim());
                                 startActivity(intent);
                             }else{
                            //Toast.makeText(activity, "Email doesn't exist", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("status", error.toString());
                        findViewById(R.id.progressBar).setVisibility(View.GONE);
                    }
                }) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("email", mEmail);
                        return map;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(PasswordForget.this);
                requestQueue.add(stringRequest);
            }


        });

    }
}