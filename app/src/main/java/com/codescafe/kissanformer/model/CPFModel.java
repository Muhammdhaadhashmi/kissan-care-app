package com.codescafe.kissanformer.model;

import java.io.Serializable;

public class CPFModel implements Serializable {
    String cpf_id,
            tc_id,
            pum_id,
            ff_id;

    public String getCpf_id() {
        return cpf_id;
    }

    public void setCpf_id(String cpf_id) {
        this.cpf_id = cpf_id;
    }

    public String getTc_id() {
        return tc_id;
    }

    public void setTc_id(String tc_id) {
        this.tc_id = tc_id;
    }

    public String getPum_id() {
        return pum_id;
    }

    public void setPum_id(String pum_id) {
        this.pum_id = pum_id;
    }

    public String getFf_id() {
        return ff_id;
    }

    public void setFf_id(String ff_id) {
        this.ff_id = ff_id;
    }
}
