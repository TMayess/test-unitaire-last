package com.example.exo13.model;

public class Account {

    private String number;
    private String owner;
    private double balance;

    public Account() {}

    public Account(String number, String owner, double balance) {
        this.number = number;
        this.owner = owner;
        this.balance = balance;
    }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}
