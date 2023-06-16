package com.codescafe.kissanformer.auth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codescafe.kissanformer.Activities.PasswordForget;
import com.codescafe.kissanformer.MainActivity;
import com.codescafe.kissanformer.R;
import com.codescafe.kissanformer.farmer.FarmerHome;
import com.codescafe.kissanformer.model.DataEntryModel;
import com.codescafe.kissanformer.model.SupportTeamModel;
import com.codescafe.kissanformer.supportteam.SupportTeamHome;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    Button login;
    TextView register;
    Activity activity;
    TextView forgot;
    DataEnterManager dataEnterManager;
    SupportTeamManager supportTeamManager;
    String loginType = "";
    RadioButton dataEntryPerson,supportTeamPerson;
    EditText pass;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dataEnterManager = new DataEnterManager(Login.this);
        supportTeamManager = new SupportTeamManager(Login.this);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        activity = this;


        forgot = findViewById(R.id.forgot);
        supportTeamPerson = findViewById(R.id.supportTeamPerson);
        dataEntryPerson = findViewById(R.id.dataEntryPerson);

        pass = findViewById(R.id.pass);
        supportTeamPerson.setChecked(false);
        dataEntryPerson.setChecked(true);
        loginType = "data";
        supportTeamPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportTeamPerson.setChecked(true);
                dataEntryPerson.setChecked(false);
                loginType = "support";
                forgot.setVisibility(View.GONE);
            }
        });
        dataEntryPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportTeamPerson.setChecked(false);
                dataEntryPerson.setChecked(true);
                loginType = "data";
                forgot.setVisibility(View.VISIBLE);
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent intent = new Intent(Login.this, PasswordForget.class);
                startActivity(intent);
            }
        });
        pass.setOnTouchListener((View v, @SuppressLint("ClickableViewAccessibility") MotionEvent event) -> {
            @SuppressLint("ClickableViewAccessibility") final int DRAWABLE_RIGHT = 2;

            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getRawX() >= (pass.getRight() - pass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    //    Utils.setToast(activity,"Right");
                    if(pass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                        //  ((ImageView(view)).setImageResource(R.drawable.hide_password);
                        pass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_24, 0);
                        //      Utils.setToast(activity,"Show");
                        //Show Password
                        pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    else{
                        //    Utils.setToast(activity,"Hide");
                        pass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_off_24, 0);
                        //  ((ImageView)(view)).setImageResource(R.drawable.show_password);
                        //Hide Password
                        pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                    //   tv_SignUp_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    // tv_SignUp_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
            return false;
        });
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        //Back
        findViewById(R.id.imageView).setOnClickListener(v -> onBackPressed());
        //OnClick
      //  findViewById(R.id.signUP).setOnClickListener(v -> startActivity(new Intent(activity, Register.class)));
        //findViewById(R.id.forgot).setOnClickListener(v -> startActivity(new Intent(activity, ForgotPasswordActivity.class)));
        //EditText
        EditText email = findViewById(R.id.email);
        EditText pass = findViewById(R.id.pass);
        //Button
        findViewById(R.id.login).setOnClickListener(v -> {
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            String mEmail = email.getText().toString().trim();
            String mPassword = pass.getText().toString().trim();
            if (mEmail.isEmpty()) {
                Snackbar.make(v,
                        "Enter your email",
                        Snackbar.LENGTH_LONG).show();
                findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
            } else if (mPassword.isEmpty()) {
                Snackbar.make(v, "Enter your password", Snackbar.LENGTH_LONG).show();
                findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
            } else {
                if (loginType.equals("data")){
                    String url = "https://kissancare.reedspak.org/LoginKisanUser.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.e("eee",response.toString());
                                response = response.replace("[","").replace("]","");
                                JSONObject jsonObject=new JSONObject(response);
                                String user=jsonObject.getJSONObject("data").toString();
                                Gson gson = new Gson();
                                DataEntryModel superMan = gson.fromJson(user.toString(), DataEntryModel.class);
                                String value = gson.toJson(superMan);
                                // Toast.makeText(activity, ""+value.toString(), Toast.LENGTH_SHORT).show();
                                dataEnterManager.setUserDetails("userDetail", superMan);
                                dataEnterManager.setBooleanData("data", true);
                                Intent intent = new Intent(activity, MainActivity.class);
                                startActivity(intent);
                                finishAffinity();

                            } catch (JSONException e) {
                                Log.e("eee",e.toString());
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("eee1",error.toString());
                        }
                    }){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<String,String>();
                            map.put("email",email.getText().toString());
                            map.put("password",pass.getText().toString());
                            return map;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(activity);
                    requestQueue.add(stringRequest);
                }else if (loginType.equals("support")){
                    String url = "https://kissancare.reedspak.org/LoginChatSupport.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.e("eee",response.toString());
                                response = response.replace("[","").replace("]","");
                                JSONObject jsonObject=new JSONObject(response);
                                String user=jsonObject.getJSONObject("data").toString();
                                Gson gson = new Gson();
                                SupportTeamModel superMan = gson.fromJson(user.toString(), SupportTeamModel.class);
                                String value = gson.toJson(superMan);
                                //Toast.makeText(activity, ""+value.toString(), Toast.LENGTH_SHORT).show();
                                supportTeamManager.setUserDetails("userDetail", superMan);
                                supportTeamManager.setBooleanData("support", true);
                                Intent intent = new Intent(activity, SupportTeamHome.class);
                                startActivity(intent);
                                finishAffinity();

                            } catch (JSONException e) {
                                Log.e("eee",e.toString());
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("eee1",error.toString());
                        }
                    }){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<String,String>();
                            map.put("email",email.getText().toString());
                            map.put("password",pass.getText().toString());
                            return map;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(activity);
                    requestQueue.add(stringRequest);
                }

            }
        });

    }

}