package com.example.exo13.dto;

public class AmountRequest {

    private double amount;

    public AmountRequest() {}

    public AmountRequest(double amount) {
        this.amount = amount;
    }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}
