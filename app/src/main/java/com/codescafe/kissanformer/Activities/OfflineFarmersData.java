package com.codescafe.kissanformer.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codescafe.kissanformer.MainActivity;
import com.codescafe.kissanformer.R;
import com.codescafe.kissanformer.adapter.AdapterOfflineFarmers;
import com.codescafe.kissanformer.model.FarmerModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OfflineFarmersData extends AppCompatActivity {

    RecyclerView rvOfflineFarmers;
    Activity activity;
    ArrayList<FarmerModel> finalOfflineList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_farmers_data);
        activity = this;
        rvOfflineFarmers = findViewById(R.id.rvOfflineFarmers);

     getOfflineFarmers();
    }

    private void getOfflineFarmers() {

        Gson gson1 = new Gson();
        SharedPreferences prefs = getSharedPreferences("OfflineList", MODE_PRIVATE);
        String json1 = prefs.getString("OfflineList", "");
        if (!(json1.equals(""))){
            finalOfflineList = gson1.fromJson(json1, new TypeToken<ArrayList<FarmerModel>>() {
            }.getType());
        }

        AdapterOfflineFarmers adapterOfflineFarmers = new AdapterOfflineFarmers(finalOfflineList,activity);
        rvOfflineFarmers.setAdapter(adapterOfflineFarmers);

    }

    public void uploadThis(FarmerModel farmerModel, int adapterPosition) {
            String url = "https://kissancare.reedspak.org/addfarmer.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("offline data farmer", response.toString());
                    if (response.trim().equals("1")) {
                        Toast.makeText(activity, "Successfully Added", Toast.LENGTH_SHORT).show();
                        finalOfflineList.remove(adapterPosition);
                        AdapterOfflineFarmers adapterOfflineFarmers = new AdapterOfflineFarmers(finalOfflineList,activity);
                        rvOfflineFarmers.setAdapter(adapterOfflineFarmers);
                        adapterOfflineFarmers.notifyItemRemoved(adapterPosition);
                        Gson gson = new Gson();
                        getSharedPreferences("OfflineList", Context.MODE_PRIVATE).edit().putString("OfflineList",gson.toJson(finalOfflineList)).apply();
                        getOfflineFarmers();
                    } else {
                        Toast.makeText(activity, "Something Went Wrong. Try Again Later", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("offline Uploading",""+error.getMessage());
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> map = new HashMap<>();
                    map.put("LG_VG_Code",""+ farmerModel.getHub_code());
                    map.put("responsible_person", farmerModel.getResponsible_person());
                    map.put("lg_address", farmerModel.getLg_address());
                    map.put("UC", "" + farmerModel.getUnion_council());
                    map.put("city", "" + farmerModel.getCity());
                    map.put("District", "" + farmerModel.getDistrict());
                    map.put("Province", "" + farmerModel.getProvince());
                    map.put("Country", ""+farmerModel.getCountry());
                    map.put("Farmer_Name", farmerModel.getFfname());
                    map.put("FF_Phone", farmerModel.getFfmobile());
                    map.put("LF_Name", farmerModel.getLfname());
                    map.put("LF_Contact", farmerModel.getLfmobile());
                    map.put("Farmer_Code", farmerModel.getFcode());
                    map.put("date", "" + farmerModel.getDate());
                    map.put("image", "");
                    map.put("Farmer_CNIC", "0");
                    map.put("Father_Name", ""+farmerModel.getFather_name());
                    map.put("Year_of_Birth", ""+farmerModel.getYear_of_Birth());
                    map.put("Qaulification", ""+farmerModel.getQaulification());
                    map.put("Status_Active_Deactive", farmerModel.getActive());
                    map.put("Farmer_Gender", ""+farmerModel.getGender());
                    map.put("Farmer_Status", ""+farmerModel.getFarmer_status());
                    map.put("Total_Area", ""+farmerModel.getTotal_area());
                    map.put("Cotton_Area", ""+farmerModel.getCotton_area());
                    map.put("Expected_Production", farmerModel.getExpected_area());
                    map.put("Total_Expected_Production", "" + farmerModel.getTotal_production());
                    map.put("PU_PG", ""+farmerModel.getPu_vg());
                    map.put("PU_PG_Selection", ""+farmerModel.getPu_code());
                    map.put("CF_ID", "" + farmerModel.getFcf_id());
                    map.put("PUM_ID", "" + farmerModel.getFpum_id());
                    return map;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(activity);
            requestQueue.add(stringRequest);

    }
}