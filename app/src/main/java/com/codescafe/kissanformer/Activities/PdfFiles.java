package com.codescafe.kissanformer.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codescafe.kissanformer.R;
import com.codescafe.kissanformer.adapter.AdapterAudio;
import com.codescafe.kissanformer.adapter.AdapterPdf;
import com.codescafe.kissanformer.model.AllMediaModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PdfFiles extends AppCompatActivity {

    RecyclerView rvPdf;
    TextView noData;
    ProgressBar progressBar;
    EditText password;
    int textlength = 0;
    ArrayList<AllMediaModel> array_sort = new ArrayList<>();
    ArrayList<AllMediaModel> sortList = new ArrayList<>();
    AdapterPdf adapterIncoming;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_files);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        ImageView back;
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        rvPdf = findViewById(R.id.rvPdf);
        progressBar = findViewById(R.id.progressBar);
        noData = findViewById(R.id.noData);

        password = findViewById(R.id.password);
        password.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textlength = password.getText().length();
                array_sort.clear();
                for (int i = 0; i < sortList.size(); i++) {
                    if (textlength <= sortList.get(i).getFar_description().length()) {
                        Log.d("ertyyy", sortList.get(i).getFar_description().toLowerCase().trim());
                        if (sortList.get(i).getFar_description().toLowerCase().trim().contains(
                                password.getText().toString().toLowerCase().trim())) {
                            array_sort.add(sortList.get(i));
                        }
                    }
                }
                adapterIncoming = new AdapterPdf(PdfFiles.this, array_sort);
                rvPdf.setAdapter(adapterIncoming);

            }
        });
        LoadPost();
    }
    private void LoadPost() {
        RequestQueue queue = Volley.newRequestQueue(PdfFiles.this);
        String url = "https://kissancare.reedspak.org/fetchAFData.php";

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
                            ArrayList<AllMediaModel> list = gson.fromJson(s, new TypeToken<ArrayList<AllMediaModel>>() {
                            }.getType());

                            //Toast.makeText(AudioFiles.this, "" + list.size(), Toast.LENGTH_SHORT).show();
                            // LayoutInflater inflater=LayoutInflater.from(getContext());
                            sortList = new ArrayList<>();
                            sortList.clear();
                            if (list.size() > 0) {
                                for (int i=0;list.size()>i;i++){
                                    if (list.get(i).getFar_type().equals("1")){
                                        sortList.add(list.get(i));
                                    }
                                    // Toast.makeText(AudioFiles.this, ""+sortList.size(), Toast.LENGTH_SHORT).show();
                                }

                                if (sortList.size()>0){
                                    progressBar.setVisibility(View.GONE);
                                    rvPdf.setVisibility(View.VISIBLE);
                                    adapterIncoming = new AdapterPdf(PdfFiles.this, sortList);
                                    rvPdf.setAdapter(adapterIncoming);
                                }
                                if (sortList.size()==0){
                                    progressBar.setVisibility(View.GONE);
                                    rvPdf.setVisibility(View.GONE);
                                    noData.setVisibility(View.VISIBLE);
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
    }

}