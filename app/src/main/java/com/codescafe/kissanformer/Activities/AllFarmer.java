package com.codescafe.kissanformer.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.codescafe.kissanformer.MainActivity;
import com.codescafe.kissanformer.R;
import com.codescafe.kissanformer.adapter.AdapterAudio;
import com.codescafe.kissanformer.adapter.AdapterFarmers;
import com.codescafe.kissanformer.auth.DataEnterManager;
import com.codescafe.kissanformer.model.CityModel;
import com.codescafe.kissanformer.model.DataEntryModel;
import com.codescafe.kissanformer.model.FarmerModel;
import com.codescafe.kissanformer.supportteam.AllChat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllFarmer extends AppCompatActivity {


    LinearLayout rvFarmers;
    Activity activity;
    TextView name, speciality, experience;
    EditText password;
    int textlength = 0;
    ArrayList<FarmerModel> array_sort = new ArrayList<>();
    ArrayList<FarmerModel> list = new ArrayList<>();
    AdapterFarmers adapterFarmers;
    DataEnterManager dataEnterManager;
    DataEntryModel dataEntryModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_farmer);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        activity = this;
        dataEnterManager = new DataEnterManager(activity);
        dataEntryModel = dataEnterManager.getUserDetails("Userdetail");
        rvFarmers = findViewById(R.id.rvView);
        password = findViewById(R.id.password);
        password.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textlength = password.getText().length();
                array_sort.clear();
                for (int i = 0; i < list.size(); i++) {
                    if (textlength <= list.get(i).getFfname().length()) {
                        Log.d("ertyyy", list.get(i).getFfname().toLowerCase().trim());
                        if (list.get(i).getFfname().toLowerCase().trim().contains(
                                password.getText().toString().toLowerCase().trim())) {
                            array_sort.add(list.get(i));
                        }
                    }
                }

//                adapterFarmers = new AdapterFarmers(array_sort,AllFarmer.this);
//                rvfarmers.setAdapter(adapterFarmers);

            }
        });
        getFarmers();

    }
    private void getFarmers() {
        RequestQueue queue = Volley.newRequestQueue(activity);
        String url = "https://kissancare.reedspak.org/fetchFarmersInfo.php";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        // trustEveryone();
                        //Toast.makeText(activity, "hi", Toast.LENGTH_SHORT).show();
                        Gson gson = new Gson();
                        //ModelScore modelScore = gson.fromJson(response.toString(), ModelScore.class);
                        // scoreArrayList.add(modelScore);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.toString());
                            String s = jsonObject.getJSONArray("All_virtual").toString();
                            list = gson.fromJson(s, new TypeToken<ArrayList<FarmerModel>>() {
                            }.getType());

                            if (list.size() > 0) {
                                //Toast.makeText(activity, ""+list.size(), Toast.LENGTH_SHORT).show();

                                for (int i = 0; i < list.size(); i++) {
                                    View itemView = getLayoutInflater().inflate(R.layout.item_doc, null);

                                    name = itemView.findViewById(R.id.name);
                                    speciality = itemView.findViewById(R.id.speciality);
                                    experience = itemView.findViewById(R.id.experience);

                                    try {
                                        name.setText("" + list.get(i).getFfname());
                                        speciality.setText("" + list.get(i).getFather_name());
                                        experience.setText("" + list.get(i).getCity());
                                        rvFarmers.addView(itemView);

                                        ArrayList<CityModel> cityList = new ArrayList<>();
                                        cityList = MainActivity.getTehsil("tehsil",AllFarmer.this);

                                        for (int k = 0; cityList.size() > k; k++) {
                                            for (int j = 0; list.size() > j; j++) {
                                                if (cityList.get(k).getTehsil_id().equals(list.get(j).getCity())) {
                                                    experience.setText("" + cityList.get(k).getTehsil_name());
                                                }
                                            }
                                        }

                                        int finalI = i;
                                        itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(AllFarmer.this, FamerProfile.class);
                                                intent.putExtra("model", list.get(finalI));
                                                startActivity(intent);
                                            }
                                        });
                                    } catch (Exception e) {

                                    }

                                }
                            }
//                            adapterPost = new AdapterPost(getActivity(), list);
//                            rvPostPak.setAdapter(adapterPost);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("eee1", error.toString());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("responsible_person", dataEntryModel.getUser_id());
                return map;
            }
        };
        queue.add(stringRequest);
    }
}