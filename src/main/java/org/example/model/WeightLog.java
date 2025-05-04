package org.example.model;

import java.time.LocalDate;

public class WeightLog {
    private int id;
    private int userId;
    private LocalDate logDate;
    private double weight;
    private String notes;

    public WeightLog(int id, int userId, LocalDate logDate, double weight, String notes) {
        this.id = id;
        this.userId = userId;
        this.logDate = logDate;
        this.weight = weight;
        this.notes = notes;
    }

    public WeightLog(int userId, LocalDate logDate, double weight, String notes) {
        this(0, userId, logDate, weight, notes);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public LocalDate getLogDate() { return logDate; }
    public void setLogDate(LocalDate logDate) { this.logDate = logDate; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}