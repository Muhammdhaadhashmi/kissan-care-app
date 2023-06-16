package com.codescafe.kissanformer.model;

import java.io.Serializable;

public class EducationModel implements Serializable {
    String edu_id,
            edu_name;

    public String getEdu_id() {
        return edu_id;
    }

    public void setEdu_id(String edu_id) {
        this.edu_id = edu_id;
    }

    public String getEdu_name() {
        return edu_name;
    }

    public void setEdu_name(String edu_name) {
        this.edu_name = edu_name;
    }
}
