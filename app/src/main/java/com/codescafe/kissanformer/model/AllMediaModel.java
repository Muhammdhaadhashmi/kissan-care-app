package com.codescafe.kissanformer.model;

import java.io.Serializable;

public class AllMediaModel implements Serializable {
    String far_src, far_type, far_description;

    public AllMediaModel(String far_src, String far_type, String far_description) {
        this.far_src = far_src;
        this.far_type = far_type;
        this.far_description = far_description;
    }

    public AllMediaModel() {
    }

    public String getFar_src() {
        return far_src;
    }

    public void setFar_src(String far_src) {
        this.far_src = far_src;
    }

    public String getFar_type() {
        return far_type;
    }

    public void setFar_type(String far_type) {
        this.far_type = far_type;
    }

    public String getFar_description() {
        return far_description;
    }

    public void setFar_description(String far_description) {
        this.far_description = far_description;
    }
}
