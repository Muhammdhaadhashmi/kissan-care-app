package com.codescafe.kissanformer.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.util.Util;
import com.codescafe.kissanformer.R;
import com.codescafe.kissanformer.auth.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class OTP extends AppCompatActivity {

    EditText edt1;
    ProgressDialog progressDialog;
    boolean show = false;
    int timeout = 100;
    boolean sentotp = false;
    Activity activity;
    String val;
    TextView login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        activity = this;
        AndroidNetworking.initialize(activity);

        val = getIntent().getStringExtra("email");
        Toast.makeText(activity, "" + val, Toast.LENGTH_SHORT).show();
        login_btn = findViewById(R.id.login_btn);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("OTP Sending");
        progressDialog.setCancelable(true);
        //userModel= (UserModel) getIntent().getSerializableExtra("model");
        edt1 = findViewById(R.id.edt1);

        //  Toast.makeText(this, ""+userModel.getPhone(), Toast.LENGTH_SHORT).show();
        // initiateotp();
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runNow();
               // Toast.makeText(activity, "" + edt1.getText().toString().trim(), Toast.LENGTH_SHORT).show();
//                AndroidNetworking.post("https://kissancare.reedspak.org/verifyToken.php")
//                        .addBodyParameter("token", ""+edt1.getText().toString().trim())
//                        .setPriority(Priority.HIGH)
//                        .build()
//                        .getAsJSONObject(new JSONObjectRequestListener() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                // do anything with response
//                                initiateotp();
//                                Log.e("ease", response.toString());
//                                String ss = response.toString();
//                                if (ss.equals("1")) {
//                                    //userManager.setBooleanData("login",true,activity);
//                                    Intent intent = new Intent(activity, NewPassword.class);
//                                    intent.putExtra("email",""+val);
//                                    startActivity(intent);
//                                    finish();
//                                } else {
//                                    Toast.makeText(activity, "Invalid OTP", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                            @Override
//                            public void onError(ANError error) {
//                                // handle error
//                                Log.e("err", error.toString());
//                                error.printStackTrace();
//                            }
//                        });
//                public static final MediaType JSON
//                        = MediaType.get("application/json; charset=utf-8");
//
//                OkHttpClient client = new OkHttpClient();
//
//                String post(String url, String json) throws IOException {
//                    RequestBody body = RequestBody.create(json, JSON);
//                    okhttp3.Request request = new okhttp3.Request().Builder()
//                            .url(url)
//                            .post(body)
//                            .build();
//                    try (Response response = client.newCall(request).execute()) {
//                        return response.body().string();
//                    }
//                }

//                HttpClient client = new DefaultHttpClient();
//                HttpPost post = new HttpPost("https://kissancare.reedspak.org/verifyToken.php");
//
//                List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
//                urlParameters.add(new BasicNameValuePair("token", ""+edt1.getText().toString().trim()));
//
//                try {
//                    post.setEntity(new UrlEncodedFormEntity(urlParameters));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//
//                try {
//                    HttpResponse response = client.execute(post);
//                    Log.e("reposnse",""+response.toString());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        });
    }

    public void runNow() {
        initiateotp();
        //String e1=edt1.getText().toString().trim();
        String url = "https://kissancare.reedspak.org/verifyToken.php";
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("otp", response);
                if (response.trim().equals("1")) {
                    //   userManager.setBooleanData("login",true,activity);
                    progressDialog.dismiss();
                    Intent intent = new Intent(activity, NewPassword.class);
                    intent.putExtra("email", "" + val);
                    startActivity(intent);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(activity, "Invalid OTP", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("err", error.getMessage());
                Utils.parseVolleyError(error, activity);
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("token",""+edt1.getText().toString().trim());
                map.put("email",""+val);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        requestQueue.add(request);
    }

    private void initiateotp() {
        progressDialog.show();
    }


}