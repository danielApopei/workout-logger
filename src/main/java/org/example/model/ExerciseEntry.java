package org.example.model;

public class ExerciseEntry {
    private int id;
    private int sessionId;
    private ExerciseType type;
    private double weight;
    private int reps;
    private int sets;
    private Integer durationSeconds;  // nullable

    public ExerciseEntry(int id, int sessionId, ExerciseType type,
                         double weight, int reps, int sets, Integer durationSeconds) {
        this.id = id;
        this.sessionId = sessionId;
        this.type = type;
        this.weight = weight;
        this.reps = reps;
        this.sets = sets;
        this.durationSeconds = durationSeconds;
    }
    public ExerciseEntry(int sessionId, ExerciseType type,
                         double weight, int reps, int sets, Integer durationSeconds) {
        this(0, sessionId, type, weight, reps, sets, durationSeconds);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public ExerciseType getType() {
        return type;
    }

    public void setType(ExerciseType type) {
        this.type = type;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }
}
