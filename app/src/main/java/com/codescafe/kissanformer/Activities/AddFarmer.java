package com.codescafe.kissanformer.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codescafe.kissanformer.APICalling;
import com.codescafe.kissanformer.Interface.Common;
import com.codescafe.kissanformer.MainActivity;
import com.codescafe.kissanformer.R;
import com.codescafe.kissanformer.adapter.CustomSpinner;
import com.codescafe.kissanformer.adapter.SpinnerAdapter;
import com.codescafe.kissanformer.auth.DataEnterManager;
import com.codescafe.kissanformer.model.CPFModel;
import com.codescafe.kissanformer.model.CityModel;
import com.codescafe.kissanformer.model.DataEntryModel;
import com.codescafe.kissanformer.model.DistrictModel;
import com.codescafe.kissanformer.model.EducationModel;
import com.codescafe.kissanformer.model.FarmerModel;
import com.codescafe.kissanformer.model.ProvinceModel;
import com.codescafe.kissanformer.model.UcModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddFarmer extends AppCompatActivity implements CustomSpinner.OnSpinnerEventsListener {

    Spinner gender, status, farmerstatus;
    TextView addFarmer, country, totalfarmola;
    EditText farmerName, fatherName, farmerContactNumber, dateOfBirth, leadfarmerName, leadfarmerNumber, totalArea, cottonArea, expectedseed, lgvgCode, lg_address, farmerCode, pu_pg_Code;
    Activity activity;
    DataEnterManager dataEnterManager;
    DataEntryModel dataEntryModel;
    ArrayList<CPFModel> cpfList = new ArrayList<>();
    CustomSpinner pu_pg, province, selecrDistrict, selectTehsil, selectUc,qualification;
    ArrayList<String> pupgList = new ArrayList<>();
    ArrayList<EducationModel> educationList = new ArrayList<>();
    String provinceID = null, districtID = null, tehsilID = null, ucId = null,qualificationID= null;
    int para = 0;
    FarmerModel farmerModel;
    FarmerModel fmodel;
    String editMode;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_farmer);

        activity = this;
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        dataEnterManager = new DataEnterManager(activity);
        dataEntryModel = dataEnterManager.getUserDetails("Userdetail");
        fmodel = new FarmerModel("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "");

        farmerModel = (FarmerModel) getIntent().getSerializableExtra("model");
        editMode = getIntent().getStringExtra("edit");

        totalfarmola = findViewById(R.id.totalfarmola);
        pu_pg = findViewById(R.id.pu_pg);
        addFarmer = findViewById(R.id.addFarmer);
        country = findViewById(R.id.country);
        selecrDistrict = findViewById(R.id.selecrDistrict);
        farmerName = findViewById(R.id.farmerName);
        fatherName = findViewById(R.id.fatherName);
        qualification = findViewById(R.id.qualification);
        gender = findViewById(R.id.gender);
        dateOfBirth = findViewById(R.id.dateOfBirth);
        status = findViewById(R.id.status);
        leadfarmerName = findViewById(R.id.leadfarmerName);
        leadfarmerNumber = findViewById(R.id.leadfarmerNumber);
        farmerContactNumber = findViewById(R.id.farmerContactNumber);
        totalArea = findViewById(R.id.totalArea);
        cottonArea = findViewById(R.id.cottonArea);
        expectedseed = findViewById(R.id.expectedseed);
        lgvgCode = findViewById(R.id.lgvgCode);
        lg_address = findViewById(R.id.lg_address);
        farmerCode = findViewById(R.id.farmerCode);
        pu_pg_Code = findViewById(R.id.pu_pg_Code);
        farmerstatus = findViewById(R.id.farmerstatus);
        province = findViewById(R.id.province);
        selectTehsil = findViewById(R.id.selectTehsil);
        selectUc = findViewById(R.id.selectUc);

        getPUPGLIst();

        pu_pg.setSpinnerEventsListener(this);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(activity, pupgList);
        pu_pg.setAdapter(spinnerAdapter);

        //province
        ArrayList<ProvinceModel> list = new ArrayList<>();
        ArrayList<String> provinceNames = new ArrayList<>();
        list = MainActivity.getProvince("province", AddFarmer.this);
        for (int i = 0; list.size() > i; i++) {
            provinceNames.add(list.get(i).getState_name());
        }
        province.setSpinnerEventsListener(this);
        SpinnerAdapter provinceAdapter = new SpinnerAdapter(activity, provinceNames);
        province.setAdapter(provinceAdapter);
        ArrayList<ProvinceModel> finalList = list;
        province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Toast.makeText(activity, ""+ finalList.get(i).getState_id(), Toast.LENGTH_SHORT).show();
                provinceID = finalList.get(i).getState_id();
                setValuesDistrict(provinceID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        gender.setAdapter(adapter1);

        educationList = MainActivity.getEducation("education",activity);
        ArrayList<String> educationTitle = new ArrayList<>();
        for (int i = 0; educationList.size() > i; i++) {
            educationTitle.add(educationList.get(i).getEdu_name());
        }
        qualification.setSpinnerEventsListener(this);
        SpinnerAdapter qualificationAdapter = new SpinnerAdapter(activity, educationTitle);
        qualification.setAdapter(qualificationAdapter);
        ArrayList<String> finalListFinal = educationTitle;
        qualification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(activity, ""+ finalListFinal.get(i), Toast.LENGTH_SHORT).show();
                qualificationID = finalListFinal.get(i);
                qualification.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.status, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_item);
        status.setAdapter(adapter3);

        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this, R.array.duration, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_item);
        farmerstatus.setAdapter(adapter4);

        expectedseed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s = charSequence.toString();
                String s1 = cottonArea.getText().toString().trim();
                if (!s.equals("") && !s1.equals("")) {
                    int d = Integer.parseInt(s);
                    int d1 = Integer.parseInt(s1);
                    int f = d1 * d;
                    totalfarmola.setText("" + f);

                } else {
                    totalfarmola.setText("" + para);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cottonArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s = charSequence.toString();
                String s1 = expectedseed.getText().toString().trim();
                if (!s.equals("") && !s1.equals("")) {
                    int d = Integer.parseInt(s);
                    int d1 = Integer.parseInt(s1);
                    int f = d1 * d;
                    totalfarmola.setText("" + f);

                } else {
                    totalfarmola.setText("" + para);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        checkFFID();
        // Toast.makeText(activity, ""+dataEntryModel.getUser_id(), Toast.LENGTH_SHORT).show();
        addFarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                //Toast.makeText(activity, "" + date, Toast.LENGTH_SHORT).show();
                String idOF = null;
                ArrayList<CPFModel> idList = new ArrayList<>();
                Gson gson = new Gson();
                SharedPreferences sharedPreferences = getSharedPreferences("Idof", MODE_PRIVATE);
                String s = sharedPreferences.getString("Idof", "");
                //idList = getSharedPreferences("Idof",MODE_PRIVATE).getString("Idof","");
                if (!s.equals("")) {
                    idList.clear();
                    idList = gson.fromJson(s, new TypeToken<ArrayList<CPFModel>>() {
                    }.getType());
                }
                try {
                    idOF = idList.get(0).getFf_id();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (farmerName.getText().toString().equals("")) {
                    //Toast.makeText(activity, ""+idOF, Toast.LENGTH_SHORT).show();
                    Toast.makeText(activity, "Enter Farmer Name", Toast.LENGTH_SHORT).show();
                } else if (farmerContactNumber.getText().toString().equals("")) {
                    Toast.makeText(activity, "Enter Farmer Name", Toast.LENGTH_SHORT).show();
                } else if (farmerContactNumber.getText().length() != 11) {
                    Toast.makeText(activity, "Enter valid number", Toast.LENGTH_SHORT).show();
                } else if (leadfarmerNumber.getText().length() != 11) {
                    Toast.makeText(activity, "Enter valid number", Toast.LENGTH_SHORT).show();
                } else if (provinceID.equals("")) {
                    Toast.makeText(activity, "Select Province", Toast.LENGTH_SHORT).show();
                } else if (districtID.equals("")) {
                    Toast.makeText(activity, "Select District", Toast.LENGTH_SHORT).show();
                } else if (tehsilID == null) {
                    Toast.makeText(activity, "Select Tehsil", Toast.LENGTH_SHORT).show();
                } else if (ucId.equals("")) {
                    Toast.makeText(activity, "Select union consul", Toast.LENGTH_SHORT).show();
                } else if (leadfarmerName.getText().toString().equals("")) {
                    Toast.makeText(activity, "Enter Lead Farmer name", Toast.LENGTH_SHORT).show();
                } else if (dataEntryModel.getUser_id().equals(idOF)) {
                    //  Toast.makeText(activity, ""+(Integer.parseInt(cottonArea.getText().toString()))*(Integer.parseInt(expectedseed.getText().toString())), Toast.LENGTH_SHORT).show();
                    if (isNetworkConnected()) {
                        RequestQueue queue = Volley.newRequestQueue(AddFarmer.this);
                        String url = "https://kissancare.reedspak.org/addfarmer.php";
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onResponse(String response) {
                                        // Display the first 500 characters of the response string.
                                        // trustEveryone();
                                        //Toast.makeText(activity, "hi", Toast.LENGTH_SHORT).show();
                                        Log.e("add farmer", response.toString());
                                        // JSONObject jsonObject;
                                        //   jsonObject = new JSONObject(response);

                                        // String ss = jsonObject.getString("1");
                                        if (response.trim().equals("1")) {
                                            Toast.makeText(AddFarmer.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(AddFarmer.this, "Something Went Wrong. Try Again Later", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("add farmer error", error.toString());
                            }
                        }) {
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("LG_VG_Code", lgvgCode.getText().toString().trim());
                                map.put("responsible_person", dataEntryModel.getUser_id());
                                map.put("lg_address", lg_address.getText().toString().trim());
                                map.put("UC", "" + ucId);
                                map.put("city", "" + tehsilID);
                                map.put("District", "" + districtID);
                                map.put("Province", "" + provinceID);
                                map.put("Country", "1");
                                map.put("Farmer_Name", farmerName.getText().toString().trim());
                                map.put("FF_Phone", farmerContactNumber.getText().toString().trim());
                                map.put("LF_Name", leadfarmerName.getText().toString().trim());
                                map.put("LF_Contact", leadfarmerNumber.getText().toString().trim());
                                map.put("Farmer_Code", farmerCode.getText().toString().trim());
                                map.put("date", "" + date);
                                map.put("image", "");
                                map.put("Farmer_CNIC", "0");
                                map.put("Father_Name", fatherName.getText().toString().trim());
                                map.put("Year_of_Birth", dateOfBirth.getText().toString().trim());
                                map.put("Qaulification", qualificationID);
                                map.put("Status_Active_Deactive", status.getSelectedItem().toString());
                                map.put("Farmer_Gender", gender.getSelectedItem().toString());
                                map.put("Farmer_Status", farmerstatus.getSelectedItem().toString());
                                map.put("Total_Area", totalArea.getText().toString().trim());
                                map.put("Cotton_Area", cottonArea.getText().toString().trim());
                                map.put("Expected_Production", expectedseed.getText().toString().trim());
                                map.put("Total_Expected_Production", "" + totalfarmola.getText().toString().trim());
                                map.put("PU_PG", pu_pg.getSelectedItem().toString());
                                map.put("PU_PG_Selection", pu_pg_Code.getText().toString().trim());
                                map.put("CF_ID", "" + cpfList.get(0).getCpf_id());
                                map.put("PUM_ID", "" + cpfList.get(0).getPum_id());
                                //(Integer.parseInt(cottonArea.getText().toString())) * (Integer.parseInt(expectedseed.getText().toString()))
                                return map;
                            }
                        };
                        queue.add(stringRequest);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle("Internet is not Available You wanna save it offline...?");
                        ArrayList<CPFModel> finalIdList = idList;
                        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                fmodel.setHub_code(lgvgCode.getText().toString().trim());
                                fmodel.setResponsible_person(dataEntryModel.getUser_id());
                                fmodel.setUnion_council("" + ucId);
                                fmodel.setCity("" + tehsilID);
                                fmodel.setDistrict("" + districtID);
                                fmodel.setProvince("" + provinceID);
                                fmodel.setCountry("1");
                                fmodel.setFfname("" + farmerName.getText().toString().trim());
                                fmodel.setFfmobile("" + farmerContactNumber.getText().toString().trim());
                                fmodel.setLfname("" + leadfarmerName.getText().toString().trim());
                                fmodel.setLfmobile(leadfarmerNumber.getText().toString().trim());
                                fmodel.setFcode("" + farmerCode.getText().toString().trim());
                                fmodel.setDate("" + date);
                                fmodel.setImage("");
                                fmodel.setCnic("0");
                                fmodel.setFather_name("" + fatherName.getText().toString().trim());
                                fmodel.setYear_of_Birth("" + dateOfBirth.getText().toString().trim());
                                fmodel.setQaulification("" + qualification.getSelectedItem().toString());
                                fmodel.setActive("" + status.getSelectedItem().toString());
                                fmodel.setGender("" + gender.getSelectedItem().toString());
                                fmodel.setFarmer_status("" + farmerstatus.getSelectedItem().toString());
                                fmodel.setTotal_area("" + totalArea.getText().toString().trim());
                                fmodel.setCotton_area("" + cottonArea.getText().toString().trim());
                                fmodel.setExpected_area("" + expectedseed.getText().toString().trim());
                                fmodel.setTotal_production("" + totalfarmola.getText().toString().trim());
                                fmodel.setPu_code("" + pu_pg.getSelectedItem().toString());
                                fmodel.setPu_vg(pu_pg_Code.getText().toString().trim());
                                fmodel.setFcf_id("" + finalIdList.get(0).getCpf_id());
                                fmodel.setFpum_id("" + finalIdList.get(0).getPum_id());

                                ArrayList<FarmerModel> offlineList = new ArrayList<>();
                                Gson gson1 = new Gson();
                                SharedPreferences prefs = getSharedPreferences("OfflineList", MODE_PRIVATE);
                                String json1 = prefs.getString("OfflineList", "");
                                if (!(json1.equals(""))){
                                    offlineList = gson1.fromJson(json1, new TypeToken<ArrayList<FarmerModel>>() {
                                    }.getType());
                                }
                                offlineList.add(fmodel);
                                getSharedPreferences("OfflineList", MODE_PRIVATE).edit().putString("OfflineList", gson1.toJson(offlineList)).apply();
                                dialogInterface.dismiss();
                                Toast.makeText(activity, "Offline Data Save Sucessfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    }
                } else {
                    Toast.makeText(activity, "You are not capable of add data", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void setValuesDistrict(String provinceID) {
        //district
        ArrayList<DistrictModel> districtList = new ArrayList<>();
        ArrayList<String> districtNames = new ArrayList<>();
        districtList = MainActivity.getDistrict("district", AddFarmer.this);
        for (int i = 0; districtList.size() > i; i++) {
            if (districtList.get(i).getState_id().equals(provinceID)) {
                districtNames.add(districtList.get(i).getDistrict_name());
            }
        }
        selecrDistrict.setSpinnerEventsListener(this);
        SpinnerAdapter districtAdapter = new SpinnerAdapter(activity, districtNames);
        selecrDistrict.setAdapter(districtAdapter);
        ArrayList<DistrictModel> finalDistrictList = districtList;
        selecrDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(activity, ""+ finalDistrictList.get(i).getState_id(), Toast.LENGTH_SHORT).show();
                districtID = finalDistrictList.get(i).getDistrict_id();
                setValueTehsil(districtID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setValueTehsil(String districtID) {
        //tehsil
        ArrayList<CityModel> tehsillist = new ArrayList<>();
        ArrayList<String> tehsilNames = new ArrayList<>();
        tehsillist = MainActivity.getTehsil("tehsil", AddFarmer.this);
        for (int i = 0; tehsillist.size() > i; i++) {
            if (tehsillist.get(i).getDistrict_id().equals(districtID)) {
                tehsilNames.add(tehsillist.get(i).getTehsil_name());
            }
        }
        selectTehsil.setSpinnerEventsListener(this);
        SpinnerAdapter tehsilAdapter = new SpinnerAdapter(activity, tehsilNames);
        selectTehsil.setAdapter(tehsilAdapter);
        ArrayList<CityModel> finalTehsillist = tehsillist;
        selectTehsil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(activity, ""+ finalDistrictList.get(i).getState_id(), Toast.LENGTH_SHORT).show();
                tehsilID = finalTehsillist.get(i).getTehsil_id();
                setValueUC(tehsilID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setValueUC(String tehsilID) {
        //uc
        ArrayList<UcModel> ucList = new ArrayList<>();
        ArrayList<String> ucNames = new ArrayList<>();
        ucList = MainActivity.getUC("uc", AddFarmer.this);
        for (int i = 0; ucList.size() > i; i++) {
            if (ucList.get(i).getTehsil_id().equals(tehsilID)) {
                ucNames.add(ucList.get(i).getUnion_council_name());
            }
        }
        selectUc.setSpinnerEventsListener(this);
        SpinnerAdapter ucAdapter = new SpinnerAdapter(activity, ucNames);
        selectUc.setAdapter(ucAdapter);
        ArrayList<UcModel> finalUcList = ucList;
        selectUc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ucId = finalUcList.get(i).getUnion_council_id();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getPUPGLIst() {
        pupgList.add("PU");
        pupgList.add("PG");
    }

    private void checkFFID() {
        String url = "https://kissancare.reedspak.org/fetchCpf.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                try {
                    Log.e("eee", response.toString());
                    JSONObject jsonObject = new JSONObject(response);
                    String s = jsonObject.getJSONArray("data").toString();
                    cpfList = gson.fromJson(s, new TypeToken<ArrayList<CPFModel>>() {
                    }.getType());

                    Gson gson1 = new Gson();
                    getSharedPreferences("Idof", MODE_PRIVATE).edit().putString("Idof", gson1.toJson(cpfList)).apply();
                    Log.e("val", response.toString());

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
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("ff_id", dataEntryModel.getUser_id());
                return hashMap;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onPopupWindowOpened(Spinner spinner) {
        //province.setBackground(getResources().getDrawable(R.drawable.bg_spinner_fruit_up));
    }

    @Override
    public void onPopupWindowClosed(Spinner spinner) {
        //province.setBackground(getResources().getDrawable(R.drawable.bg_spinner_fruit));
    }


}