package com.codescafe.kissanformer.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity implements CustomSpinner.OnSpinnerEventsListener{

    TextView addFarmer, country,totalfarmola;
    //spinner
    TextView pu_pg,province,selecrDistrict,selectTehsil,selectUc,gender,qualification,status,farmerstatus;
    EditText farmerName, fatherName, farmerContactNumber, dateOfBirth, leadfarmerName, leadfarmerNumber, totalArea, cottonArea, expectedseed, lgvgCode, lg_address, farmerCode, pu_pg_Code;
    Activity activity;
    DataEnterManager dataEnterManager;
    DataEntryModel dataEntryModel;
    ArrayList<CPFModel> cpfList = new ArrayList<>();
    ArrayList<String> pupgList = new ArrayList<>();
    String provinceID = null,districtID = null,tehsilID= null,ucId = null;
    int para = 0;
    FarmerModel farmerModel;
    String editMode;
    BottomSheetDialog usSheetDialog,citySheetDialog,distSheetDialog,proSheetDialog,pupgSheetDialog,qualificationSheet;
    RecyclerView rv_routes;
    ArrayList<UcModel> uCList = new ArrayList<>();
    ArrayList<CityModel> tehsilList = new ArrayList<>();
    ArrayList<DistrictModel> distList = new ArrayList<>();
    ArrayList<ProvinceModel> proList = new ArrayList<>();
    //sort
    ArrayList<UcModel> sortUCArray = new ArrayList<>();
    ArrayList<CityModel> sortCityArray = new ArrayList<>();
    ArrayList<DistrictModel> sortDistrictArray = new ArrayList<>();
    ArrayList<EducationModel> educationList = new ArrayList<>();

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        activity = this;
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        dataEnterManager = new DataEnterManager(activity);
        dataEntryModel = dataEnterManager.getUserDetails("Userdetail");

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

        totalfarmola.setText(""+farmerModel.getTotal_production());
        pu_pg.setText(""+farmerModel.getPu_vg());
        lgvgCode.setText(""+farmerModel.getHub_code());
        pu_pg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pupgSheetDialog = new BottomSheetDialog(activity);
                View v = getLayoutInflater().inflate(R.layout.bottom_layout,null);
                pupgSheetDialog.setContentView(v);
                rv_routes = v.findViewById(R.id.rv_routes);
                rv_routes.setAdapter(new AdapterPUPG(pupgList));
                pupgSheetDialog.show();
            }
        });
        pu_pg_Code.setText(""+farmerModel.getPu_code());


        //uc
        ArrayList<UcModel> ucList = new ArrayList<>();
        ucList = MainActivity.getUC("uc", activity);
        for (int k = 0; ucList.size() > k; k++) {
            if (farmerModel.getUnion_council().equals(ucList.get(k).getUnion_council_id())) {
                selectUc.setText("" + ucList.get(k).getUnion_council_name());
            }
        }
        selectUc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usSheetDialog = new BottomSheetDialog(activity,R.style.BottomSheetDialogTheme);
                View v = getLayoutInflater().inflate(R.layout.bottom_layout,null);
                usSheetDialog.setContentView(v);
                rv_routes = v.findViewById(R.id.rv_routes);
                if (sortUCArray.size()>0){
                    rv_routes.setAdapter(new AdapterUc(sortUCArray));
                }else{
                    rv_routes.setAdapter(new AdapterUc(uCList));
                }
                usSheetDialog.show();
            }
        });

        //tehsil
        //Toast.makeText(activity, ""+farmerModel.getCity(), Toast.LENGTH_SHORT).show();
        ArrayList<CityModel> cityList = new ArrayList<>();
        cityList = MainActivity.getTehsil("tehsil", activity);
        for (int k = 0; cityList.size() > k; k++) {
            if (farmerModel.getCity().equals(cityList.get(k).getTehsil_id())) {
                selectTehsil.setText("" + cityList.get(k).getTehsil_name());
            }
        }
        selectTehsil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                citySheetDialog = new BottomSheetDialog(activity,R.style.BottomSheetDialogTheme);
                View v = getLayoutInflater().inflate(R.layout.bottom_layout,null);
                citySheetDialog.setContentView(v);
                rv_routes = v.findViewById(R.id.rv_routes);
                if (sortCityArray.size()>0){
                    rv_routes.setAdapter(new AdapterTehsil(sortCityArray));
                }else{
                    rv_routes.setAdapter(new AdapterTehsil(tehsilList));
                }
                citySheetDialog.show();
            }
        });

        //district
        ArrayList<DistrictModel> districtList = new ArrayList<>();
        districtList = MainActivity.getDistrict("district", activity);
        for (int k = 0; districtList.size() > k; k++) {
            if (farmerModel.getDistrict().equals(districtList.get(k).getDistrict_id())) {
                selecrDistrict.setText("" + districtList.get(k).getDistrict_name());
            }
        }
        selecrDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                distSheetDialog = new BottomSheetDialog(activity,R.style.BottomSheetDialogTheme);
                View v = getLayoutInflater().inflate(R.layout.bottom_layout,null);
                distSheetDialog.setContentView(v);
                rv_routes = v.findViewById(R.id.rv_routes);
                if (sortDistrictArray.size()>0){
                    rv_routes.setAdapter(new AdapterDist(sortDistrictArray));
                }else{
                    rv_routes.setAdapter(new AdapterDist(distList));
                }

                distSheetDialog.show();
            }
        });

        //province
        ArrayList<ProvinceModel> provinceList = new ArrayList<>();
        provinceList = MainActivity.getProvince("province", activity);
        for (int k = 0; provinceList.size() > k; k++) {
            if (farmerModel.getProvince().equals(provinceList.get(k).getState_id())) {
                province.setText("" + provinceList.get(k).getState_name());
            }
        }
        province.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proSheetDialog = new BottomSheetDialog(activity,R.style.BottomSheetDialogTheme);
                View v = getLayoutInflater().inflate(R.layout.bottom_layout,null);
                proSheetDialog.setContentView(v);
                rv_routes = v.findViewById(R.id.rv_routes);
                rv_routes.setAdapter(new AdapterPro(proList));
                proSheetDialog.show();
            }
        });
        //country
        country.setText("Pakistan");
        //
        //education
        educationList = MainActivity.getEducation("education",activity);
        ArrayList<String> educationTitle = new ArrayList<>();
        for (int i = 0; educationList.size() > i; i++) {
            educationTitle.add(educationList.get(i).getEdu_name());
        }
        qualification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qualificationSheet = new BottomSheetDialog(activity,R.style.BottomSheetDialogTheme);
                View v = getLayoutInflater().inflate(R.layout.bottom_layout,null);
                qualificationSheet.setContentView(v);
                rv_routes = v.findViewById(R.id.rv_routes);
                rv_routes.setAdapter(new AdapterEducation(educationTitle));
                qualificationSheet.show();
            }
        });
        //
        lg_address.setText(""+farmerModel.getLg_address());
        farmerCode.setText(""+farmerModel.getFcode());
        farmerName.setText(""+farmerModel.getFfname());
        fatherName.setText(""+farmerModel.getFather_name());
        farmerContactNumber.setText(""+farmerModel.getFfmobile());

        //
        gender.setText(""+farmerModel.getGender());
        qualification.setText(""+farmerModel.getQaulification());
        dateOfBirth.setText(""+farmerModel.getYear_of_Birth());
        status.setText(""+farmerModel.getActive());
        farmerstatus.setText(""+farmerModel.getFarmer_status());
        leadfarmerName.setText(""+farmerModel.getLfname());
        leadfarmerNumber.setText(""+farmerModel.getLfmobile());
        totalArea.setText(""+farmerModel.getTotal_area());
        cottonArea.setText(""+farmerModel.getCotton_area());
        expectedseed.setText(""+farmerModel.getExpected_area());
        totalfarmola.setText(""+farmerModel.getTotal_production());

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
                try {
                    idOF = cpfList.get(0).getFf_id();
                } catch (Exception e) {

                }
