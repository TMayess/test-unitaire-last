package com.example.exo11.model;

public class Ticket {
    private String id;
    private String title;
    private Priority priority;
    private TicketStatus status;

    public Ticket() {}

    public Ticket(String id, String title, Priority priority, TicketStatus status) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public TicketStatus getStatus() { return status; }
    public void setStatus(TicketStatus status) { this.status = status; }
}
