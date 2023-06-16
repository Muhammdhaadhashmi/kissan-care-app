package com.codescafe.kissanformer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codescafe.kissanformer.Activities.FamerProfile;
import com.codescafe.kissanformer.R;
import com.codescafe.kissanformer.model.CityModel;
import com.codescafe.kissanformer.model.FarmerModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterFarmers extends RecyclerView.Adapter<AdapterFarmers.ViewHolder> {

    ArrayList<FarmerModel> list;
    Context context;

    public AdapterFarmers(ArrayList<FarmerModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterFarmers.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_doc, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFarmers.ViewHolder holder, int position) {

        if (list.size()>0){
            FarmerModel farmerModel = list.get(position);
            holder.name.setText("" + farmerModel.getFfname());
            holder.speciality.setText("" + farmerModel.getFather_name());
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = "https://kissancare.reedspak.org/fetchTehsils.php";

// Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            // trustEveryone();

                            Gson gson = new Gson();
                            //ModelScore modelScore = gson.fromJson(response.toString(), ModelScore.class);
                            // scoreArrayList.add(modelScore);
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response.toString());
                                String s = jsonObject.getJSONArray("data").toString();
                                ArrayList<CityModel> list = gson.fromJson(s, new TypeToken<ArrayList<CityModel>>() {
                                }.getType());

                                //Toast.makeText(getContext(), "" + list.size(), Toast.LENGTH_SHORT).show();
                                // LayoutInflater inflater=LayoutInflater.from(getContext());
                                if (list.size() > 0) {
                                    for (int i=0; list.size()>i;i++){
                                        if (list.get(i).getTehsil_id().equals(farmerModel.getCity())){
                                            holder.experience.setText("" + list.get(i).getTehsil_name());
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
            });
            queue.add(stringRequest);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, FamerProfile.class);
                    intent.putExtra("model",farmerModel);
                    context.startActivity(intent);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, speciality, experience;

        CircleImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            img = itemView.findViewById(R.id.img);
            speciality = itemView.findViewById(R.id.speciality);
            experience = itemView.findViewById(R.id.experience);
        }

    }
}