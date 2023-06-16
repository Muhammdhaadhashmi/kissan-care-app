package com.codescafe.kissanformer.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codescafe.kissanformer.Activities.FamerProfile;
import com.codescafe.kissanformer.Activities.OfflineFarmersData;
import com.codescafe.kissanformer.R;
import com.codescafe.kissanformer.model.CityModel;
import com.codescafe.kissanformer.model.FarmerModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterOfflineFarmers extends RecyclerView.Adapter<AdapterOfflineFarmers.ViewHolder> {

    ArrayList<FarmerModel> list;
    Context context;

    public AdapterOfflineFarmers(ArrayList<FarmerModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterOfflineFarmers.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_doc, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOfflineFarmers.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (list.size()>0){
            FarmerModel farmerModel = list.get(position);
            holder.name.setText("" + farmerModel.getFfname());
            holder.speciality.setText("" + farmerModel.getFather_name());
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = "https://kissancare.reedspak.org/fetchTehsils.php";

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
//                    Intent intent = new Intent(context, FamerProfile.class);
//                    intent.putExtra("model",farmerModel);
//                    context.startActivity(intent);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Make Your Decision..?");
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ((OfflineFarmersData)context).uploadThis(farmerModel,holder.getAdapterPosition());
                            dialogInterface.dismiss();
                        }
                    });

                    builder.show();
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                    builder.setTitle("Are you sure you wanna delete...?");
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @SuppressLint("CommitPrefEdits")
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            list.remove(position);
                            notifyItemRemoved(position);
                            Gson gson = new Gson();
                            context.getSharedPreferences("OfflineList",Context.MODE_PRIVATE).edit().putString("OfflineList",gson.toJson(list)).apply();
                            dialogInterface.dismiss();
                        }
                    });

                    builder.show();
                    return false;
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