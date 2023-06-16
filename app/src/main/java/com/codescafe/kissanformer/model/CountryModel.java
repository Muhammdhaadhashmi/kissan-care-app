package com.codescafe.kissanformer.model;

import java.io.Serializable;

public class CountryModel implements Serializable {

    String country_id,country_name,country_status;


    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCountry_status() {
        return country_status;
    }

    public void setCountry_status(String country_status) {
        this.country_status = country_status;
    }
}
