package com.codescafe.kissanformer.model;

import java.io.Serializable;

public class FarmerModel implements Serializable {
    String id,
            hub_code,
            responsible_person,
            lg_address,
            union_council,
            city,
            district,
            province,
            country,
            ffname,
            ffmobile,
            lfname,
            lfmobile,
            fcode,
            date,
            image,
            cnic,
            father_name,
            Year_of_Birth,
            qaulification,
            active,
            gender,
            farmer_status,
            total_area,
            cotton_area,
            expected_area,
            total_production,
            pu_vg,
            pu_code,
            fcf_id,
            fpum_id,
            created_at;

    public FarmerModel(String id, String hub_code, String responsible_person, String lg_address, String union_council, String city, String district, String province, String country, String ffname, String ffmobile, String lfname, String lfmobile, String fcode, String date, String image, String cnic, String father_name, String year_of_Birth, String qaulification, String active, String gender, String farmer_status, String total_area, String cotton_area, String expected_area, String total_production, String pu_vg, String pu_code, String fcf_id, String fpum_id, String created_at) {
        this.id = id;
        this.hub_code = hub_code;
        this.responsible_person = responsible_person;
        this.lg_address = lg_address;
        this.union_council = union_council;
        this.city = city;
        this.district = district;
        this.province = province;
        this.country = country;
        this.ffname = ffname;
        this.ffmobile = ffmobile;
        this.lfname = lfname;
        this.lfmobile = lfmobile;
        this.fcode = fcode;
        this.date = date;
        this.image = image;
        this.cnic = cnic;
        this.father_name = father_name;
        Year_of_Birth = year_of_Birth;
        this.qaulification = qaulification;
        this.active = active;
        this.gender = gender;
        this.farmer_status = farmer_status;
        this.total_area = total_area;
        this.cotton_area = cotton_area;
        this.expected_area = expected_area;
        this.total_production = total_production;
        this.pu_vg = pu_vg;
        this.pu_code = pu_code;
        this.fcf_id = fcf_id;
        this.fpum_id = fpum_id;
        this.created_at = created_at;
    }

    public FarmerModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHub_code() {
        return hub_code;
    }

    public void setHub_code(String hub_code) {
        this.hub_code = hub_code;
    }

    public String getResponsible_person() {
        return responsible_person;
    }

    public void setResponsible_person(String responsible_person) {
        this.responsible_person = responsible_person;
    }

    public String getLg_address() {
        return lg_address;
    }

    public void setLg_address(String lg_address) {
        this.lg_address = lg_address;
    }

    public String getUnion_council() {
        return union_council;
    }

    public void setUnion_council(String union_council) {
        this.union_council = union_council;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFfname() {
        return ffname;
    }

    public void setFfname(String ffname) {
        this.ffname = ffname;
    }

    public String getFfmobile() {
        return ffmobile;
    }

    public void setFfmobile(String ffmobile) {
        this.ffmobile = ffmobile;
    }

    public String getLfname() {
        return lfname;
    }

    public void setLfname(String lfname) {
        this.lfname = lfname;
    }

    public String getLfmobile() {
        return lfmobile;
    }

    public void setLfmobile(String lfmobile) {
        this.lfmobile = lfmobile;
    }

    public String getFcode() {
        return fcode;
    }

    public void setFcode(String fcode) {
        this.fcode = fcode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public String getYear_of_Birth() {
        return Year_of_Birth;
    }

    public void setYear_of_Birth(String year_of_Birth) {
        Year_of_Birth = year_of_Birth;
    }

    public String getQaulification() {
        return qaulification;
    }

    public void setQaulification(String qaulification) {
        this.qaulification = qaulification;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFarmer_status() {
        return farmer_status;
    }

    public void setFarmer_status(String farmer_status) {
        this.farmer_status = farmer_status;
    }

    public String getTotal_area() {
        return total_area;
    }

    public void setTotal_area(String total_area) {
        this.total_area = total_area;
    }

    public String getCotton_area() {
        return cotton_area;
    }

    public void setCotton_area(String cotton_area) {
        this.cotton_area = cotton_area;
    }

    public String getExpected_area() {
        return expected_area;
    }

    public void setExpected_area(String expected_area) {
        this.expected_area = expected_area;
    }

    public String getTotal_production() {
        return total_production;
    }

    public void setTotal_production(String total_production) {
        this.total_production = total_production;
    }

    public String getPu_vg() {
        return pu_vg;
    }

    public void setPu_vg(String pu_vg) {
        this.pu_vg = pu_vg;
    }

    public String getPu_code() {
        return pu_code;
    }

    public void setPu_code(String pu_code) {
        this.pu_code = pu_code;
    }

    public String getFcf_id() {
        return fcf_id;
    }

    public void setFcf_id(String fcf_id) {
        this.fcf_id = fcf_id;
    }

    public String getFpum_id() {
        return fpum_id;
    }

    public void setFpum_id(String fpum_id) {
        this.fpum_id = fpum_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
