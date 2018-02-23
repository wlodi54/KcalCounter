package com.example.sywlia.licznikkaloryczny.models;


public class DisciplineSubDTO {
    public static final String DISCIPLINESUB_ID="DISCIPLINESUB_ID";
    public static final String DISCIPLINE_ID="DISCIPLINESUB_ID_DISCIPLINE";
    public static final String DISCIPLINESUB_MET="DISCIPLINE_MET";
    public static final String DISCIPLINESUB_MINSPEED="DISCIPLINESUB_MINSPEED";
    public static final String DISCIPLINESUB_MAXSPEED="DISCIPLINESUB_MAXSPEED";
    public static final String DISCIPLINESUB_TABLE="DISCIPLINESUB";
    private double minSpeed;//km/h
    private double maxSpeed;
    private long disciplineId;
    private long id;
    private double met;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getMet() {
        return met;
    }

    public void setMet(double met) {
        this.met = met;
    }

    public double getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(double minSpeed) {
        this.minSpeed = minSpeed;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public long getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(long disciplineId) {
        this.disciplineId = disciplineId;
    }



}
