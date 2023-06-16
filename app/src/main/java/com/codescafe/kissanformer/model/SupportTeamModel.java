package com.codescafe.kissanformer.model;

import java.io.Serializable;

public class SupportTeamModel implements Serializable {
    String admin_id,
            admin_name,
            admin_email;

    public SupportTeamModel(String admin_id, String admin_name, String admin_email) {
        this.admin_id = admin_id;
        this.admin_name = admin_name;
        this.admin_email = admin_email;
    }

    public SupportTeamModel() {
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public String getAdmin_email() {
        return admin_email;
    }

    public void setAdmin_email(String admin_email) {
        this.admin_email = admin_email;
    }
}
