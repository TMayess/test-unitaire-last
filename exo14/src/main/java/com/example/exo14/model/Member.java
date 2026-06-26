package com.example.exo14.model;

public class Member {

    private String id;
    private String name;
    private boolean suspended;
    private int lateReturnsThisYear;

    public Member() {}

    public Member(String id, String name) {
        this.id = id;
        this.name = name;
        this.suspended = false;
        this.lateReturnsThisYear = 0;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isSuspended() { return suspended; }
    public void setSuspended(boolean suspended) { this.suspended = suspended; }

    public int getLateReturnsThisYear() { return lateReturnsThisYear; }
    public void setLateReturnsThisYear(int lateReturnsThisYear) { this.lateReturnsThisYear = lateReturnsThisYear; }
}
