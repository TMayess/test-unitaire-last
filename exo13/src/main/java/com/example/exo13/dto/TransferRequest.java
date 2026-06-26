package com.example.exo13.dto;

public class TransferRequest {

    private String sourceNumber;
    private String targetNumber;
    private double amount;

    public TransferRequest() {}

    public TransferRequest(String sourceNumber, String targetNumber, double amount) {
        this.sourceNumber = sourceNumber;
        this.targetNumber = targetNumber;
        this.amount = amount;
    }

    public String getSourceNumber() { return sourceNumber; }
    public void setSourceNumber(String sourceNumber) { this.sourceNumber = sourceNumber; }

    public String getTargetNumber() { return targetNumber; }
    public void setTargetNumber(String targetNumber) { this.targetNumber = targetNumber; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}
