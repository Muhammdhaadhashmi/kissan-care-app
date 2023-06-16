package com.codescafe.kissanformer.model;

import java.io.Serializable;

public class CityModel implements Serializable {
    String tehsil_id,tehsil_name,country_id,state_id,district_id;

    public String getTehsil_id() {
        return tehsil_id;
    }

    public void setTehsil_id(String tehsil_id) {
        this.tehsil_id = tehsil_id;
    }

    public String getTehsil_name() {
        return tehsil_name;
    }

    public void setTehsil_name(String tehsil_name) {
        this.tehsil_name = tehsil_name;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }
}
