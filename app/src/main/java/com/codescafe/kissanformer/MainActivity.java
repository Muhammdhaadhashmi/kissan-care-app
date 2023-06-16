package com.codescafe.kissanformer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codescafe.kissanformer.Activities.AddFarmer;
import com.codescafe.kissanformer.Activities.AllFarmer;
import com.codescafe.kissanformer.Activities.AudioFiles;
import com.codescafe.kissanformer.Activities.ChangePassword;
import com.codescafe.kissanformer.Activities.OfflineFarmersData;
import com.codescafe.kissanformer.Activities.PdfFiles;
import com.codescafe.kissanformer.Activities.SupportChat;
import com.codescafe.kissanformer.Activities.VideoFiles;
import com.codescafe.kissanformer.Interface.Common;
import com.codescafe.kissanformer.adapter.AdapterUC;
import com.codescafe.kissanformer.auth.DataEnterManager;
import com.codescafe.kissanformer.auth.Login;
import com.codescafe.kissanformer.model.CityModel;
import com.codescafe.kissanformer.model.DataEntryModel;
import com.codescafe.kissanformer.model.DistrictModel;
import com.codescafe.kissanformer.model.EducationModel;
import com.codescafe.kissanformer.model.FarmerModel;
import com.codescafe.kissanformer.model.ProvinceModel;
import com.codescafe.kissanformer.model.UcModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ImageView menu,loguot;
    DrawerLayout drawer;
    CardView addFarmer,farmerRecords,pdfFiles,videoFiles,audioFiles,passwordChange,offlineFarmers;
    Activity activity;
    NavigationView nav_view;
    DataEnterManager dataEnterManager;
    DataEntryModel dataEntryModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        activity = this;
        dataEnterManager = new DataEnterManager(activity);
        dataEntryModel = dataEnterManager.getUserDetails("userDetail");

        loguot = findViewById(R.id.logout);
        drawer = findViewById(R.id.drawer);
        menu = findViewById(R.id.menu);
        nav_view = findViewById(R.id.nav_view);
        View headerView = nav_view.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.usernameTxt);
        navUsername.setText(""+dataEntryModel.getUser_name());
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        addFarmer = findViewById(R.id.addFarmer);
        farmerRecords = findViewById(R.id.farmerRecords);
        pdfFiles = findViewById(R.id.pdfFiles);
        videoFiles = findViewById(R.id.videoFiles);
        audioFiles = findViewById(R.id.audioFiles);
        passwordChange = findViewById(R.id.passwordChange);
        offlineFarmers = findViewById(R.id.offlineFarmers);

        addFarmer.setOnClickListener(view -> startActivity(new Intent(activity, AddFarmer.class)));
        farmerRecords.setOnClickListener(view -> startActivity(new Intent(activity, AllFarmer.class)));
        pdfFiles.setOnClickListener(view -> startActivity(new Intent(activity, PdfFiles.class)));
        videoFiles.setOnClickListener(view -> startActivity(new Intent(activity, VideoFiles.class)));
        audioFiles.setOnClickListener(view -> startActivity(new Intent(activity, AudioFiles.class)));
        passwordChange.setOnClickListener(view -> startActivity(new Intent(activity, ChangePassword.class)));
        offlineFarmers.setOnClickListener(view -> startActivity(new Intent(activity, OfflineFarmersData.class)));

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment frag = null;
                item.setChecked(true);
                int itemId = item.getItemId();

                if (itemId == R.id.share) {
                    drawer.close();
                    try {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, ""+getString(R.string.app_name));
                        String shareMessage = "\nLet me recommend you this application\n\n";
                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        startActivity(Intent.createChooser(shareIntent, "choose one"));
                    } catch (Exception e) {

                    }

                } else if (itemId == R.id.login) {
                    drawer.close();
                    startActivity(new Intent(activity, Login.class));
                } else if (itemId == R.id.rate) {
                    drawer.close();
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    try {
                        startActivity(myAppLinkToMarket);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(getApplicationContext(), " unable to find app", Toast.LENGTH_LONG).show();
                    }
                }else if (itemId == R.id.download) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Are you sure you wanna download...???");
                    builder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Download();
                            //new DownloadFileFromURL().execute(file_url);
                            dialogInterface.dismiss();
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    builder.show();

                    drawer.close();

                }
                return false;
            }

        });
        getpermission();
        loguot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Are You Sure You Want To Logout");
                builder.setPositiveButton(Html.fromHtml("Logout"), (dialogInterface, i) -> {
                    dataEnterManager.logoutUser();
                    DataEntryModel user = new DataEntryModel();
                    user.setUser_id("0");
                    dataEnterManager.setUserDetails("", dataEntryModel);
                    startActivity(new Intent(activity, Login.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                });
                builder.setNegativeButton(Html.fromHtml("Cancel"), (dialogInterface, i) -> dialogInterface.dismiss());
                builder.show();
            }
        });
        //province
        String url = "https://kissancare.reedspak.org/fetchStates.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                try {
                    Log.e("eee", response.toString());
                    JSONObject jsonObject = new JSONObject(response);
                    String s = jsonObject.getJSONArray("data").toString();
                    ArrayList<ProvinceModel> list = gson.fromJson(s, new TypeToken<ArrayList<ProvinceModel>>() {
                    }.getType());


                    if (list.size() > 0) {
                        saveProvince(list,"province");
                    }
                } catch (JSONException e) {
                    Log.e("eee", e.toString());
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("eee1", error.toString());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
        //district
        String url1 = "https://kissancare.reedspak.org/fetchDistricts.php";
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                try {
                    Log.e("eee", response.toString());
                    JSONObject jsonObject = new JSONObject(response);
                    String s = jsonObject.getJSONArray("data").toString();
                    ArrayList<DistrictModel> list = gson.fromJson(s, new TypeToken<ArrayList<DistrictModel>>() {
                    }.getType());


                    if (list.size() > 0) {
                        saveDistrict(list,"district");
                    }
                } catch (JSONException e) {
                    Log.e("eee", e.toString());
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("eee1", error.toString());
            }
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(activity);
        requestQueue1.add(stringRequest1);
        //tehsil
        String url2 = "https://kissancare.reedspak.org/fetchTehsils.php";
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                try {
                    Log.e("eee", response.toString());
                    JSONObject jsonObject = new JSONObject(response);
                    String s = jsonObject.getJSONArray("data").toString();
                    ArrayList<CityModel> list = gson.fromJson(s, new TypeToken<ArrayList<CityModel>>() {
                    }.getType());

                    if (list.size() > 0) {
                        saveTehsil(list,"tehsil");
                    }
                } catch (JSONException e) {
                    Log.e("eee", e.toString());
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("eee1", error.toString());
            }
        });
        RequestQueue requestQueue2 = Volley.newRequestQueue(activity);
        requestQueue2.add(stringRequest2);
        //union consil
        String url3 = "https://kissancare.reedspak.org/fetchUC.php";
        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, url3, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                try {
                    Log.e("eee", response.toString());
                    JSONObject jsonObject = new JSONObject(response);
                    String s = jsonObject.getJSONArray("data").toString();
                    ArrayList<UcModel> list = gson.fromJson(s, new TypeToken<ArrayList<UcModel>>() {
                    }.getType());


                    if (list.size() > 0) {
                        saveUC(list,"uc");
                    }
                } catch (JSONException e) {
                    Log.e("eee", e.toString());
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("eee1", error.toString());
            }
        });
        RequestQueue requestQueue3 = Volley.newRequestQueue(activity);
        requestQueue3.add(stringRequest3);

        APICalling.Education_API(activity, new Common.APISuccessListener() {
            @Override
            public void onSuccessReceived(String success) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(success);
                    if (jsonObject.getString("success").equals("1")){
                        String ss = jsonObject.getJSONArray("data").toString();
                        ArrayList<EducationModel> list = new Gson().fromJson(ss,new TypeToken<ArrayList<EducationModel>>(){}.getType());
                        if (list.size()>0){
                            saveEducation(list,"education");
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Common.APIErrorListener() {
            @Override
            public void onErrorReceived(VolleyError error) {

            }
        });

    }
    public void saveEducation(ArrayList<EducationModel> list, String education){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(education, json);
        editor.apply();
    }
    public void saveProvince(ArrayList<ProvinceModel> list, String province){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(province, json);
        editor.apply();
    }
    public void saveDistrict(ArrayList<DistrictModel> list, String district){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(district, json);
        editor.apply();
    }
    public void saveTehsil(ArrayList<CityModel> list, String tehsil){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(tehsil, json);
        editor.apply();
    }
    public void saveUC(ArrayList<UcModel> list, String uc){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(uc, json);
        editor.apply();
    }
    public static ArrayList<EducationModel> getEducation(String education,Activity activity){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        Gson gson = new Gson();
        String json = prefs.getString(education, null);
        Type type = new TypeToken<ArrayList<EducationModel>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public static ArrayList<ProvinceModel> getProvince(String province,Activity activity){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        Gson gson = new Gson();
        String json = prefs.getString(province, null);
        Type type = new TypeToken<ArrayList<ProvinceModel>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public static ArrayList<DistrictModel> getDistrict(String district,Activity activity){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        Gson gson = new Gson();
        String json = prefs.getString(district, null);
        Type type = new TypeToken<ArrayList<DistrictModel>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public static ArrayList<CityModel> getTehsil(String tehsil, Activity activity){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        Gson gson = new Gson();
        String json = prefs.getString(tehsil, null);
        Type type = new TypeToken<ArrayList<CityModel>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public static ArrayList<UcModel> getUC(String uc, Activity activity){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        Gson gson = new Gson();
        String json = prefs.getString(uc, null);
        Type type = new TypeToken<ArrayList<UcModel>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public void getpermission() {
        String exread = Manifest.permission.READ_EXTERNAL_STORAGE;
        String exwrite = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String call_phone = Manifest.permission.CALL_PHONE;
        String mic = Manifest.permission.RECORD_AUDIO;
        if (ContextCompat.checkSelfPermission(this, exread) !=
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                exwrite) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                mic) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        call_phone) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{exread, exwrite, call_phone}, 200);
            }
        }
    }
    private void downloadDoc(Context context, String directoryDownloads, String url, String extension) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri1 = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri1);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, directoryDownloads, extension);
        Objects.requireNonNull(downloadManager).enqueue(request);
    }
    public void Uploading(){
        ArrayList<FarmerModel> offlineList = new ArrayList<>();
        Gson gson1 = new Gson();
        SharedPreferences prefs = getSharedPreferences("OfflineList", MODE_PRIVATE);
        String json1 = prefs.getString("OfflineList", "");
        if (!(json1.equals(""))){
            offlineList = gson1.fromJson(json1, new TypeToken<ArrayList<FarmerModel>>() {
            }.getType());
        }
        for (int x = 0; offlineList.size()>x;x++){
            String url = "https://kissancare.reedspak.org/addfarmer.php";
            ArrayList<FarmerModel> finalOfflineList = offlineList;
            int finalX = x;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Display the first 500 characters of the response string.
                    // trustEveryone();
                    //Toast.makeText(activity, "hi", Toast.LENGTH_SHORT).show();
                    Log.e("offline data farmer", response.toString());
                    // JSONObject jsonObject;
                    //   jsonObject = new JSONObject(response);

                    // String ss = jsonObject.getString("1");
                    if (response.trim().equals("1")) {
                        Toast.makeText(MainActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Something Went Wrong. Try Again Later", Toast.LENGTH_SHORT).show();
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
                    map.put("LG_VG_Code",""+ finalOfflineList.get(finalX).getHub_code());
                    map.put("responsible_person", finalOfflineList.get(finalX).getResponsible_person());
                    map.put("lg_address", finalOfflineList.get(finalX).getLg_address());
                    map.put("UC", "" + finalOfflineList.get(finalX).getUnion_council());
                    map.put("city", "" + finalOfflineList.get(finalX).getCity());
                    map.put("District", "" + finalOfflineList.get(finalX).getDistrict());
                    map.put("Province", "" + finalOfflineList.get(finalX).getProvince());
                    map.put("Country", ""+finalOfflineList.get(finalX).getCountry());
                    map.put("Farmer_Name", finalOfflineList.get(finalX).getFfname());
                    map.put("FF_Phone", finalOfflineList.get(finalX).getFfmobile());
                    map.put("LF_Name", finalOfflineList.get(finalX).getLfname());
                    map.put("LF_Contact", finalOfflineList.get(finalX).getLfmobile());
                    map.put("Farmer_Code", finalOfflineList.get(finalX).getFcode());
                    map.put("date", "" + finalOfflineList.get(finalX).getDate());
                    map.put("image", "");
                    map.put("Farmer_CNIC", "0");
                    map.put("Father_Name", ""+finalOfflineList.get(finalX).getFather_name());
                    map.put("Year_of_Birth", ""+finalOfflineList.get(finalX).getYear_of_Birth());
                    map.put("Qaulification", ""+finalOfflineList.get(finalX).getQaulification());
                    map.put("Status_Active_Deactive", finalOfflineList.get(finalX).getActive());
                    map.put("Farmer_Gender", ""+finalOfflineList.get(finalX).getGender());
                    map.put("Farmer_Status", ""+finalOfflineList.get(finalX).getFarmer_status());
                    map.put("Total_Area", ""+finalOfflineList.get(finalX).getTotal_area());
                    map.put("Cotton_Area", ""+finalOfflineList.get(finalX).getCotton_area());
                    map.put("Expected_Production", finalOfflineList.get(finalX).getExpected_area());
                    map.put("Total_Expected_Production", "" + finalOfflineList.get(finalX).getTotal_production());
                    map.put("PU_PG", ""+finalOfflineList.get(finalX).getPu_vg());
                    map.put("PU_PG_Selection", ""+finalOfflineList.get(finalX).getPu_code());
                    map.put("CF_ID", "" + finalOfflineList.get(finalX).getFcf_id());
                    map.put("PUM_ID", "" + finalOfflineList.get(finalX).getFpum_id());
                    return map;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(activity);
            requestQueue.add(stringRequest);
        }

    }
    public void Download (){

        DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse("https://kissancare.reedspak.org/upload/pdf_file/manual_book_BCI_&_REEDS.pdf");
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("Manual Details");
        request.setDescription("Downloading");//request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"game-of-life");
        downloadmanager.enqueue(request);
    }

}