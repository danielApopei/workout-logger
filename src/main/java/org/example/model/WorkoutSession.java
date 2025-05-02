package org.example.model;

import java.time.LocalDateTime;
import java.util.List;

public class WorkoutSession {
    private int id;
    private int userId;
    private LocalDateTime sessionDate;
    private String notes;
    private List<ExerciseEntry> exercises;

    public WorkoutSession(int id, int userId, LocalDateTime sessionDate, String notes) {
        this.id = id;
        this.userId = userId;
        this.sessionDate = sessionDate;
        this.notes = notes;
    }

    public WorkoutSession(int userId, LocalDateTime sessionDate, String notes) {
        this(0, userId, sessionDate, notes);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDateTime sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<ExerciseEntry> getExercises() {
        return exercises;
    }

    public void setExercises(List<ExerciseEntry> exercises) {
        this.exercises = exercises;
    }
}
