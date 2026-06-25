package com.example.exo11.dto;

import com.example.exo11.model.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateTicketRequest {

    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 3, message = "Le titre doit contenir au moins 3 caractères")
    private String title;

    @NotNull(message = "La priorité est obligatoire")
    private Priority priority;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
}
