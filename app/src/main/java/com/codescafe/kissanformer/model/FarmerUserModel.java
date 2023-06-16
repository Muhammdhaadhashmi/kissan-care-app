package com.codescafe.kissanformer.model;

import java.io.Serializable;

public class FarmerUserModel implements Serializable {
    String name,id,city;

    public FarmerUserModel(String name, String id, String city) {
        this.name = name;
        this.id = id;
        this.city = city;
    }

    public FarmerUserModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
