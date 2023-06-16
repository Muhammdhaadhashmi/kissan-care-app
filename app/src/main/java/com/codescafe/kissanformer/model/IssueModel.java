package com.codescafe.kissanformer.model;

import java.io.Serializable;

public class IssueModel implements Serializable {
    String subject,name,city,date,status,key,userId;

    public IssueModel(String subject, String name, String city, String date, String status, String key) {
        this.subject = subject;
        this.name = name;
        this.city = city;
        this.date = date;
        this.status = status;
        this.key = key;
    }

    public IssueModel(String subject, String name, String city, String date, String status, String key, String userId) {
        this.subject = subject;
        this.name = name;
        this.city = city;
        this.date = date;
        this.status = status;
        this.key = key;
        this.userId = userId;
    }

    public IssueModel() {
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
