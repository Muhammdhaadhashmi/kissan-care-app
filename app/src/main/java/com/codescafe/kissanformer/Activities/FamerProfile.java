package com.codescafe.kissanformer.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.codescafe.kissanformer.MainActivity;
import com.codescafe.kissanformer.R;
import com.codescafe.kissanformer.model.CityModel;
import com.codescafe.kissanformer.model.DistrictModel;
import com.codescafe.kissanformer.model.FarmerModel;
import com.codescafe.kissanformer.model.ProvinceModel;
import com.codescafe.kissanformer.model.UcModel;

import java.util.ArrayList;

public class FamerProfile extends AppCompatActivity {


    TextView fname, farmerStatus, farmerCode, farmerFatherName, leadfarmerName, farmerContactNumber, leadfarmerNumber, gender,
            dbirth, qualification, puOrpg, puOrpgCode, lgOrbgCode, lg_address, tarea, cottonArea, expectedP, ttproduction,
            unionConsil,tehsil,district,province,country;
    CardView fab_add;
    FarmerModel farmerModel;
    Activity activity;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_famer_profile);
        activity = this;
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        farmerModel = (FarmerModel) getIntent().getSerializableExtra("model");

        fname = findViewById(R.id.name);
        farmerStatus = findViewById(R.id.farmerStatus);
        farmerCode = findViewById(R.id.farmerCode);
        farmerFatherName = findViewById(R.id.farmerFatherName);
        leadfarmerName = findViewById(R.id.leadfarmerName);
        farmerContactNumber = findViewById(R.id.farmerContactNumber);
        leadfarmerNumber = findViewById(R.id.leadfarmerNumber);
        gender = findViewById(R.id.gender);
        dbirth = findViewById(R.id.dbirth);
        qualification = findViewById(R.id.qualification);
        puOrpg = findViewById(R.id.puOrpg);
        puOrpgCode = findViewById(R.id.puOrpgCode);
        lgOrbgCode = findViewById(R.id.lgOrbgCode);
        lg_address = findViewById(R.id.lg_address);
        tarea = findViewById(R.id.tarea);
        cottonArea = findViewById(R.id.cottonArea);
        expectedP = findViewById(R.id.expectedP);
        ttproduction = findViewById(R.id.ttproduction);
        unionConsil = findViewById(R.id.unionConsil);
        tehsil = findViewById(R.id.tehsil);
        district = findViewById(R.id.district);
        province = findViewById(R.id.province);
        country = findViewById(R.id.country);
        fab_add = findViewById(R.id.fab_add);

        fname.setText("" + farmerModel.getFfname());
        farmerStatus.setText("" + farmerModel.getFarmer_status());
        farmerCode.setText("" + farmerModel.getFcode());
        farmerFatherName.setText("" + farmerModel.getFather_name());
        leadfarmerName.setText("" + farmerModel.getLfname());
        farmerContactNumber.setText("" + farmerModel.getFfmobile());
        leadfarmerNumber.setText("" + farmerModel.getLfmobile());
        gender.setText("" + farmerModel.getGender());
        dbirth.setText("" + farmerModel.getYear_of_Birth());
        qualification.setText("" + farmerModel.getQaulification());
        puOrpg.setText("" + farmerModel.getPu_vg());
        puOrpgCode.setText("" + farmerModel.getPu_code());
        lgOrbgCode.setText("" + farmerModel.getHub_code());
        lg_address.setText("" + farmerModel.getLg_address());
        tarea.setText("" + farmerModel.getTotal_area());
        cottonArea.setText("" + farmerModel.getCotton_area());
        expectedP.setText("" + farmerModel.getExpected_area());
        ttproduction.setText("" + farmerModel.getTotal_production());

        //uc
        //Toast.makeText(activity, ""+farmerModel.getUnion_council(), Toast.LENGTH_SHORT).show();
        ArrayList<UcModel> ucList = new ArrayList<>();
        ucList = MainActivity.getUC("uc", activity);
        for (int k = 0; ucList.size() > k; k++) {
            if (farmerModel.getUnion_council().equals(ucList.get(k).getUnion_council_id())) {
                unionConsil.setText("" + ucList.get(k).getUnion_council_name());
            }
        }

        //tehsil
        //Toast.makeText(activity, ""+farmerModel.getCity(), Toast.LENGTH_SHORT).show();
        ArrayList<CityModel> cityList = new ArrayList<>();
        cityList = MainActivity.getTehsil("tehsil", activity);
        for (int k = 0; cityList.size() > k; k++) {
            if (farmerModel.getCity().equals(cityList.get(k).getTehsil_id())) {
                tehsil.setText("," + cityList.get(k).getTehsil_name());
            }
        }

        //district
        ArrayList<DistrictModel> districtList = new ArrayList<>();
        districtList = MainActivity.getDistrict("district", activity);
        for (int k = 0; districtList.size() > k; k++) {
            if (farmerModel.getDistrict().equals(districtList.get(k).getDistrict_id())) {
                district.setText("," + districtList.get(k).getDistrict_name());
            }
        }

        //province
        ArrayList<ProvinceModel> provinceList = new ArrayList<>();
        provinceList = MainActivity.getProvince("province", activity);
        for (int k = 0; provinceList.size() > k; k++) {
            if (farmerModel.getProvince().equals(provinceList.get(k).getState_id())) {
                province.setText("," + provinceList.get(k).getState_name());
            }
        }
        //country
        country.setText(", Pakistan");

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,EditProfile.class);
                intent.putExtra("model",farmerModel);
                intent.putExtra("edit","true");
                startActivity(intent);
            }
        });



    }
}