//                if (farmerName.getText().toString().equals("")) {
//                    Toast.makeText(activity, "Enter Farmer Name", Toast.LENGTH_SHORT).show();
//                } else if (farmerContactNumber.getText().toString().equals("")) {
//                    Toast.makeText(activity, "Enter Farmer Name", Toast.LENGTH_SHORT).show();
//                } else if (farmerContactNumber.getText().length() != 11) {
//                    Toast.makeText(activity, "Enter valid number", Toast.LENGTH_SHORT).show();
//                } else if (leadfarmerNumber.getText().length() != 11) {
//                    Toast.makeText(activity, "Enter valid number", Toast.LENGTH_SHORT).show();
//                } else if (provinceID.equals("")) {
//                    Toast.makeText(activity, "Select Province", Toast.LENGTH_SHORT).show();
//                } else if (districtID.equals("")) {
//                    Toast.makeText(activity, "Select District", Toast.LENGTH_SHORT).show();
//                } else if (tehsilID == null) {
//                    Toast.makeText(activity, "Select Tehsil", Toast.LENGTH_SHORT).show();
//                } else if (ucId.equals("")) {
//                    Toast.makeText(activity, "Select union consul", Toast.LENGTH_SHORT).show();
//                } else if (leadfarmerName.getText().toString().equals("")) {
//                    Toast.makeText(activity, "Enter Lead Farmer name", Toast.LENGTH_SHORT).show();
//                } else
                    if (dataEntryModel.getUser_id().equals(idOF)) {
                    //  Toast.makeText(activity, ""+(Integer.parseInt(cottonArea.getText().toString()))*(Integer.parseInt(expectedseed.getText().toString())), Toast.LENGTH_SHORT).show();
                    RequestQueue queue = Volley.newRequestQueue(EditProfile.this);
                    String url = "https://kissancare.reedspak.org/updateFarmerDatas.php";

// Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onResponse(String response) {
                                    // Display the first 500 characters of the response string.
                                    // trustEveryone();
                                    //Toast.makeText(activity, "hi", Toast.LENGTH_SHORT).show();
                                   // JSONObject jsonObject;
                                    //jsonObject = new JSONObject(response);
                                    Log.e("eeesaaseeee", response);
                                    //String ss = jsonObject.getString("1");
                                    if (response.toString().equals("1")) {
                                        Toast.makeText(EditProfile.this, "Ture", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(EditProfile.this, "False", Toast.LENGTH_SHORT).show();
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
                            map.put("LG_VG_Code", lgvgCode.getText().toString().trim());
                            map.put("LG_VG_Address", lg_address.getText().toString().trim());
                            map.put("UC", ""+ucId);
                            map.put("Tehsil", ""+tehsilID);
                            map.put("PU_PG", pu_pg.getText().toString().trim());
                            map.put("PU_PG_Selection", pu_pg_Code.getText().toString().trim());
                            map.put("Province", provinceID);
                            map.put("District", districtID);
                            map.put("FF_Phone", farmerContactNumber.getText().toString().trim());
                            map.put("LF_Name", leadfarmerName.getText().toString().trim());
                            map.put("LF_Contact", leadfarmerNumber.getText().toString().trim());
                            map.put("Farmer_Code", farmerCode.getText().toString().trim());
                            map.put("Farmer_Name", farmerName.getText().toString().trim());
                            map.put("Father_Name", fatherName.getText().toString().trim());
                            map.put("Qaulification", qualification.getText().toString());
                            map.put("Farmer_Status", farmerstatus.getText().toString().trim());
                            map.put("Farmer_Gender", gender.getText().toString().trim());
                            map.put("Total_Area", totalArea.getText().toString().trim());
                            map.put("Cotton_Area", cottonArea.getText().toString().trim());
                            map.put("Expected_Production", expectedseed.getText().toString().trim());
                            map.put("Total_Expected_Production", "" +totalfarmola.getText().toString().trim());
                            map.put("Status_Active_Deactive", status.getText().toString());
                            map.put("Year_of_Birth", dateOfBirth.getText().toString().trim());
                            //(Integer.parseInt(cottonArea.getText().toString())) * (Integer.parseInt(expectedseed.getText().toString()))
                            return map;
                        }
                    };
                    queue.add(stringRequest);
                } else {
                    Toast.makeText(activity, "You are not capable of add data", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //setLists
        setListVal();
    }
    private void setValuesDistrict(String provinceID) {
        //district
        ArrayList<DistrictModel> districtList = new ArrayList<>();
        sortDistrictArray = new ArrayList<>();
        districtList = MainActivity.getDistrict("district", EditProfile.this);
        for (int i=0;districtList.size()>i;i++) {
            if (districtList.get(i).getState_id().equals(provinceID)){
                sortDistrictArray.add(districtList.get(i));
            }
        }

    }
    private void setValueTehsil(String districtID) {
        //tehsil
        ArrayList<CityModel> tehsillist = new ArrayList<>();
        sortCityArray = new ArrayList<>();
        tehsillist = MainActivity.getTehsil("tehsil", EditProfile.this);
        for (int i=0;tehsillist.size()>i;i++){
            if (tehsillist.get(i).getDistrict_id().equals(districtID)){
                sortCityArray.add(tehsillist.get(i));
            }
        }
    }
    private void setValueUC(String tehsilID) {
        //uc
        ArrayList<UcModel> ucList = new ArrayList<>();
        sortUCArray = new ArrayList<>();
        ucList = MainActivity.getUC("uc", EditProfile.this);
        for (int i = 0;ucList.size()>i;i++) {
            if (ucList.get(i).getTehsil_id().equals(tehsilID)){
                sortUCArray.add(ucList.get(i));
            }
        }
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
    public  void setListVal(){
        uCList = MainActivity.getUC("uc",activity);
        tehsilList = MainActivity.getTehsil("tehsil", activity);
        distList = MainActivity.getDistrict("district", activity);
        proList = MainActivity.getProvince("province",activity);
    }
    //ucAdapter
    public class AdapterUc extends RecyclerView.Adapter<AdapterUc.ViewHolder> {

        ArrayList<UcModel> list = new ArrayList<>();

        public AdapterUc(ArrayList<UcModel> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public AdapterUc.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mm,parent,false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull AdapterUc.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            if (list.size()>0){
                holder.name.setText(""+list.get(position).getUnion_council_name());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectUc.setText(""+list.get(position).getUnion_council_name());
                        ucId = list.get(position).getUnion_council_id();
                        usSheetDialog.dismiss();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
            }
        }
    }
    //tehsilAdapter
    public class AdapterTehsil extends RecyclerView.Adapter<AdapterTehsil.ViewHolder> {

        ArrayList<CityModel> list = new ArrayList<>();

        public AdapterTehsil(ArrayList<CityModel> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public AdapterTehsil.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mm,parent,false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull AdapterTehsil.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            if (list.size()>0){
                holder.name.setText(""+list.get(position).getTehsil_name());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectTehsil.setText(""+list.get(position).getTehsil_name());
                        tehsilID = list.get(position).getTehsil_id();
                        setValueUC(tehsilID);
                        citySheetDialog.dismiss();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
            }
        }
    }
    //tehsilAdapter
    public class AdapterDist extends RecyclerView.Adapter<AdapterDist.ViewHolder> {

        ArrayList<DistrictModel> list = new ArrayList<>();

        public AdapterDist(ArrayList<DistrictModel> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public AdapterDist.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mm,parent,false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull AdapterDist.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            if (list.size()>0){
                holder.name.setText(""+list.get(position).getDistrict_name());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selecrDistrict.setText(""+list.get(position).getDistrict_name());
                        districtID = list.get(position).getDistrict_id();
                        setValueTehsil(districtID);
                        distSheetDialog.dismiss();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
            }
        }
    }
    //tehsilAdapter
    public class AdapterPro extends RecyclerView.Adapter<AdapterPro.ViewHolder> {

        ArrayList<ProvinceModel> list = new ArrayList<>();

        public AdapterPro(ArrayList<ProvinceModel> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public AdapterPro.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mm,parent,false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull AdapterPro.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            if (list.size()>0){
                holder.name.setText(""+list.get(position).getState_name());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        province.setText(""+list.get(position).getState_name());
                        provinceID = list.get(position).getState_id();
                        setValuesDistrict(provinceID);
                        proSheetDialog.dismiss();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
            }
        }
    }
    //tehsilAdapter
    public class AdapterPUPG extends RecyclerView.Adapter<AdapterPUPG.ViewHolder> {

        ArrayList<String> list = new ArrayList<>();

        public AdapterPUPG(ArrayList<String> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public AdapterPUPG.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mm,parent,false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull AdapterPUPG.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            if (list.size()>0){
                holder.name.setText(""+list.get(position));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pu_pg.setText(""+list.get(position));
                        pupgSheetDialog.dismiss();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
            }
        }
    }
    //educationAdapter
    public class AdapterEducation extends RecyclerView.Adapter<AdapterEducation.ViewHolder> {

        ArrayList<String> list = new ArrayList<>();

        public AdapterEducation(ArrayList<String> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public AdapterEducation.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mm,parent,false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull AdapterEducation.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            if (list.size()>0){
                holder.name.setText(""+list.get(position));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        qualification.setText(""+list.get(position));
                        qualificationSheet.dismiss();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
            }
        }
    }


}