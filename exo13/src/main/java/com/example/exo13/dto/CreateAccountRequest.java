package com.example.exo13.dto;

public class CreateAccountRequest {

    private String number;
    private String owner;

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
}
