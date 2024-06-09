package com.iot.lab6.entity;

import com.google.firebase.Timestamp;

public class Outcome {

    String tittle;
    Double amount;
    String description;
    com.google.firebase.Timestamp date;
    String userId;

    public Outcome() {
    }

    public Outcome(String tittle, Double amount, String description, com.google.firebase.Timestamp date, String userId) {
        this.tittle = tittle;
        this.amount = amount;
        this.description = description;
        this.date = date;

    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
